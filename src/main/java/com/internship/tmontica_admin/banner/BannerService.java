package com.internship.tmontica_admin.banner;

import com.internship.tmontica_admin.banner.exception.BannerException;
import com.internship.tmontica_admin.banner.exception.BannerExceptionType;
import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.util.JsonUtil;
import com.internship.tmontica_admin.util.SaveImageFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerDao bannerDao;

    private final SaveImageFile saveImageFile;

    @Value("${menu.imagepath}")
    private String location;

    private final JwtService jwtService;

    // 배너 등록하기
    @Transactional
    public int addBanner(Banner banner, MultipartFile multipartFile){
        // TODO : 등록하는 관리자 정보 가져오기
        String creatorId = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"), "id");
        banner.setCreatorId(creatorId);

        //usePage 영어로 저장
        checkUsePage(banner.getUsePage());
        String usePage = BannerUsePage.convertKoToEng(banner.getUsePage());
        banner.setUsePage(usePage);

        // 이미지 저장
        String imgUrl = saveImageFile.saveImg(multipartFile, usePage , location);
        banner.setImgUrl(imgUrl);
        // 배너 등록
        bannerDao.addBanner(banner);

//        // usable = true 일때 같은 페이지, 같은 번호의 배너를 등록 시 기존의 배너들의 usable = false 로 변경
//        if(banner.isUsable()){
//            bannerDao.updateBannerUnusable(banner.getNumber(), banner.getUsePage(), banner.getId());
//        }
        return banner.getId();
    }

    // usePage로 배너 가져오기
    public List<Banner> getBannersByPage(String usePage){
        checkUsePageEng(usePage);
        List<Banner> banners = bannerDao.getBannersByUsePage(usePage);
        return banners;
    }

    // id로 배너 가져오기
    public Banner getBannerById(int id){
        return getBannerById(id);
    }
    // 배너 수정하기
    public void updateBanner(Banner banner, MultipartFile multipartFile){

        //usePage 영어로 저장
        checkUsePage(banner.getUsePage());
        String usePage = BannerUsePage.convertKoToEng(banner.getUsePage());
        banner.setUsePage(usePage);

        // 이미지 파일 업데이트
        if(multipartFile == null || multipartFile.isEmpty()){
            Banner beforeBanner = bannerDao.getBannerById(banner.getId());
            banner.setImgUrl(beforeBanner.getImgUrl());
        }else{
            String imgUrl = saveImageFile.saveImg(multipartFile, usePage, location);
            banner.setImgUrl(imgUrl);
        }

        // DB에 업데이트
        bannerDao.updateBanner(banner);
    }
    // 배너 삭제하기
    public void deleteBanner(int id){
        bannerDao.deleteBanner(id);
    }

    // usepage check
    public void checkUsePage(String usePage){

        for(BannerUsePage bannerUsePage : BannerUsePage.values()){
            if(bannerUsePage.getUsePageKo().equals(usePage)){
                return;
            }
        }
        throw new BannerException(BannerExceptionType.USEPAGE_MISMATCH_EXCEPTION);
    }

    public void checkUsePageEng(String usePageEng){

        for(BannerUsePage bannerUsePage : BannerUsePage.values()){
            if(bannerUsePage.getUsePageEng().equals(usePageEng)){
                return;
            }
        }
        throw  new BannerException(BannerExceptionType.USEPAGE_MISMATCH_EXCEPTION);
    }

}
