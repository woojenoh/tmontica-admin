package com.internship.tmontica_admin.order.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusReq {
    @NotEmpty
    private List<Integer> orderIds;
    @NotEmpty
    private String status;
}
