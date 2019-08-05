package com.internship.tmontica_admin.menu;


import com.internship.tmontica_admin.menu.exception.MenuException;
import com.internship.tmontica_admin.menu.exception.MenuExceptionType;
import com.internship.tmontica_admin.menu.exception.MenuValidException;
import com.internship.tmontica_admin.menu.model.request.MenuReq;
import com.internship.tmontica_admin.menu.model.request.MenuUpdateReq;
import com.internship.tmontica_admin.menu.model.response.MenuCategoryResp;
import com.internship.tmontica_admin.menu.model.response.MenuDetailResp;
import com.internship.tmontica_admin.menu.model.response.MenuSimpleResp;
import com.internship.tmontica_admin.menu.validaton.MenuUpdateValidator;
import com.internship.tmontica_admin.menu.validaton.MenuValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    private final ModelMapper modelMapper;

    private final MenuValidator menuValidator;

    private final MenuUpdateValidator menuUpdateValidator;

    @Value("${menu.imagepath}")
    private String location;

    @InitBinder("menuReq")
    private void initMenuBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(menuValidator);
    }

    @InitBinder("menuUpdateReq")
    private void initMenuUpdateBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(menuUpdateValidator);
    }


    /** 전체 메뉴 가져오기 (관리자용) **/
    @GetMapping
    public ResponseEntity<List<Menu>> getAllMenus(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
                                                  @RequestParam(value = "size", required = false, defaultValue = "10")int size){

        List<Menu> menus = menuService.getAllMenus(page, size);
        if (menus.isEmpty()) {
            throw new MenuException(MenuExceptionType.MENU_NO_CONTENT_EXCEPTION);
        }

        for(Menu menu : menus){
            Date date = menu.getCreatedDate();
        }
        return new ResponseEntity<>(menus, HttpStatus.OK);

    }

    /** 카테고리 별 메뉴 가져오기 (관리자용) **/
    @GetMapping("/{category:[a-z-]+}")
    public ResponseEntity<MenuCategoryResp> getMenusByCategory(@PathVariable("category")String category,
                                                               @RequestParam(value = "page", required = false) int page,
                                                               @RequestParam(value = "size", required = false) int size){

        MenuCategoryResp menucategoryResp = new MenuCategoryResp();
        menucategoryResp.setSize(size);
        menucategoryResp.setPage(page);
        menucategoryResp.setCategoryEng(category);
        menucategoryResp.setCategoryKo(CategoryName.convertEngToKo(category));

        List<Menu> menus = menuService.getMenusByCategory(category, page, size);
        // 메뉴가 없는 경우
        if(menus == null) {
            throw new MenuException(MenuExceptionType.MENU_NO_CONTENT_EXCEPTION);
        }
        List<MenuSimpleResp> categoryMenus = modelMapper.map(menus, new TypeToken<List<MenuSimpleResp>>(){}.getType());

        for(MenuSimpleResp menu : categoryMenus)
            menu.setImgUrl("/images/".concat(menu.getImgUrl()));

        menucategoryResp.setMenus(categoryMenus);
        return new ResponseEntity<>(menucategoryResp, HttpStatus.OK);
    }

    /** 상세 메뉴 정보 가져오기 **/
    @GetMapping("/{menuId:\\d+}")
    public ResponseEntity<MenuDetailResp> getMenuDetail(@PathVariable("menuId")int menuId){
        MenuDetailResp menuDetailResp = menuService.getMenuDetailById(menuId);
        // 메뉴가 없으면 no content
        if(menuDetailResp == null) {
            throw new MenuException(MenuExceptionType.MENU_NO_CONTENT_EXCEPTION);
        }
        return new ResponseEntity<>(menuDetailResp, HttpStatus.OK);
    }

    /** 메뉴 추가하기 **/
    @PostMapping
    public ResponseEntity addMenu(@ModelAttribute @Valid MenuReq menuReq, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new MenuValidException("Menu Create Form", "메뉴 추가 폼 데이터가 올바르지 않습니다.", bindingResult);
        }
        log.info("[menu api] 메뉴 추가하기");
        log.info("menuReq : {}", menuReq.toString());

        Menu menu = new Menu();
        modelMapper.map(menuReq, menu);

        // 메뉴 저장
        menuService.addMenu(menu, menuReq.getOptionIds(), menuReq.getImgFile());
        return new ResponseEntity(HttpStatus.OK);
    }

    /** 메뉴 수정하기 **/
    @PutMapping
    public ResponseEntity updateMenu(@ModelAttribute @Valid MenuUpdateReq menuUpdateReq, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new MenuValidException("Menu Update Form", "메뉴 수정 폼 데이터가 올바르지 않습니다.", bindingResult);
        }
        log.info("[menu api] 메뉴 수정하기");
        log.info("menuReq : {}", menuUpdateReq.toString());

        Menu menu = new Menu();
        menu.setId(menuUpdateReq.getMenuId());
        modelMapper.map(menuUpdateReq, menu);

        menuService.updateMenu(menu, menuUpdateReq.getImgFile());

        return new ResponseEntity(HttpStatus.OK);
    }

    /** 메뉴 삭제하기 **/
    @DeleteMapping("/{menuId}")
    public ResponseEntity deleteMenu(@PathVariable("menuId")int menuId ){
        log.info("[menu api] 메뉴 삭제하기");
        log.info("menuId : {}", menuId);

        menuService.deleteMenu(menuId);
        return new ResponseEntity(HttpStatus.OK);
    }


}
