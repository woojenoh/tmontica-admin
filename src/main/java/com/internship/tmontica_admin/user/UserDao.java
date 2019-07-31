package com.internship.tmontica_admin.user;

import org.apache.ibatis.annotations.*;


@Mapper
public interface UserDao {

    @Select("SELECT * FROM users WHERE id = #{id}")
    User getUserByUserId(String id);
}
