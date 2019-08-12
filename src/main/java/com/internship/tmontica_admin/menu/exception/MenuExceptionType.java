package com.internship.tmontica_admin.menu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MenuExceptionType {

    MENU_NOT_EXIST_EXCEPTION("menu" , "존재하지 않는 메뉴입니다." , HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_MISMATCH_EXCEPTION("category", "존재하지 않는 카테고리명 입니다.", HttpStatus.BAD_REQUEST);

    private String field;
    private String errorMessage;
    private HttpStatus responseType;
}
