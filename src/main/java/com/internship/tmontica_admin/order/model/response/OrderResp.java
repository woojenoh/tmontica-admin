package com.internship.tmontica_admin.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResp {

    private int orderId;
    private Date orderDate;
    private String payment;
    private int totalPrice;
    private int usedPoint;
    private int realPrice;
    private String status;
    private String userId;
    private List<Order_MenusResp> menus;
}
