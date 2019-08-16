package com.internship.tmontica_admin.menu.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class MenuValidException extends RuntimeException {
    private String field;
    private String message;
    private BindingResult bindingResult;

    public MenuValidException(String field, String message, BindingResult bindingResult){
        this.field = field;
        this.message = message;
        this.bindingResult = bindingResult;
    }
}
