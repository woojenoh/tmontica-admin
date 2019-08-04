package com.internship.tmontica_admin.order.model.response;

import com.internship.tmontica_admin.paging.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryResp {
    private Pagination pagination;
    private List<OrderResp> orders;
}
