package com.internship.tmontica_admin.order.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class OrderValidException extends RuntimeException{
    private String field;
    private String message;
    private OrderExceptionType orderExceptionType;
    private BindingResult bindingResult;

    public OrderValidException(OrderExceptionType orderExceptionType, BindingResult bindingResult){
        this.field = orderExceptionType.getField();
        this.message = orderExceptionType.getMessage();
        this.orderExceptionType = orderExceptionType;
        this.bindingResult = bindingResult;
    }
}
