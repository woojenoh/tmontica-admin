package com.internship.tmontica_admin.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResp {
    private String userId; // 주문한 유저 아이디
    private int orderId;
    private int totalPrice;
    private int usedPoint;
    private int realPrice;
    private List<OrderMenusResp> menus;
    private List<OrderStatusLogResp> orderStatusLogs;
}
