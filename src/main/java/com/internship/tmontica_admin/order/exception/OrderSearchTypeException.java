package com.internship.tmontica_admin.order.exception;

import lombok.Getter;

@Getter
public class OrderSearchTypeException extends RuntimeException {
    private String field;
    private String message;
    private OrderExceptionType orderExceptionType;

    public OrderSearchTypeException(OrderExceptionType orderExceptionType){
        this.field = orderExceptionType.getField();
        this.message = orderExceptionType.getMessage();
        this.orderExceptionType = orderExceptionType;
    }
}
