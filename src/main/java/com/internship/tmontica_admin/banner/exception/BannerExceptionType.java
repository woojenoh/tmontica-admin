package com.internship.tmontica_admin.banner.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BannerExceptionType {
    USEPAGE_MISMATCH_EXCEPTION("usePage", "존재하지 않는 페이지 타입 입니다.", HttpStatus.BAD_REQUEST),
    ADD_BANNER_EXCEPTION("addBanner", "배너 추가하기가 실패했습니다.", HttpStatus.BAD_REQUEST);

    private String field;
    private String message;
    private HttpStatus responseType;
}
