package com.internship.tmontica_admin.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderDao orderDao;

    /**
     * 미결제 상태로 30분 이상인 주문 취소
     */
    @Scheduled(fixedDelay = 1000 * 60 * 10)
    @Transactional
    public void checkOrder() {
        log.info("[OrderScheduler] start Order scheduler");
        // 미결제인상태로 30분이 지난 오더만 불러오기
        List<Order> orders = orderDao.getBeforePaymentOrdersFor30Minutes();

        if (orders.size() > 0) {
            // 주문취소로 상태 변경
            // orders 테이블에서 status 수정
            orderDao.updateOrderListStatus(orders, OrderStatusType.CANCEL.getStatus());

            // order_status_log 테이블에도 로그 추가
            orderDao.insertOrderStatusLogList(orders, OrderStatusType.CANCEL.getStatus(), "SYSTEM");
        }

        log.info("[OrderScheduler] end Order scheduler : {}", orders.size() + " orders canceled");
    }

}
