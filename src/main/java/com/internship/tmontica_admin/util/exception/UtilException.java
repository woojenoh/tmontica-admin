package com.internship.tmontica_admin.util.exception;

import lombok.Getter;

@Getter
public class UtilException extends RuntimeException{

    private String field;
    private String message;
    private UtilExceptionType utilExceptionType;

    public UtilException(UtilExceptionType utilExceptionType){
        this.field = utilExceptionType.getField();
        this.message = utilExceptionType.getMessage();
        this.utilExceptionType = utilExceptionType;
    }
}
