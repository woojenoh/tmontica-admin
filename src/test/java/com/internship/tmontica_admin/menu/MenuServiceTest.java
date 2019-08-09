package com.internship.tmontica_admin.menu;

import com.internship.tmontica_admin.menu.exception.MenuException;
import com.internship.tmontica_admin.menu.model.response.MenuByPageResponse;
import com.internship.tmontica_admin.menu.model.response.MenuDetailResponse;
import com.internship.tmontica_admin.menu.model.response.MenuOptionResponse;
import com.internship.tmontica_admin.option.Option;
import com.internship.tmontica_admin.option.OptionDao;
import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.util.SaveImageFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MenuServiceTest {

    @Mock
    private MenuDao menuDao;

    @Mock
    private JwtService jwtService;

    @Mock
    private OptionDao optionDao;

    @Mock
    private SaveImageFile saveImageFile;

    @InjectMocks
    private MenuService menuService;

    @Mock
    private ModelMapper modelMapper;

    List<Menu> menus = new ArrayList<>();

    @Before
    public void setUp(){

        Menu menu1 = new Menu(1,"Americano1",1000, "커피" , "coffee", true, true,
                "imagefile/2019/7/1/Americano11_7bba01ca-5b2b-4309-8b77-7fddd615fe26.png", "풍미 가득한 아메리카노", 1000, 0,
                new Date(), null, "admin", null, 1000, "아메리카노", null, null);

        Menu menu2 = new Menu(2,"Americano2",1000, "커피" , "coffee", true, true,
                "imagefile/2019/7/1/Americano11_7bba01ca-5b2b-4309-8b77-7fddd615fe26.png", "풍미 가득한 아메리카노", 1000, 0,
                new Date(), null, "admin", null, 1000, "아메리카노", null, null);

        Menu menu3 = new Menu(3,"Americano3",1000, "커피" , "coffee", true, true,
                "imagefile/2019/7/1/Americano11_7bba01ca-5b2b-4309-8b77-7fddd615fe26.png", "풍미 가득한 아메리카노", 1000, 0,
                new Date(), null, "admin", null, 1000, "아메리카노", null, null);


        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
    }


    @Test
    public void 전체_메뉴_조회하기(){

        // given
        given(menuDao.getAllMenusByPage(10, 0))
                .willReturn(menus.subList(0, (menus.size() < 10)? menus.size() : 10));

        // when
        final MenuByPageResponse allMenus = menuService.getAllMenus(1, 10);

        // then
        verify(menuDao, atLeastOnce()).getAllMenusByPage(anyInt(), anyInt());
        assertThat(allMenus.getMenus(), is(menus.subList(0, menus.size() < 10 ? menus.size() : 10)));

    }

    @Test
    public void 메뉴_추가하기(){
        Menu menu = Menu.builder().categoryEng("Coffee").categoryKo("커피").usable(true)
                                  .monthlyMenu(true).nameKo("콜드브루").nameEng("Cold Brew")
                                  .description("시원한 콜드브루").discountRate(10).productPrice(2000)
                                  .sellPrice(1800).stock(100).createdDate(new Date()).build();

        List<Integer> optionIds = new ArrayList<>();
        optionIds.add(1);
        optionIds.add(2);

        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("cold-brew","cold-brew-image.file","image/png",new byte[]{1,2,3,4,5,66,7,7,8,9,77,8,9,0});

        given(menuDao.addMenu(menu)).willReturn(1);
        given(jwtService.getUserInfo("userInfo")).willReturn("{ \"id\" : \"test123\"}");


        // when
        int createdId = menuService.addMenu(menu, optionIds, mockMultipartFile);

        // then
        verify(menuDao, atLeastOnce()).addMenu(any(Menu.class));
        assertThat(createdId , is(0));

    }

    @Test(expected = MenuException.class)
    public void 메뉴_수정하기_예외(){
        Menu menu = Menu.builder().id(17).categoryEng("Coffee").categoryKo("커피").usable(true)
                .monthlyMenu(true).nameKo("콜드브루").nameEng("Cold Brew")
                .description("시원한 콜드브루").discountRate(10).productPrice(2000)
                .sellPrice(1800).stock(100).createdDate(new Date()).build();

        List<Integer> optionIds = new ArrayList<>();
        optionIds.add(1);
        optionIds.add(2);

        given(menuDao.getMenuById(17)).willReturn(null);    // 메뉴가 존재하지 않는 경우

        // when
        menuService.updateMenu(menu, optionIds, new MockMultipartFile("cold-brew","cold-brew-image.file","image/png",new byte[]{1,2,3,4,5,66,7,7,8,9,77,8,9,0}));

    }

    @Test
    public void 메뉴_수정하기(){
        Menu menu = Menu.builder().id(15).categoryEng("Coffee").categoryKo("커피").usable(true)
                .monthlyMenu(true).nameKo("콜드브루").nameEng("Cold Brew")
                .description("시원한 콜드브루").discountRate(10).productPrice(2000)
                .sellPrice(1800).stock(100).createdDate(new Date()).build();

        List<Integer> optionIds = new ArrayList<>();
        optionIds.add(1);
        optionIds.add(2);

        given(jwtService.getUserInfo("userInfo")).willReturn("{ \"id\" : \"test123\"}");
        given(menuDao.getMenuById(menu.getId())).willReturn(new Menu());
        given(menuDao.updateMenu(menu)).willReturn(1);
        given(menuDao.deleteMenuOption(menu.getId())).willReturn(1);


        // when
        int result = menuService.updateMenu(menu, optionIds, new MockMultipartFile("cold-brew","cold-brew-image.file","image/png",new byte[]{1,2,3,4,5,66,7,7,8,9,77,8,9,0}));

        // then
        verify(menuDao, atLeastOnce()).deleteMenuOption(menu.getId());
        assertThat(result, is(1));

    }

    @Test
    public void 메뉴_삭제하기(){
        //given
        given(menuDao.deleteMenu(10)).willReturn(1);

        //when
        menuService.deleteMenu(10);

        //then
        verify(menuDao, atLeastOnce()).deleteMenu(10);

    }

    //getMenuDetailById
    @Test
    public void 메뉴_정보_가져오기(){
        Menu menu = Menu.builder().id(10).categoryEng("Coffee").categoryKo("커피").usable(true)
                .monthlyMenu(true).nameKo("콜드브루").nameEng("Cold Brew")
                .description("시원한 콜드브루").discountRate(10).productPrice(2000)
                .sellPrice(1800).stock(100).createdDate(new Date()).build();

        List<Option> options = new ArrayList<>();
        Option size = new Option("SizeUp", 0, "Size");
        Option syrup = new Option("AddSyrup", 300, "Syrup");
        options.add(size);
        options.add(syrup);

        //given
        given(menuDao.getMenuById(10)).willReturn(menu);
        given(menuDao.getOptionsById(10)).willReturn(options);
        given(modelMapper.map(menu, MenuDetailResponse.class)).willReturn(new MenuDetailResponse());
        given(modelMapper.map(options, new TypeToken<List<MenuOptionResponse>>(){}.getType())).willReturn(new ArrayList<MenuOptionResponse>());
        //when
        menuService.getMenuDetailById(10);
        //then
        verify(menuDao, atLeastOnce()).getMenuById(10);
        verify(menuDao, atLeastOnce()).getOptionsById(10);
    }

    //카테고리 별 메뉴 정보 가져오기
    @Test
    public void 카테고리_메뉴_가져오기(){
//        menuDao.getCategoryMenuCnt(category);
//        menuDao.getMenusByCategory(category, size, pagination.getStartList());
    }


    //updateMonthlyMenu
    @Test
    public void 이달의_메뉴_상태_변경(){
        List<Integer> menuIds = new ArrayList<>();
        menuIds.add(1);
        menuIds.add(2);

        Menu menu1 = Menu.builder().id(1).nameKo("카페라떼").monthlyMenu(true).build();
        Menu menu2 = Menu.builder().id(11).nameKo("바나나쉐이크").monthlyMenu(false).build();

        //given
        given(menuDao.getMenuById(1)).willReturn(menu1);
        given(menuDao.getMenuById(2)).willReturn(menu2);

        //when
        menuService.updateMonthlyMenu(menuIds);

        //then
        verify(menuDao, atLeastOnce()).getMenuById(anyInt());
    }
}