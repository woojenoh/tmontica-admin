package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.order.exception.OrderExceptionType;
import com.internship.tmontica_admin.order.exception.OrderSearchTypeException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderSearchType {
    USER_ID("주문자", "user_id"),
    ORDER_ID("주문번호", "id"),
    ORDER_STATUS("주문상태", "status"),
    PAYMENT("결제방법", "payment");

    private String searchType;
    private String columName;


    // searchType 값으로 columName 가져오는 메서드
    public static String getBysearchType(String searchType){
        for(OrderSearchType orderSearchType : OrderSearchType.values()){
            if(orderSearchType.getSearchType().equals(searchType)){
                return orderSearchType.getColumName();
            }
        }
        throw new OrderSearchTypeException(OrderExceptionType.INVALID_ORDER_SEARCH_TYPE);
    }
}
