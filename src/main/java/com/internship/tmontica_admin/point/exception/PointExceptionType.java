package com.internship.tmontica_admin.point.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum PointExceptionType {

    INVALID_POINT_TYPE_EXCEPTION("point type", "부적절한 포인트 타입입니다.", HttpStatus.BAD_REQUEST),
    DATABASE_FAIL_EXCEPTION("dataBase", "잠시후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    POINT_LESS_THEN_ZERO_EXCEPTION("point", "사용하려는 포인트량이 보유한 포인트량을 초과합니다.", HttpStatus.BAD_REQUEST);

    private String field;
    private String message;
    private HttpStatus responseType;
}
