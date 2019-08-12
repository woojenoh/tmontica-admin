package com.internship.tmontica_admin.order.model.response;

import com.internship.tmontica_admin.order.Order;
import com.internship.tmontica_admin.paging.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersByStatusResp {

    private Pagination pagination;

    private StatusCountResp statusCount;
    private List<OrderResp> orders;

}
