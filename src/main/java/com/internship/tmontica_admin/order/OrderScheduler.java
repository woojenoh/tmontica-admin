package com.internship.tmontica_admin.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderDao orderDao;

    /** 미결제 상태로 30분 이상인 주문 취소 */
    @Scheduled(fixedDelay = 1000*60)
    public void checkOrder(){
        log.info("[scheduler] start Order scheduler");
        // 미결제인 오더만 불러오기
        List<Order> orders = orderDao.getBeforePaymentOrders();
        for (Order order:orders) {
            Date orderDate = order.getOrderDate();
            Date now = new Date();

            // 현재시간과 주문시간의 차이(분)
            long diff = now.getTime() - orderDate.getTime();
            long min = diff/(1000*60);
            log.info("min: {}", min);
            if(min > 30){
                // 주문취소로 상태 변경
                // orders 테이블에서 status 수정
                orderDao.updateOrderStatus(order.getId(), OrderStatusType.CANCEL.getStatus());
                // order_status_log 테이블에도 로그 추가
                OrderStatusLog orderStatusLog = new OrderStatusLog(OrderStatusType.CANCEL.getStatus(), "SYSTEM", order.getId());
                orderDao.addOrderStatusLog(orderStatusLog);
            }
        }
        log.info("[scheduler] end Order scheduler");
    }

}
