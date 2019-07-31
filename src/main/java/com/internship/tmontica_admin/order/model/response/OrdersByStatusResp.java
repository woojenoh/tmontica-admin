package com.internship.tmontica_admin.order.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersByStatusResp {
    // orders
    private int orderId;
    private Date orderDate;
    private String payment;
    private int totalPrice;
    private int usedPoint;
    private int realPrice;
    private String status;
    private String userId;
    // order_details
    List<Order_MenusResp> menus;

    public OrdersByStatusResp(int orderId, Date orderDate, String payment, int totalPrice, int usedPoint, int realPrice, String status, String userId) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.payment = payment;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.realPrice = realPrice;
        this.status = status;
        this.userId = userId;
    }
}
