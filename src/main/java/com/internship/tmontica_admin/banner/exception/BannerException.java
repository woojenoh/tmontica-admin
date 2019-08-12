package com.internship.tmontica_admin.banner.exception;

import lombok.Getter;

@Getter
public class BannerException extends RuntimeException {
    private String field;
    private String errorMessage;
    private BannerExceptionType BannerExceptionType;

    public BannerException(BannerExceptionType menuExceptionType){
        this.field = menuExceptionType.getField();
        this.errorMessage = menuExceptionType.getErrorMessage();
        this.BannerExceptionType = menuExceptionType;
    }
}
