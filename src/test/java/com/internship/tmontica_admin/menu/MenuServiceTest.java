package com.internship.tmontica_admin.menu;

import com.internship.tmontica_admin.menu.model.response.MenuByPageResp;
import com.internship.tmontica_admin.option.OptionDao;
import com.internship.tmontica_admin.security.JwtService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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
//        //given
//        final MemberSignupRequest request = buildRequest(TEST_EMAIL);
//        given(memberRepository.save(any(Member.class))).willReturn(request.toMember());
//
//        //when
//        final Member member = memberSignUpService.signUp(request);
//
//        //then
//        Mockito.verify(memberRepository, atLeast(1)).save(any(Member.class));
//        Assert.assertThat(member.getEmail(), is(request.getEmail()));

        // given
        Menu menu = Menu.builder().id(1).categoryEng("Coffee").categoryKo("커피").usable(true)
                                  .monthlyMenu(true).nameKo("콜드브루").nameEng("Cold Brew")
                                  .description("시원한 콜드브루").discountRate(10).productPrice(2000)
                                  .sellPrice(1800).stock(100).createdDate(new Date()).build();
        // when


        //given
        //given(menuDao.addMenu(any(Menu.class))).willReturn(menu);

    }
}