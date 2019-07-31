package com.internship.tmontica_admin.menu.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class MenuValidException extends RuntimeException {
    private String field;
    private String exceptionMessage;
    private BindingResult bindingResult;

    public MenuValidException(String field, String exceptionMessage, BindingResult bindingResult){
        this.field = field;
        this.exceptionMessage = exceptionMessage;
        this.bindingResult = bindingResult;
    }
}
