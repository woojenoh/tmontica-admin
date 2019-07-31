package com.internship.tmontica_admin.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserResponseMessage {

    LOG_IN_SUCCESS("관리자 로그인 성공.");

    private  String message;
}
