package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.option.Option;
import com.internship.tmontica_admin.option.OptionDao;
import com.internship.tmontica_admin.order.model.request.OrderStatusReq;
import com.internship.tmontica_admin.order.model.response.*;
import com.internship.tmontica_admin.paging.Pagination;
import com.internship.tmontica_admin.point.Point;
import com.internship.tmontica_admin.point.PointLogType;
import com.internship.tmontica_admin.point.PointService;
import com.internship.tmontica_admin.security.JwtService;
import com.internship.tmontica_admin.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;
    private final OptionDao optionDao;
    private static final double RESERVE_RATE = 10.0;
    private final PointService pointService;
    private final JwtService jwtService;

    // 주문 상태 변경 api(관리자)
    @Transactional
    public void updateOrderStatusApi(OrderStatusReq orderStatusReq){
        // 관리자의 아이디 가져오기
        String userId = JsonUtil.getJsonElementValue(jwtService.getUserInfo("userInfo"),"id");

        // orderId 리스트 가져오기
        List<Integer> orderIds = orderStatusReq.getOrderIds();
        for (int orderId : orderIds) {
            //로그중에 픽업완료인 애의 리스트
            List<OrderStatusLogResp> orderStatusLogList = orderDao.getOrderStatusLogByOrderId(orderId).stream()
                    .filter(OrderStatusLogResp::isPickUp)
                    .collect(Collectors.toList());
            // "픽업완료" 상태로 바뀌면 포인트 적립 (최초 한번)
            if(orderStatusLogList.isEmpty() && orderStatusReq.getStatus().equals(OrderStatusType.PICK_UP.getStatus())){
                Order order = orderDao.getOrderByOrderId(orderId);
                Point point = new Point(order.getUserId(), PointLogType.GET_POINT.getType(),
                        String.valueOf((int)(order.getRealPrice()*(RESERVE_RATE)/100)), "결제 적립금 적립.");
                pointService.updateUserPoint(point);
            }

            // orders 테이블에서 status 수정
            orderDao.updateOrderStatus(orderId, orderStatusReq.getStatus());
            // order_status_log 테이블에도 로그 추가
            OrderStatusLog orderStatusLog = new OrderStatusLog(orderStatusReq.getStatus(), userId, orderId);
            orderDao.addOrderStatusLog(orderStatusLog);
        }
    }


    // 오늘의 상태별 주문 현황 가져오기 api(관리자)
    public OrdersByStatusResp getTodayOrderByStatusApi(String status, int size, int page) {

        List<OrderResp> orderResps = new ArrayList<>();
        // 오늘의 상태별 주문 개수 가져오기
        StatusCountResp statusCountResp = orderDao.getTodayStatusCount();

        List<Order> orders;
        int totalCnt = 0;
        Pagination pagination = new Pagination();

        // DB에서 오늘의 (상태별) 주문현황 가져오기
        if(status.equals(OrderStatusType.ALL.toString())){
            // 상태 문자열이 default "ALL" 인 경우
            totalCnt = orderDao.getTodayOrderCnt(); // 페이징을 위한 전체 데이터 개수
            pagination.pageInfo(page, size, totalCnt); // 페이지네이션 객체 생성

            orders = orderDao.getTodayOrders(pagination.getStartList(), size);
        }else {
            // 상태 문자열을 보내줬을 경우
            totalCnt = orderDao.getTodayOrderCntByStatus(status); // 페이징을 위한 전체 데이터 개수
            pagination.pageInfo(page, size, totalCnt);              // 페이지네이션 객체 생성

            orders = orderDao.getTodayOrderByStatus(status, pagination.getStartList(), size);
        }


        // 디비에서 가져온 리스트 orders -> orderResps 리스트에 매핑
        for(Order order : orders){
            // orderId로 주문 상세 정보 리스트 가져오기
            List<OrderMenusResp> menus = orderDao.getOrderDetailByOrderId(order.getId());

            // menus리스트 안의 옵션 문자열과 이미지url 셋팅 작업
            setMenuOptionAndImgurl(menus);

            OrderResp orderResp = new OrderResp(order.getId(), order.getOrderDate(), order.getPayment(), order.getTotalPrice(),
                                    order.getUsedPoint(), order.getRealPrice(), order.getStatus(), order.getUserId(), menus);
            orderResps.add(orderResp);
        }

        OrdersByStatusResp ordersByStatusResps = new OrdersByStatusResp(pagination,statusCountResp,orderResps); // 반환할 객체

        return ordersByStatusResps;
    }


    // 주문 상세정보 가져오기 api(관리자)
    public OrderDetailResp getOrderDetailApi(int orderId){
        // orderId로 주문 정보 1개 가져오기
        Order order = orderDao.getOrderByOrderId(orderId);
        // orderId로 주문 상세 정보 리스트 가져오기
        List<OrderMenusResp> menus = orderDao.getOrderDetailByOrderId(orderId);

        // menus리스트 안의 옵션 문자열과 이미지url 셋팅 작업
        setMenuOptionAndImgurl(menus);

        // orderId로 주문상태 로그 리스트 가져오기
        List<OrderStatusLogResp> orderStatusLogs = orderDao.getOrderStatusLogByOrderId(orderId);

        OrderDetailResp orderDetailResp = new OrderDetailResp(order.getUserId(), orderId, order.getTotalPrice(),menus, orderStatusLogs);
        return orderDetailResp;
    }


    // 주문 내역 검색 api(관리자)
    public OrderHistoryResp getOrderHistory(String searchType, String searchValue, String startDate, String endDate, int size, int page) {

        List<Order> orders = null;
        int totalCnt = 0;
        Pagination pagination = new Pagination();

        // 파라미터의 유무에 따라서 검색 하기
        if(searchType.equals("") && searchValue.equals("") && startDate.equals("") && endDate.equals("")){
            // (조건 없을때)전체 내역 가져오기
            totalCnt = orderDao.getSearchAllOrderCnt(); // 페이징을 위한 전체 데이터 개수
            pagination.pageInfo(page, size, totalCnt);// 페이지네이션 객체 생성

            orders = orderDao.searchAllOrder(pagination.getStartList(), size);
        }else if(searchType.equals("") && searchValue.equals("") && !startDate.equals("") && !endDate.equals("")){
            // 날짜만 적용
            totalCnt = orderDao.getSearchOrderCntByDate(startDate, endDate);
            pagination.pageInfo(page, size, totalCnt);

            orders = orderDao.searchOrderByDate(startDate, endDate, pagination.getStartList(), size);
        }else if(!searchType.equals("") && !searchValue.equals("") && startDate.equals("") && endDate.equals("")){
            // 검색 조건만 적용 (전체기간)
            totalCnt = orderDao.getSearchOrderCntBySearchValue(OrderSearchType.getBysearchType(searchType), searchValue);
            pagination.pageInfo(page, size, totalCnt);

            orders = orderDao.searchOrderBySearchValue(OrderSearchType.getBysearchType(searchType), searchValue, pagination.getStartList(), size);
        }else {
            // 검색 조건과 날짜 모두 적용
            totalCnt = orderDao.getSearchOrderCnt(OrderSearchType.getBysearchType(searchType), searchValue, startDate, endDate);
            pagination.pageInfo(page, size, totalCnt);

            // 검색조건에 맞는 데이터 가져오기
            orders = orderDao.searchOrder(OrderSearchType.getBysearchType(searchType),searchValue,startDate,endDate,pagination.getStartList(), size);
        }

        List<OrderResp> orderResps = new ArrayList<>();

        // 디비에서 가져온 리스트 orders -> orderResps 리스트에 매핑
        for(Order order : orders){
            // orderId로 주문 상세 정보 리스트 가져오기
            List<OrderMenusResp> menus = orderDao.getOrderDetailByOrderId(order.getId());

            // menus리스트 안의 옵션 문자열과 이미지url 셋팅 작업
            setMenuOptionAndImgurl(menus);

            OrderResp orderResp = new OrderResp(order.getId(), order.getOrderDate(), order.getPayment(), order.getTotalPrice(),
                    order.getUsedPoint(), order.getRealPrice(), order.getStatus(), order.getUserId(), menus);

            orderResps.add(orderResp);
        }

        OrderHistoryResp orderHistoryResp = new OrderHistoryResp(pagination, orderResps);
        return orderHistoryResp;
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


    // menus리스트 안의 옵션 문자열과 이미지url 셋팅 작업
    public void setMenuOptionAndImgurl(List<OrderMenusResp> menus){
        for (OrderMenusResp menu : menus) {
            //메뉴 옵션 "1__1/4__2" => "HOT/샷추가(2개)" 로 바꾸는 작업
            if(!menu.getOption().equals("")){
                String option = menu.getOption();
                String convert = convertOptionStringToCli(option); // 변환할 문자열
                menu.setOption(convert);
            }
            // 이미지 url 셋팅
            menu.setImgUrl("/images/".concat(menu.getImgUrl()));
        }
    }

}
