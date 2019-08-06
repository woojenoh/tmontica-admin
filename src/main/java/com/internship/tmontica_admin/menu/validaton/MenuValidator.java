package com.internship.tmontica_admin.menu.validaton;

import com.internship.tmontica_admin.menu.model.request.MenuReq;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MenuValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return MenuReq.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        MenuReq menuReq = (MenuReq) obj;

        // 1. sellPrice
        if(menuReq.getSellPrice() != menuReq.getProductPrice() * (100 - menuReq.getDiscountRate())/100 ){
            errors.rejectValue("sellPrice" , "wrongValue", "판매가격이 잘못되었습니다.");
        }

        if(menuReq.getStartDate()!= null && menuReq.getEndDate()!= null) {
            // 2. startDate - endDate
            if (menuReq.getStartDate().after(menuReq.getEndDate())) {
                errors.rejectValue("startDate", "wrongValue", "시작일이 잘못되었습니다.");
                errors.rejectValue("endDate", "wrongValue", "종료일이 잘못되었습니다.");
            }
        }

        // 3. 이미지 파일
        if(menuReq.getImgFile().isEmpty()){
            errors.rejectValue("imgFile", "wrongValue", "이미지 파일은 필수입니다.");
        }

        if(!menuReq.getImgFile().getContentType().startsWith("image")){
            errors.rejectValue("imgFile", "wrongValue", "올바른 이미지 타입이 아닙니다.");
        }
    }
}
