package com.internship.tmontica_admin.order.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum OrderExceptionType {
    INVALID_ORDER_ADD_FORM("order add form", "결제 요청 폼이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    INVALID_STATUS_FORM("status form", "주문상태 수정 폼이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    FORBIDDEN_ACCESS_ORDER_DATA("user id", "해당 주문 데이터에 권한이 없습니다", HttpStatus.FORBIDDEN);


    private String field;
    private String errorMessage;
    private HttpStatus responseType;
}
