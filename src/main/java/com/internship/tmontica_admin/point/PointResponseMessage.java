package com.internship.tmontica_admin.point;

import lombok.Getter;

@Getter
public enum PointResponseMessage {

    DELETE_POINT_LOG_SUCCESS("Delete all point log."),
    DELETE_POINT_LOG_FAIL("Fail to delete point log."),
    POST_POINT_LOG_SUCCESS("Post point log success."),
    POST_POINT_LOG_FAIL("Fail to post point log");

    private String message;

    PointResponseMessage(String message){

        this.message = message;
    }
}
