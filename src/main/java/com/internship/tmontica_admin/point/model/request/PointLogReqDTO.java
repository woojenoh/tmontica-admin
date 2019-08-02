package com.internship.tmontica_admin.point.model.request;

import com.internship.tmontica.point.PointLogType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class PointLogReqDTO {

    private String userId;
    @NotNull
    private PointLogType pointLogType;
    @NotNull
    @Min(0)
    private int amount;
    private String description;
}
