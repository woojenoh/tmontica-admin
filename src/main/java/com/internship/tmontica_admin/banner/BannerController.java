package com.internship.tmontica_admin.banner;

import com.internship.tmontica_admin.banner.exception.BannerValidException;
import com.internship.tmontica_admin.banner.model.request.BannerRequest;
import com.internship.tmontica_admin.banner.model.request.BannerUpdateRequest;
import com.internship.tmontica_admin.banner.validator.BannerUpdateValidator;
import com.internship.tmontica_admin.banner.validator.BannerValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    private final ModelMapper modelMapper;

    private final BannerValidator bannerValidator;

    private final BannerUpdateValidator bannerUpdateValidator;

    @InitBinder("bannerRequest")
    private void initAddBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(bannerValidator);
    }

    @InitBinder("bannerUpdateReuqest")
    private void initUpdateBinder(WebDataBinder dataBinder){ dataBinder.addValidators(bannerUpdateValidator);}

    /**  배너 추가하기 **/
    @PostMapping
    public ResponseEntity createBanner(@ModelAttribute @Valid BannerRequest bannerRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BannerValidException("Banner create form" , "배너 추가 폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        Banner banner = modelMapper.map(bannerRequest, Banner.class);
        bannerService.addBanner(banner, bannerRequest.getImgFile());

        return new ResponseEntity(HttpStatus.CREATED);
    }

    /** id로 배너 조회하기 **/
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Banner> getBannerById(@PathVariable int id){
        Banner banner = bannerService.getBannerById(id);
        banner.setImgUrl("/images/".concat(banner.getImgUrl()));
        return new ResponseEntity<>(banner, HttpStatus.OK);
    }

    /** 전체 배너 가져오기 **/
    @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners(){
        List<Banner> banners = bannerService.getAllBanners();

        if(banners.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(banners, HttpStatus.OK);
    }

    /** 배너 업데이트 **/
    @PutMapping
    public ResponseEntity updateBanner(@ModelAttribute @Valid BannerUpdateRequest bannerUpdateRequest, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BannerValidException("Banner update form", "배너 업데이트 폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        Banner banner = modelMapper.map(bannerUpdateRequest, Banner.class);
        bannerService.updateBanner(banner, bannerUpdateRequest.getImgFile());

        return new ResponseEntity(HttpStatus.OK);

    }

    /** 배너 삭제 **/
    @DeleteMapping("/{id}")
    public ResponseEntity deleteBanner(@PathVariable int id){
        bannerService.deleteBanner(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
