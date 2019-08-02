package com.internship.tmontica_admin.banner.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class BannerValidException extends RuntimeException {
    private String field;
    private String exceptionMessage;
    private BindingResult bindingResult;

    public BannerValidException(String field, String exceptionMessage, BindingResult bindingResult){
        this.field = field;
        this.exceptionMessage = exceptionMessage;
        this.bindingResult = bindingResult;
    }
}
