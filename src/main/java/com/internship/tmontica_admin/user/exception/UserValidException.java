package com.internship.tmontica_admin.user.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class UserValidException extends RuntimeException{
    private String field;
    private String errorMessage;
    private BindingResult bindingResult;

    public UserValidException(String field, String errorMessage, BindingResult bindingResult){
        this.field = field;
        this.errorMessage = errorMessage;
        this.bindingResult = bindingResult;
    }
}
