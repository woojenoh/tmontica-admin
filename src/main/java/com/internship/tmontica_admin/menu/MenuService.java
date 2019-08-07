package com.internship.tmontica_admin.menu;

import com.internship.tmontica_admin.menu.exception.MenuException;
import com.internship.tmontica_admin.menu.exception.MenuExceptionType;
import com.internship.tmontica_admin.menu.model.response.MenuByPageResp;
import com.internship.tmontica_admin.menu.model.response.MenuCategoryResp;
import com.internship.tmontica_admin.menu.model.response.MenuDetailResp;
import com.internship.tmontica_admin.menu.model.response.MenuOptionResp;
import com.internship.tmontica_admin.option.Option;
import com.internship.tmontica_admin.paging.Pagination;
import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.util.JsonUtil;
import com.internship.tmontica_admin.util.SaveImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuDao menuDao;

    private final ModelMapper modelMapper;

    private final JwtService jwtService;

    private final SaveImageFile saveImageFile;

    @Value("${menu.imagepath}")
    private String location;

    // 메뉴 추가
    @Transactional
    public int addMenu(Menu menu, List<Integer>optionIds, MultipartFile imgFile){
        String imgUrl = saveImageFile.saveImg(imgFile, menu.getNameEng(), location);
        menu.setImgUrl(imgUrl);

        // 등록인 정보 가져오기
        String creatorId = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"), "id");
        menu.setCreatorId(creatorId);

        menuDao.addMenu(menu);

        // 메뉴의 옵션 추가
        for(int optionId : optionIds)
            menuDao.addMenuOption(menu.getId(), optionId);

        return menu.getId();
    }

    // 하나의 메뉴 상세 정보 가져오기 -- 관리자는 사용 불가능한 메뉴도 보여준다.
    public MenuDetailResp getMenuDetailById(int id){
        Menu menu = menuDao.getMenuById(id);

        // 메뉴의 옵션 정보 가져오기
        List<Option> options = menuDao.getOptionsById(id);

        if(menu == null){
            throw new MenuException(MenuExceptionType.MENU_NOT_EXIST_EXCEPTION);
        }

        MenuDetailResp menuDetailResp = modelMapper.map(menu, MenuDetailResp.class);
        List<MenuOptionResp> menuOptions = modelMapper.map(options, new TypeToken<List<MenuOptionResp>>(){}.getType());

        menuDetailResp.setOption(menuOptions);
        menuDetailResp.setImgUrl("/images/".concat(menuDetailResp.getImgUrl()));

        return menuDetailResp;
    }

    // 카테고리 별 메뉴 정보 가져오기
    public MenuCategoryResp getMenusByCategory(String category, int page, int size){
        checkCategoryName(category);
        // 메뉴 전체 갯수
        int totalCnt = menuDao.getCategoryMenuCnt(category);
        // 페이지 객체 생성
        Pagination pagination = new Pagination();
        pagination.pageInfo(page, size, totalCnt);

        List<Menu> menus = menuDao.getMenusByCategory(category, size, pagination.getStartList());

        MenuCategoryResp menuCategoryResp = new MenuCategoryResp();
        menuCategoryResp.setMenus(menus);
        menuCategoryResp.setPagination(pagination);

        return menuCategoryResp;
    }

    // 메뉴 정보 가져오기 (전체)
    public MenuByPageResp getAllMenus(int page, int size){
        // 메뉴 전체 갯수
        int totalCnt = menuDao.getAllMenuCnt();
        // 페이지 객체 생성
        Pagination pagination = new Pagination();
        pagination.pageInfo(page, size, totalCnt);
        // 페이지에 맞는 메뉴 리스트 가져오기.
        List<Menu> menus = menuDao.getAllMenusByPage(size, pagination.getStartList());

        MenuByPageResp menuByPageResp = new MenuByPageResp();
        menuByPageResp.setMenus(menus);
        menuByPageResp.setPagination(pagination);
        return menuByPageResp;

    }

    // 사용 가능한 메뉴 정보 가져오기 (전체)
    public List<Menu> getAllUsableMenus(int page, int size){
        // 페이지에 맞는 메뉴들만 리턴한다.
        List<Menu> usableMenus = MenuScheduler.getUsableMenus();
        // 사용가능한 메뉴가 존재하지 않을 경우
        if(usableMenus.isEmpty()){
            return new ArrayList<>();
        }
        return getMenusByPage(page, size, usableMenus);

    }

    // 카테고리 별 사용 가능한 메뉴 정보 가져오기
    public List<Menu> getUsableMenusByCategory(String category, int page, int size){
        // 카테고리 이름 체크
        checkCategoryName(category);

        List<Menu> usableMenus = MenuScheduler.getUsableMenus();
        // 사용가능한 메뉴가 존재하지 않을 경우
        if(usableMenus.isEmpty()){
            return new ArrayList<>();
        }
        // 해당 카테고리의 모든 메뉴를 가져온다.
        List<Menu> categoryMenus = usableMenus.stream().filter(menu -> menu.getCategoryEng().equals(category))
                .collect(Collectors.toList());
        // 가져온 메뉴들 중 페이지에 맞는 메뉴들만 리턴한다.
        return getMenusByPage(page, size, categoryMenus);

    }

    private List<Menu> getMenusByPage(int page, int size, List<Menu> menus) {
        int startIndex = (page - 1) * size;

        if(startIndex < 0 || startIndex >= menus.size())
            return new ArrayList<>();

        int endIndex = (startIndex + size < menus.size())? startIndex + size : menus.size();

        return menus.subList(startIndex, endIndex);
    }


    // 하나의 메뉴 정보 가져오기
    public Menu getMenuById(int id){
        return menuDao.getMenuById(id);
    }

    // 메뉴 수정하기
    public int updateMenu(Menu menu, List<Integer>optionIds, MultipartFile imgFile){
        if(!existMenu(menu.getId()))
            throw new MenuException(MenuExceptionType.MENU_NOT_EXIST_EXCEPTION);

        String updaterId = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"), "id");
        menu.setUpdaterId(updaterId);

        if(imgFile==null || imgFile.isEmpty()){
            Menu beforeMenu = getMenuById(menu.getId());
            menu.setImgUrl(beforeMenu.getImgUrl());
        }else{
            String img = saveImageFile.saveImg(imgFile, menu.getNameEng(), location);
            menu.setImgUrl(img);
        }

        // 메뉴 옵션
        menuDao.deleteMenuOption(menu.getId());
        for(int optionId : optionIds) {
            menuDao.addMenuOption(menu.getId(), optionId);
        }
        menu.setUpdatedDate(new Date());
        return menuDao.updateMenu(menu);
    }

    // 메뉴 삭제하기
    public void deleteMenu(int id) {
        int result = menuDao.deleteMenu(id);
        if(result < 1) throw new MenuException(MenuExceptionType.MENU_NOT_EXIST_EXCEPTION);
    }


    // 메뉴 존재하는지 확인
    public boolean existMenu(int id){
        return (menuDao.getMenuById(id) == null) ? false : true;
    }

    // 이달의 메뉴 상태 변경
    @Transactional
    public void updateMonthlyMenu(List<Integer> menuIds){
        for(int id : menuIds){
            Menu menu = menuDao.getMenuById(id);
            menuDao.updateMonthlyMenu(id, !menu.isMonthlyMenu());
        }
    }


    // 올바른 카테고리 이름인지 확인
    public void checkCategoryName(String categoryName){

        for(CategoryName element : CategoryName.values()){
            if(element.getCategoryEng().equals(categoryName)){
                return;
            }
        }
        // 아니면 exception
        throw new MenuException(MenuExceptionType.CATEGORY_NAME_MISMATCH_EXCEPTION);
    }


}
