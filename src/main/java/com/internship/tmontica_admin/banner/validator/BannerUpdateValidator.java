package com.internship.tmontica_admin.banner.validator;


import com.internship.tmontica_admin.banner.model.request.BannerUpdateRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Date;

@Component
public class BannerUpdateValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BannerUpdateRequest.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        BannerUpdateRequest bannerRequest = (BannerUpdateRequest) o;

        // 기한 체크
        Date startDate = bannerRequest.getStartDate();
        Date endDate = bannerRequest.getEndDate();
        if(startDate.after(endDate) || endDate.before(startDate)){
            errors.rejectValue("startDate", "wrongValue", "기한이 올바르지 않습니다.");
            errors.rejectValue("endDate", "wrongValue", "기한이 올바르지 않습니다.");
        }


    }
}
