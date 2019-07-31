package com.internship.tmontica_admin.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum UserExceptionType {

    NOT_ADMIN_EXCEPTION("userRole","관리자 페이지 로그인 권한이 없습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_MISMATCH_EXCEPTION("password", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    USER_ID_NOT_FOUND_EXCEPTION("userId", "아이디를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private String field;
    private String errorMessage;
    private HttpStatus responseType;
}
