package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.point.Point;
import com.internship.tmontica_admin.point.PointDao;
import com.internship.tmontica_admin.point.PointLogType;
import com.internship.tmontica_admin.user.UserDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderScheduler {
    private final OrderDao orderDao;
    private final UserDao userDao;
    private final PointDao pointDao;

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

            // 포인트를 사용한 주문만 걸러내기
            List<Order> pointOrders = orders.stream()
                    .filter(x -> x.getUsedPoint() > 0)
                    .collect(Collectors.toList());
            log.info("[OrderScheduler] : {}", pointOrders.size() + "orders refunded point");
            // 사용한 포인트 환불
            userDao.updateUserPointList(pointOrders);
            // 포인트 로그에 추가
            pointDao.addPointList(pointOrders, PointLogType.GET_POINT.getType(), "주문취소 환불");

        }

        log.info("[OrderScheduler] end Order scheduler : {}", orders.size() + " orders canceled");
    }

}
