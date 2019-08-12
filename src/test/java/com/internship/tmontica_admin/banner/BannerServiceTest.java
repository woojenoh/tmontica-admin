package com.internship.tmontica_admin.banner;

import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.util.SaveImageFile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockMultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BannerServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private BannerDao bannerDao;

    @Mock
    private SaveImageFile saveImageFile;

    @InjectMocks
    private BannerService bannerService;

    private List<Banner> banners = new ArrayList<>();

    @Before
    public void setUp() throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Banner banner1 = Banner.builder().id(1).link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_TOP.getUsePageEng()).creatorId("admin")
                .imgUrl("/imagefiles/2019/10/1/banner1.png").startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(1).build();

        Banner banner2 = Banner.builder().id(2).link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_TOP.getUsePageEng()).creatorId("admin")
                .imgUrl("/imagefiles/2019/10/1/banner2.png").startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(2).build();

        Banner banner3 = Banner.builder().id(3).link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_BOTTOM.getUsePageEng()).creatorId("admin")
                .imgUrl("/imagefiles/2019/10/1/banner3.png").startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(1).build();

        banners.add(banner1);
        banners.add(banner2);
        banners.add(banner3);

    }

    @Test
    public void 배너_등록하기() throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Banner banner = Banner.builder().link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_BOTTOM.getUsePageKo()).creatorId("admin")
                .startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(1).build();

        //given
        given(jwtService.getUserInfo("userInfo")).willReturn("{ \"id\" : \"admin\"}");
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("mainbanner","mainbanner.png","image/png",new byte[]{1,2,3,4,5,66,7,7,8,9,77,8,9,0});

        given(saveImageFile.saveImg(mockMultipartFile, banner.getUsePage() , "/images/")).willReturn("/imagefiles/2019/8/10/main-top123123123.png");
        given(bannerDao.addBanner(banner)).willReturn(1);

        //when
        bannerService.addBanner(banner, mockMultipartFile);

        //then
        verify(bannerDao, atLeastOnce()).addBanner(banner);
    }

    @Test
    public void userPage로_배너_가져오기() {
        List<Banner> mainTopBanners = banners.stream().filter(b -> b.getUsePage().equals(BannerUsePage.BANNER_MAIN_TOP.getUsePageEng())).collect(Collectors.toList());

        //given
        given(bannerDao.getBannersByUsePage(BannerUsePage.BANNER_MAIN_TOP.getUsePageEng())).willReturn(mainTopBanners);

        //when
        List<Banner> result = bannerService.getBannersByPage(BannerUsePage.BANNER_MAIN_TOP.getUsePageEng());

        //then
        verify(bannerDao, atLeastOnce()).getBannersByUsePage(anyString());
        assertEquals(mainTopBanners, result);
    }

    @Test
    public void 모든_배너_가져오기() {
        //given
        given(bannerDao.getAllBanner()).willReturn(banners);

        //when
        List<Banner> result = bannerService.getAllBanners();

        //then
        verify(bannerDao, atLeastOnce()).getAllBanner();
        assertEquals(banners, result);
    }

    @Test
    public void id로_배너_가져오기() {
        //given
        given(bannerDao.getBannerById(1)).willReturn(banners.get(0));

        //when
        Banner banner = bannerService.getBannerById(1);

        //then
        verify(bannerDao, atLeastOnce()).getBannerById(1);
        assertEquals(banner , banners.get(0));
    }

    @Test
    public void 배너_업데이트() throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Banner banner = Banner.builder().id(1).link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_BOTTOM.getUsePageKo()).creatorId("admin")
                .startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(1).build();

        //given
        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("mainbanner","mainbanner.png","image/png",new byte[]{1,2,3,4,5,66,7,7,8,9,77,8,9,0});

        given(saveImageFile.saveImg(mockMultipartFile, banner.getUsePage() , "/images/")).willReturn("/imagefiles/2019/8/10/main-top123123123.png");

        //when
        bannerService.updateBanner(banner, mockMultipartFile);

        //then
        verify(bannerDao, atLeastOnce()).updateBanner(banner);
    }

    @Test
    public void 배너_업데이트_기존_이미지_사용() throws Exception{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Banner banner = Banner.builder().id(1).link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_BOTTOM.getUsePageKo()).creatorId("admin")
                .startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(1).build();

        Banner beforeBanner = Banner.builder().id(1).link("http://tmontica-idev.tmon.co.kr/")
                .usePage(BannerUsePage.BANNER_MAIN_BOTTOM.getUsePageKo()).creatorId("admin")
                .startDate(sdf.parse("2018-08-28 17:22:21"))
                .endDate(sdf.parse("2020-08-28 17:22:21")).number(1).build();

        //given
        given(bannerDao.getBannerById(1)).willReturn(beforeBanner);

        //when
        bannerService.updateBanner(banner, null);

        //then
        verify(bannerDao, atLeastOnce()).updateBanner(banner);
        verify(bannerDao, atLeastOnce()).getBannerById(1);
    }

    @Test
    public void 배너_삭제하기() {
        //when
        bannerService.deleteBanner(1);
        // then
        verify(bannerDao, atLeastOnce()).deleteBanner(1);

    }

    @Test
    public void checkUsePage() {

    }

    @Test
    public void checkUsePageEng() {
    }
}