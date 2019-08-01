package com.internship.tmontica_admin.exception.handler;

import com.internship.tmontica_admin.exception.TmonTicaExceptionFormat;
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
        return new TmonTicaExceptionFormat(e.getField(), e.getExceptionMessage(), e.getBindingResult());
    }

}
