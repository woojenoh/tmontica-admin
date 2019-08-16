package com.internship.tmontica_admin.banner.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class BannerValidException extends RuntimeException {
    private String field;
    private String message;
    private BindingResult bindingResult;

    public BannerValidException(String field, String message, BindingResult bindingResult){
        this.field = field;
        this.message = message;
        this.bindingResult = bindingResult;
    }
}
