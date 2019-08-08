package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.order.model.response.OrderStatusLogResp;
import com.internship.tmontica_admin.order.model.response.OrderMenusResp;
import com.internship.tmontica_admin.order.model.response.StatusCountResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    // orderId로 주문상세 정보 가져오기 (메뉴 이름 추가)
    @Select("select A.menu_id, B.name_eng, B.name_ko, A.option, B.img_url, A.quantity, A.price " +
            "from order_details as A inner join menus as B " +
            "   on A.menu_id = B.id " +
            "where A.order_id = #{orderId}")
    List<OrderMenusResp> getOrderDetailByOrderId(int orderId);

    // orderId로 주문 정보 가져오기
    @Select("select * from orders where id = #{orderId}")
    Order getOrderByOrderId(int orderId);

    // order의 주문 상태 변경
    @Update("update orders set status=#{status} where id=#{orderId}")
    void updateOrderStatus(int orderId, String status);

    // order_status_log 추가
    @Insert("insert into order_status_logs " +
            "values(0, #{status}, #{editorId}, #{orderId}, sysdate())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int addOrderStatusLog(OrderStatusLog orderStatusLog);

    // orderId로 order_status_log 내역 가져오기
    @Select("select status, editor_id, modified_date from order_status_logs where order_id=#{orderId}")
    List<OrderStatusLogResp> getOrderStatusLogByOrderId(int orderId);

    // order Status로 오늘의 주문현황 개수 가져오기
    @Select("select count(*) from orders where status = #{status} and order_date > curdate()")
    int getTodayOrderCntByStatus(String status);

    // order Status로 오늘의 주문현황 가져오기(페이징)
    @Select("select * from orders where status = #{status} and order_date > curdate() " +
            "limit #{startList}, #{size}")
    List<Order> getTodayOrderByStatus(String status, int startList, int size);

    // 오늘의 order 정보 전체 개수 가져오기
    @Select("select count(*) from orders where order_date > curdate()")
    int getTodayOrderCnt();

    // 오늘의 order 정보 가져오기(페이징)
    @Select("select * from orders where order_date > curdate() " +
            "limit #{startList}, #{size}")
    List<Order> getTodayOrders(int startList, int size);


    // 오늘의 order 상태별 개수 가져오기
    @Select("select count(if(status=\"미결제\", status, null)) beforePayment, count(if(status=\"결제완료\", status, null)) afterPayment, " +
            "       count(if(status=\"준비중\", status, null)) inProduction,count(if(status=\"준비완료\", status, null)) ready, " +
            "        count(if(status=\"픽업완료\", status, null)) pickUp,count(if(status=\"주문취소\", status, null)) cancel " +
            "from orders " +
            "where order_date > curdate()")
    StatusCountResp getTodayStatusCount();

    // 검색 조건과 날짜가 적용된 주문내역 전체 개수 가져오기
    @Select("select count(*) from orders " +
            "where ${searchType} like '%${searchValue}%' " +
            "   and order_date between date(#{startDate}) and date(#{endDate})+1")
    int getSearchOrderCnt(String searchType, String searchValue, String startDate, String endDate);

    // 검색 조건과 날짜가 적용된 주문내역 검색하기(페이징)
    @Select("select * from orders " +
            "where ${searchType} like '%${searchValue}%' " +
            "   and order_date between date(#{startDate}) and date(#{endDate})+1 " +
            "order by order_date desc " +
            "limit #{startList}, #{size}")
    List<Order> searchOrder(String searchType, String searchValue, String startDate, String endDate, int startList, int size);

    // 날짜 적용된 주문 내역 개수 가져오기
    @Select("select count(*) from orders " +
            "where order_date between date(#{startDate}) and date(#{endDate})+1 ")
    int getSearchOrderCntByDate(String startDate, String endDate);

    // 날짜 적용된 주문 내역 가져오기 (페이징)
    @Select("select * from orders " +
            "where order_date between date(#{startDate}) and date(#{endDate})+1 " +
            "order by order_date desc " +
            "limit #{startList}, #{size}")
    List<Order> searchOrderByDate(String startDate, String endDate, int startList, int size);

    // 검색조건만 적용된 주문내역 개수 가져오기
    @Select("select count(*) from orders " +
            "where ${searchType} like '%${searchValue}%' ")
    int getSearchOrderCntBySearchValue(String searchType, String searchValue);

    // 검색조건만 적용된 주문내역 가져오기 (페이징)
    @Select("select * from orders " +
            "where ${searchType} like '%${searchValue}%' " +
            "order by order_date desc " +
            "limit #{startList}, #{size}")
    List<Order> searchOrderBySearchValue(String searchType, String searchValue, int startList, int size);

    // 전체 주문 내역 개수 가져오기
    @Select("select count(*) from orders ")
    int getSearchAllOrderCnt();

    // 전체 주문 내역 가져오기 (페이징)
    @Select("select * from orders " +
            "order by order_date desc " +
            "limit #{startList}, #{size}")
    List<Order> searchAllOrder(int startList, int size);

    // 미결제 상태인 주문 가져오기 (스케줄러)
    @Select("select * from orders where status=\"미결제\"")
    List<Order> getBeforePaymentOrders();
}
