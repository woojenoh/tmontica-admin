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
        String creatorId = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"), "id");
        banner.setCreatorId(creatorId);

        //usePage 체크
        checkUsePage(banner.getUsePage());

        // 이미지 저장
        String imgUrl = saveImageFile.saveImg(multipartFile, banner.getUsePage(), location);
        banner.setImgUrl(imgUrl);
        // 배너 등록
        bannerDao.addBanner(banner);

        return banner.getId();
    }

    //전체 배너 가져오기
    public List<Banner> getAllBanners(){
        List<Banner> banners = bannerDao.getAllBanner();
        return banners;
    }

    // id로 배너 가져오기
    public Banner getBannerById(int id){
        return bannerDao.getBannerById(id);
    }

    // 배너 수정하기
    public void updateBanner(Banner banner, MultipartFile multipartFile){

        //usePage 확인
        checkUsePage(banner.getUsePage());

        // 이미지 파일 업데이트
        if(multipartFile == null || multipartFile.isEmpty()){
            Banner beforeBanner = bannerDao.getBannerById(banner.getId());
            banner.setImgUrl(beforeBanner.getImgUrl());
        }else{
            String imgUrl = saveImageFile.saveImg(multipartFile, banner.getUsePage(), location);
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
    public void checkUsePage(String checkPage){

        for(UsePage usePage : UsePage.values()){
            if(usePage.toString().equals(checkPage)){
                return;
            }
        }
        throw new BannerException(BannerExceptionType.USEPAGE_MISMATCH_EXCEPTION);
    }


}
