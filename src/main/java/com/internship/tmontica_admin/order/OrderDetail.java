package com.internship.tmontica_admin.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class OrderDetail {

    private int id;         // pk, auto increment
    @NotNull
    private int orderId;    // order id

    private String option;  // 옵션키__값/키__값 의 문자열
    @NotNull
    private int price;      // 옵션 + 메뉴가격 (수량 미적용)
    @NotNull
    private int quantity;   // 수량
    @NotNull
    private int menuId;     // 메뉴아이디

    public OrderDetail(int id, @NotNull int orderId, String option, @NotNull int price, @NotNull int quantity, @NotNull int menuId) {
        this.id = id;
        this.orderId = orderId;
        this.option = option;
        this.price = price;
        this.quantity = quantity;
        this.menuId = menuId;
    }
}
