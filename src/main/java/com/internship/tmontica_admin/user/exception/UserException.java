package com.internship.tmontica_admin.user.exception;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

    private String field;
    private String errorMessage;
    private UserExceptionType userExceptionType;

    public UserException(UserExceptionType userExceptionType){
        this.field = userExceptionType.getField();
        this.errorMessage = userExceptionType.getErrorMessage();
        this.userExceptionType = userExceptionType;
    }
}
