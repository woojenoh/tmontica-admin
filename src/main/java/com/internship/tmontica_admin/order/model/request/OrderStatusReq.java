package com.internship.tmontica_admin.order.model.request;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
public class OrderStatusReq {
    @NotEmpty
    private List<Integer> orderIds;
    @NotEmpty
    private String status;
}
