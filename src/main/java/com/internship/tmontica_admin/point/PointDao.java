package com.internship.tmontica_admin.point;

import com.internship.tmontica_admin.order.Order;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PointDao {

    @Insert("INSERT INTO points (user_id ,type ,amount, description) " +
            "VALUES(#{userId}, #{type}, #{amount}, #{description})")
    int addPoint(Point point);
    @Select("SELECT * FROM points where user_id = #{userId")
    List<Point> getAllPointLogByUserId(String userId);
    @Delete("DELETE FROM points WHERE user_id = #{userId}")
    int withdrawPointLogByUserId(String userId); // 특정 유저의 변동내역 삭제


    // 포인트 환불내역 포인트 테이블에 일괄 추가(스케줄러)
    @Insert("<script>" +
            "   <foreach collection='orders' item='order'>" +
            "       insert into points (user_id, type, amount, description)" +
            "       values(#{order.userId}, #{type}, #{order.usedPoint}, #{description}); " +
            "   </foreach>" +
            "</script>")
    void addPointList(@Param("orders")List<Order> orders, String type, String description);

}
