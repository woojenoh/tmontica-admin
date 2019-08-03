package com.internship.tmontica_admin.exception.handler;

import com.internship.tmontica_admin.banner.exception.BannerException;
import com.internship.tmontica_admin.banner.exception.BannerValidException;
import com.internship.tmontica_admin.exception.TmonTicaExceptionFormat;
import com.internship.tmontica_admin.menu.exception.MenuException;
import com.internship.tmontica_admin.menu.exception.MenuValidException;
import com.internship.tmontica_admin.menu.exception.SaveImgException;
import com.internship.tmontica_admin.order.exception.OrderSearchTypeException;
import com.internship.tmontica_admin.order.exception.OrderValidException;
import com.internship.tmontica_admin.security.exception.UnauthorizedException;
import com.internship.tmontica_admin.user.exception.UserException;
import com.internship.tmontica_admin.user.exception.UserValidException;
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

    // 권한
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public TmonTicaExceptionFormat handleUnauthorizedException(UnauthorizedException e) {

        log.info("need authorization");
        return new TmonTicaExceptionFormat("authorization", "권한이 유효하지 않습니다.");
    }

    // 유저
    @ExceptionHandler(UserValidException.class)
    public TmonTicaExceptionFormat handleUserValidException(UserValidException e) {
        log.info("UserValidExceptionMessage : {}" , e.getMessage());
        return new TmonTicaExceptionFormat(e.getField(), e.getErrorMessage(), e.getBindingResult());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<TmonTicaExceptionFormat> handleUserException(UserException e) {
        log.debug("UserExceptionMessage : {}", e.getErrorMessage());
        return new ResponseEntity<>(new TmonTicaExceptionFormat(e.getField(), e.getErrorMessage()), e.getUserExceptionType().getResponseType());
    }

    // 오더
    @ExceptionHandler(OrderValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TmonTicaExceptionFormat handleOrderValidException(OrderValidException e){
        return new TmonTicaExceptionFormat(e.getField(), e.getMessage(), e.getBindingResult());
    }

    @ExceptionHandler(OrderSearchTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TmonTicaExceptionFormat handleOrderSearchTypeException(OrderSearchTypeException e){
        return new TmonTicaExceptionFormat(e.getField(), e.getMessage());
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

    // 배너
    @ExceptionHandler(BannerValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public TmonTicaExceptionFormat handleBannerValidException(BannerValidException e){
        return new TmonTicaExceptionFormat(e.getField(), e.getExceptionMessage(), e.getBindingResult());
    }

    @ExceptionHandler(BannerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<TmonTicaExceptionFormat> handleBannerException(BannerException e){
        return new ResponseEntity<>(new TmonTicaExceptionFormat(e.getField(), e.getErrorMessage()), e.getBannerExceptionType().getResponseType());
    }

}
