package com.internship.tmontica_admin.banner.exception;

import lombok.Getter;

@Getter
public class BannerException extends RuntimeException {
    private String field;
    private String message;
    private BannerExceptionType BannerExceptionType;

    public BannerException(BannerExceptionType menuExceptionType){
        this.field = menuExceptionType.getField();
        this.message = menuExceptionType.getMessage();
        this.BannerExceptionType = menuExceptionType;
    }
}
