package com.internship.tmontica_admin.order.model.response;

import com.internship.tmontica_admin.order.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersByStatusResp {

    private StatusCountResp statusCount;
    private List<OrderResp> orders;

}
