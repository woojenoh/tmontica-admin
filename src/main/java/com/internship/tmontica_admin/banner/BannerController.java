package com.internship.tmontica_admin.banner;

import com.internship.tmontica_admin.banner.exception.BannerValidException;
import com.internship.tmontica_admin.banner.model.request.BannerReq;
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

    @InitBinder
    private void initBinder(WebDataBinder dataBinder) {
        dataBinder.addValidators(bannerValidator);
    }

    @PostMapping
    public ResponseEntity createBanner(@ModelAttribute @Valid BannerReq bannerReq , BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BannerValidException("Banner create form" , "배너 추가 폼 데이터가 올바르지 않습니다.", bindingResult);
        }

        Banner banner = modelMapper.map(bannerReq, Banner.class);
        bannerService.addBanner(banner, bannerReq.getImgFile());

        return new ResponseEntity(HttpStatus.CREATED);
    }

    // usePage에 맞는 banner 가져오기.
    @GetMapping("/{usePageEng}")
    public ResponseEntity<List<Banner>> getBannerByNumber(@PathVariable String usePageEng){
        List<Banner> banners = bannerService.getBannersByPage(usePageEng);
        return new ResponseEntity<>(banners, HttpStatus.OK);
    }
}
