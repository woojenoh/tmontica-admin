package com.internship.tmontica_admin.statistic.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum StatisticExceptionType {

    INVALID_DATE_EXCEPTION("user birth date","부적절한 생일 정보 입니다.", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER_OPTION("parameter value", "부적절한 파라미터 값입니다.",HttpStatus.BAD_REQUEST);

    private String field;
    private String message;
    private HttpStatus responseType;
}
