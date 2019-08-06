package com.internship.tmontica_admin.menu;

import com.internship.tmontica_admin.menu.model.response.MenuByPageResp;
import com.internship.tmontica_admin.option.OptionDao;
import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.util.SaveImageFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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



    @Test
    public void 전체_메뉴_조회하기(){
//    //given
//    given(mockRepository.findByname("wonwoo")).willReturn(new Account(1L, "wonwoo", "wonwoo@test.com"));
//    //when
//    final Account account = mockService.findByname("wonwoo");
//    //then
//    verify(mockRepository, atLeastOnce()).findByname(anyString());
//    assertThat(account.getId(), is(1L));
//    assertThat(account.getName(), is("wonwoo"));
//    assertThat(account.getEmail(), is("wonwoo@test.com"));
        List<Menu> menus = new ArrayList<>();
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

        // given
        given(menuDao.getAllMenusByPage(10, 0))
                .willReturn(menus.subList(0, (menus.size() < 10)? menus.size() : 10));

        // when
        final MenuByPageResp allMenus = menuService.getAllMenus(1, 10);

        // then
        verify(menuDao, atLeastOnce()).getAllMenusByPage(anyInt(), anyInt());
        assertThat(allMenus.getMenus().size() ,is(3));

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

        given(menuDao.addMenu(any(Menu.class))).willReturn(1);
        given(saveImageFile.saveImg(any(MockMultipartFile.class),any(String.class),any(String.class))).willReturn("/imagefile/2019/8/5/cold-brew-1234124.png");
        given(jwtService.getUserInfo("userInfo")).willReturn("{ \"id\" : \"test123\"}");


        // when
        int createdId = menuService.addMenu(menu, optionIds, mockMultipartFile);

        // then
        verify(menuDao, atLeastOnce()).addMenu(any(Menu.class));
        assertThat(createdId , is(0));

    }

    @Test
    public void 메뉴_수정하기(){

    }
}