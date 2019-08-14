package com.internship.tmontica_admin.menu.exception;


import lombok.Getter;

@Getter
public class MenuException extends  RuntimeException {

    private String field;
    private String message;
    private MenuExceptionType menuExceptionType;

    public MenuException(MenuExceptionType menuExceptionType){
        this.field = menuExceptionType.getField();
        this.message = menuExceptionType.getMessage();
        this.menuExceptionType = menuExceptionType;
    }
}
