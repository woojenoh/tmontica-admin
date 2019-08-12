package com.internship.tmontica_admin.user.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class UserValidException extends RuntimeException{
    private String field;
    private String message;
    private BindingResult bindingResult;

    public UserValidException(String field, String message, BindingResult bindingResult){
        this.field = field;
        this.message = message;
        this.bindingResult = bindingResult;
    }
}
