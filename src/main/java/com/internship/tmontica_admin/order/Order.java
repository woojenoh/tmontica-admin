package com.internship.tmontica_admin.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
public class Order {
    private int id;            // 주문번호

    private Date orderDate;    // 주문날짜
    @NotEmpty
    private String payment;    // 결제수단
    @NotNull
    private int totalPrice;    // 주문 전체 가격

    private int usedPoint;     // 사용한 포인트
    @NotNull
    private int realPrice;     // 실제 지불해야할 금액
    @NotEmpty
    private String status;     // 주문상태
    @NotEmpty
    private String userId;     // 주문한 유저의 아이디

    private String userAgent;  // 주문자의 기기환경?(모바일/웹)


    public Order(int id, @NotEmpty String payment, @NotNull int totalPrice, int usedPoint, @NotNull int realPrice, @NotEmpty String status, @NotEmpty String userId) {
        this.id = id;
        this.payment = payment;
        this.totalPrice = totalPrice;
        this.usedPoint = usedPoint;
        this.realPrice = realPrice;
        this.status = status;
        this.userId = userId;
    }
}
