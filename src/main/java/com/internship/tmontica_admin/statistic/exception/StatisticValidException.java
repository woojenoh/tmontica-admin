package com.internship.tmontica_admin.statistic.exception;

import org.springframework.validation.BindingResult;

public class StatisticValidException extends RuntimeException{
    private String field;
    private String message;
    private BindingResult bindingResult;

    public StatisticValidException(String field, String message, BindingResult bindingResult){
        this.field = field;
        this.message = message;
        this.bindingResult = bindingResult;
    }
}
