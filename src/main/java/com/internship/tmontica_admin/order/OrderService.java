package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.option.Option;
import com.internship.tmontica_admin.option.OptionDao;
import com.internship.tmontica_admin.order.model.request.OrderStatusReq;
import com.internship.tmontica_admin.order.model.response.*;
import com.internship.tmontica_admin.point.Point;
import com.internship.tmontica_admin.point.PointLogType;
import com.internship.tmontica_admin.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderDao orderDao;
    private final OptionDao optionDao;
    private static final double RESERVE_RATE = 10.0;
    private final PointService pointService;

    // 주문 상태 변경 api(관리자)
    public void updateOrderStatusApi(OrderStatusReq orderStatusReq){
//        String userId = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"),"id");
//        // 관리자 권한 검사
//        String role = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"),"role");
//        if(!role.equals(UserRole.ADMIN.toString())){
//            throw new UserException(UserExceptionType.INVALID_USER_ROLE_EXCEPTION);
//        }
        // TODO: 관리자 아이디 받아오기
        String userId = "admin";
        // orderId 리스트 가져오기
        List<Integer> orderIds = orderStatusReq.getOrderIds();
        for (int orderId : orderIds) {
            // orders 테이블에서 status 수정
            orderDao.updateOrderStatus(orderId, orderStatusReq.getStatus());
            // order_status_log 테이블에도 로그 추가
            OrderStatusLog orderStatusLog = new OrderStatusLog(orderStatusReq.getStatus(), userId, orderId);
            orderDao.addOrderStatusLog(orderStatusLog);

            // "픽업완료" 상태로 바뀌면 포인트 적립
            if(orderStatusReq.getStatus().equals(OrderStatusType.PICK_UP.getStatus())){
                Order order = orderDao.getOrderByOrderId(orderId);
                Point point = new Point(order.getUserId(), PointLogType.GET_POINT.getType(),
                        String.valueOf((order.getTotalPrice()*(100-RESERVE_RATE)/100)), "결제 적립금 적립.");
                pointService.updateUserPoint(point);
            }
        }
    }


    // 오늘의 상태별 주문 현황 가져오기 api(관리자)
    public OrdersByStatusResp getTodayOrderByStatusApi(String status, int size, int page) {
//        // 관리자 권한 검사
//        String role = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"),"role");
//        if(!role.equals(UserRole.ADMIN.toString())){
//            throw new UserException(UserExceptionType.INVALID_USER_ROLE_EXCEPTION);
//        }

        List<OrderResp> orderResps = new ArrayList<>();
        // 오늘의 상태별 주문 개수 가져오기
        StatusCountResp statusCountResp = orderDao.getTodayStatusCount();

        // DB에서 오늘의 (상태별) 주문현황 가져오기
        List<Order> orders;
        // 상태 문자열을 보내줬을 경우
        if(!status.equals(OrderStatusType.ALL.toString())){
            orders = orderDao.getTodayOrderByStatus(OrderStatusType.valueOf(status).getStatus());
        }else {
            // 상태 문자열이 default "ALL" 인 경우
            orders = orderDao.getTodayOrders();
        }

        for(Order order : orders){
            List<Order_MenusResp> menus = orderDao.getOrderDetailByOrderId(order.getId());
            for (Order_MenusResp menu : menus) {
                //메뉴 옵션 "1__1/4__2" => "HOT/샷추가(2개)" 로 바꾸는 작업
                if(!menu.getOption().equals("")){
                    String option = menu.getOption();
                    String convert = convertOptionStringToCli(option); // 변환할 문자열
                    menu.setOption(convert);
                }
                // 이미지 url 셋팅
                menu.setImgUrl("/images/".concat(menu.getImgUrl()));
            }
            OrderResp orderResp = new OrderResp(order.getId(), order.getOrderDate(), order.getPayment(), order.getTotalPrice(),
                                    order.getUsedPoint(), order.getRealPrice(), order.getStatus(), order.getUserId(), menus);
            orderResps.add(orderResp);
        }

        OrdersByStatusResp ordersByStatusResps = new OrdersByStatusResp(statusCountResp,orderResps); // 반환할 객체

        return ordersByStatusResps;
    }


    // 주문 상세정보 가져오기 api(관리자)
    public OrderDetailResp getOrderDetailApi(int orderId){
//        // 관리자 권한 검사
//        String role = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"),"role");
//        if(!role.equals(UserRole.ADMIN.toString())){
//            throw new UserException(UserExceptionType.INVALID_USER_ROLE_EXCEPTION);
//        }

        Order order = orderDao.getOrderByOrderId(orderId);
        List<Order_MenusResp> menus = orderDao.getOrderDetailByOrderId(orderId);
        for (Order_MenusResp menu : menus) {
            //메뉴 옵션 "1__1/4__2" => "HOT/샷추가(2개)" 로 바꾸는 작업
            if(!menu.getOption().equals("")){
                String option = menu.getOption();
                String convert = convertOptionStringToCli(option); // 변환할 문자열
                menu.setOption(convert);
            }
            // 이미지 url 셋팅
            menu.setImgUrl("/images/".concat(menu.getImgUrl()));
        }
        List<OrderStatusLogResp> orderStatusLogs = orderDao.getOrderStatusLogByOrderId(orderId);

        OrderDetailResp orderDetailResp = new OrderDetailResp(order.getUserId(), orderId, order.getTotalPrice(),menus, orderStatusLogs);
        return orderDetailResp;
    }





    // DB의 옵션 문자열을 변환하는 메서드
    public String convertOptionStringToCli(String option){
        //메뉴 옵션 "1__1/4__2" => "HOT/샷추가(2개)" 로 바꾸는 작업
        StringBuilder convert = new StringBuilder();
        String[] arrOption = option.split("/");
        for (String opStr : arrOption) {
            String[] oneOption = opStr.split("__");
            Option tmpOption = optionDao.getOptionById(Integer.parseInt(oneOption[0]));

            if (tmpOption.getType().equals("Temperature")) {
                convert.append(tmpOption.getName());
            } else if(tmpOption.getType().equals("Shot")){
                convert.append("/샷추가("+oneOption[1]+"개)");
            } else if(tmpOption.getType().equals("Syrup")){
                convert.append("/시럽추가("+oneOption[1]+"개)");
            } else if(tmpOption.getType().equals("Size")){
                convert.append("/사이즈업");
            }
        }
        return convert.toString();
    }
}
