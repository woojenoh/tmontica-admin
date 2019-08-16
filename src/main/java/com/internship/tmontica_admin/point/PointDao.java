package com.internship.tmontica_admin.point;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PointDao {

    @Insert("INSERT INTO points (user_id ,type ,amount, description) " +
            "VALUES(#{userId}, #{type}, #{amount}, #{description})")
    int addPoint(Point point);
    @Select("SELECT user_id, id, type, date, amount, description FROM points where user_id = #{userId")
    List<Point> getAllPointLogByUserId(String userId);
    @Delete("DELETE FROM points WHERE user_id = #{userId}")
    int withdrawPointLogByUserId(String userId); // 특정 유저의 변동내역 삭제
}
