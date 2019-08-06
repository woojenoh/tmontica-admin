package com.internship.tmontica_admin.statistic.exception;

import lombok.Getter;

@Getter
public class StatisticException extends RuntimeException {

    private String field;
    private String message;
    private StatisticExceptionType statisticExceptionType;

    public StatisticException(StatisticExceptionType statisticExceptionType){
        this.field = statisticExceptionType.getField();
        this.message = statisticExceptionType.getMessage();
        this.statisticExceptionType = statisticExceptionType;
    }
}
