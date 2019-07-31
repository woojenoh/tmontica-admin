package com.internship.tmontica_admin.menu.exception;


import lombok.Getter;

@Getter
public class MenuException extends  RuntimeException {

    private String field;
    private String errorMessage;
    private MenuExceptionType menuExceptionType;

    public MenuException(MenuExceptionType menuExceptionType){
        this.field = menuExceptionType.getField();
        this.errorMessage = menuExceptionType.getErrorMessage();
        this.menuExceptionType = menuExceptionType;
    }
}
