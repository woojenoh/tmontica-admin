package com.internship.tmontica_admin.order;

import com.internship.tmontica_admin.order.model.response.OrderStatusLogResp;
import com.internship.tmontica_admin.order.model.response.Order_MenusResp;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {

    // orderId로 주문상세 정보 가져오기 (메뉴 이름 추가)
    @Select("select A.menu_id, B.name_eng, B.name_ko, A.option, B.img_url, A.quantity, A.price " +
            "from order_details as A inner join menus as B " +
            "   on A.menu_id = B.id " +
            "where A.order_id = #{orderId}")
    List<Order_MenusResp> getOrderDetailByOrderId(int orderId);

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

    // order Status로 주문정보 가져오기
    @Select("select * from orders where status = #{status}")
    List<Order> getOrderByStatus(String status);
}
