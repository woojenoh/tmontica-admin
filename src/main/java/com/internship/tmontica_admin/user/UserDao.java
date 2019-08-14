package com.internship.tmontica_admin.user;

import com.internship.tmontica_admin.order.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface UserDao {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserByUserId(String id);
    @Update("UPDATE users SET point = #{point} where id = #{id}")
    int updateUserPoint(int point, String id);
    @Select("SELECT point FROM users WHERE id = #{id}")
    int getUserPointByUserId(String id);

    // 여러개 주문에 대해서 사용했던 포인트 일괄 환불처리 (스케줄러)
    @Update("<script>" +
            "   <foreach collection='orders' item='order'> " +
            "       update users set point = point + #{order.usedPoint} where id = #{order.userId};" +
            "   </foreach>" +
            "</script>")
    void updateUserPointList(@Param("orders")List<Order> orders);
}
