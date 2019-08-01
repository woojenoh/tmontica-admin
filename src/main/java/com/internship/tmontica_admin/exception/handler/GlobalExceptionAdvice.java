package com.internship.tmontica_admin.exception.handler;

import com.internship.tmontica_admin.exception.TmonTicaExceptionFormat;
import com.internship.tmontica_admin.menu.exception.MenuException;
import com.internship.tmontica_admin.menu.exception.MenuValidException;
import com.internship.tmontica_admin.menu.exception.SaveImgException;
import com.internship.tmontica_admin.order.exception.OrderValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);



    // 오더
    @ExceptionHandler(OrderValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TmonTicaExceptionFormat handleOrderValidException(OrderValidException e){
        return new TmonTicaExceptionFormat(e.getField(), e.getExceptionMessage(), e.getBindingResult());
    }

    // 메뉴
    @ExceptionHandler(MenuValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TmonTicaExceptionFormat handleMenuValidException(MenuValidException e) {
        log.info("MenuExceptionMessage : {}" , e.getMessage());
        return new TmonTicaExceptionFormat(e.getField(), e.getExceptionMessage(), e.getBindingResult());
    }

    @ExceptionHandler(SaveImgException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TmonTicaExceptionFormat handleSaveImgException(SaveImgException e){
        return new TmonTicaExceptionFormat("imgFile", "올바른 이미지 파일이 아닙니다.");
    }

    @ExceptionHandler(MenuException.class)
    public ResponseEntity<TmonTicaExceptionFormat> handleMenuException(MenuException e){
        log.info("MenuException : {}" , e.getErrorMessage());
        return new ResponseEntity<>(new TmonTicaExceptionFormat(e.getField(), e.getErrorMessage()), e.getMenuExceptionType().getResponseType());
    }

}
