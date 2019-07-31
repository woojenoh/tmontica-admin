package com.internship.tmontica_admin.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusType {
    BEFORE_PAYMENT("미결제"),
    AFTER_PAYMENT("결제완료"),
    IN_PRODUCTION("제작중"),
    READY("준비완료"),
    PICK_UP("픽업완료"),
    CANCEL("주문취소");

    private String status;

}
