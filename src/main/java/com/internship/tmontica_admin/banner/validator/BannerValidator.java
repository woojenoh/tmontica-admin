package com.internship.tmontica_admin.banner.validator;


import com.internship.tmontica_admin.banner.model.request.BannerReq;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class BannerValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BannerReq.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BannerReq bannerReq = (BannerReq) o;

        // 기한 체크
        Date startDate = bannerReq.getStartDate();
        Date endDate = bannerReq.getEndDate();
        if(startDate.after(endDate) || endDate.before(startDate)){
            errors.rejectValue("startDate", "wrongValue", "기한이 올바르지 않습니다.");
            errors.rejectValue("endDate", "wrongValue", "기한이 올바르지 않습니다.");
        }

        //  이미지 파일
        if(bannerReq.getImgFile().isEmpty()){
            errors.rejectValue("imgFile", "wrongValue", "이미지 파일은 필수입니다.");
        }

        if(!bannerReq.getImgFile().getContentType().startsWith("image")){
            errors.rejectValue("imgFile", "wrongValue", "올바른 이미지 타입이 아닙니다.");
        }

    }
}
