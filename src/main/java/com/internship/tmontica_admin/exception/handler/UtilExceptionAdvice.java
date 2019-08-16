package com.internship.tmontica_admin.exception.handler;

import com.internship.tmontica_admin.exception.TmonTicaExceptionFormat;
import com.internship.tmontica_admin.util.exception.UtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UtilExceptionAdvice {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionAdvice.class);

    // 권한
    @ExceptionHandler(UtilException.class)
    public ResponseEntity<TmonTicaExceptionFormat> handleUtilException(UtilException e) {

        log.info("util exception");
        return new ResponseEntity<>(new TmonTicaExceptionFormat("utils", e.getUtilExceptionType().getMessage()), e.getUtilExceptionType().getResponseType());
    }
}
