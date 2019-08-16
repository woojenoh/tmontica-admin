package com.internship.tmontica_admin.util.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UtilExceptionType {

    JSON_PARSING_EXCEPTION("json data", "JSON데이터 파싱 에러입니다.", HttpStatus.BAD_REQUEST);

    private String field;
    private String message;
    private HttpStatus responseType;
}
