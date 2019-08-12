package com.internship.tmontica_admin.menu;


import com.internship.tmontica_admin.menu.exception.MenuValidException;
import com.internship.tmontica_admin.menu.model.request.MenuMonthlyUpdateRequest;
import com.internship.tmontica_admin.menu.model.request.MenuRequest;
import com.internship.tmontica_admin.menu.model.request.MenuUpdateRequest;
import com.internship.tmontica_admin.menu.model.response.MenuByPageResponse;
import com.internship.tmontica_admin.menu.model.response.MenuCategoryResponse;
import com.internship.tmontica_admin.menu.model.response.MenuDetailResponse;
import com.internship.tmontica_admin.menu.validaton.MenuUpdateValidator;
import com.internship.tmontica_admin.menu.validaton.MenuValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


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

    @InitBinder("menuRequest")
    private void initMenuBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(menuValidator);
    }

    @InitBinder("menuUpdateRequest")
    private void initMenuUpdateBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(menuUpdateValidator);
    }


    /** 전체 메뉴 가져오기 (관리자용) **/
    @GetMapping
    public ResponseEntity<MenuByPageResponse> getAllMenus(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
                                                          @RequestParam(value = "size", required = false, defaultValue = "10")int size){

        MenuByPageResponse menuByPageResponse = menuService.getAllMenus(page, size);
        return new ResponseEntity<>(menuByPageResponse, HttpStatus.OK);

    }

    /** 카테고리 별 메뉴 가져오기 (관리자용) **/
    @GetMapping("/{category:[a-z-]+}")
    public ResponseEntity<MenuCategoryResponse> getMenusByCategory(@PathVariable("category")String category,
                                                                   @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                                                   @RequestParam(value = "size", required = false, defaultValue = "10") int size){

        MenuCategoryResponse menuCategoryResponse = menuService.getMenusByCategory(category, page, size);
        menuCategoryResponse.setCategoryEng(category);
        menuCategoryResponse.setCategoryKo(CategoryName.convertEngToKo(category));

        for(Menu menu : menuCategoryResponse.getMenus())
            menu.setImgUrl("/images/".concat(menu.getImgUrl()));

        return new ResponseEntity<>(menuCategoryResponse, HttpStatus.OK);
    }

    /** 상세 메뉴 정보 가져오기 **/
    @GetMapping("/{menuId:\\d+}")
    public ResponseEntity<MenuDetailResponse> getMenuDetail(@PathVariable("menuId")int menuId){
        MenuDetailResponse menuDetailResponse = menuService.getMenuDetailById(menuId);
        // 메뉴가 없으면 no content
        if(menuDetailResponse == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(menuDetailResponse, HttpStatus.OK);
    }

    /** 메뉴 추가하기 **/
    @PostMapping
    public ResponseEntity addMenu(@ModelAttribute @Valid MenuRequest menuRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new MenuValidException("Menu Create Form", "메뉴 추가 폼 데이터가 올바르지 않습니다.", bindingResult);
        }
        log.info("[menu api] 메뉴 추가하기");
        log.info("menuRequest : {}", menuRequest.toString());

        Menu menu = new Menu();
        modelMapper.map(menuRequest, menu);

        // 메뉴 저장
        menuService.addMenu(menu, menuRequest.getOptionIds(), menuRequest.getImgFile());
        return new ResponseEntity(HttpStatus.OK);
    }

    /** 메뉴 수정하기 **/
    @PutMapping
    public ResponseEntity updateMenu(@ModelAttribute @Valid MenuUpdateRequest menuUpdateRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            throw new MenuValidException("Menu Update Form", "메뉴 수정 폼 데이터가 올바르지 않습니다.", bindingResult);
        }
        log.info("[menu api] 메뉴 수정하기");
        log.info("menuReq : {}", menuUpdateRequest.toString());

        Menu menu = new Menu();
        menu.setId(menuUpdateRequest.getMenuId());
        modelMapper.map(menuUpdateRequest, menu);

        int result = menuService.updateMenu(menu, menuUpdateRequest.getOptionIds(), menuUpdateRequest.getImgFile());
        return new ResponseEntity(result < 1 ? HttpStatus.BAD_REQUEST : HttpStatus.OK);
    }

    /** 메뉴 삭제하기 **/
    @DeleteMapping("/{menuId}")
    public ResponseEntity deleteMenu(@PathVariable("menuId")int menuId ){
        log.info("[menu api] 메뉴 삭제하기");
        log.info("menuId : {}", menuId);

        menuService.deleteMenu(menuId);
        return new ResponseEntity(HttpStatus.OK);
    }

    /** 이달의 메뉴 여러개 변경하기 **/
    @PutMapping("/monthly-menu")
    public ResponseEntity updateMonthlyMenu(@RequestBody MenuMonthlyUpdateRequest menuMonthlyUpdateRequest){
        menuService.updateMonthlyMenu(menuMonthlyUpdateRequest.getMenuIds());
        return new ResponseEntity(HttpStatus.OK);
    }


}
