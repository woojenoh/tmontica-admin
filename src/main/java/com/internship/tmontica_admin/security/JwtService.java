package com.internship.tmontica_admin.security;

import com.internship.tmontica_admin.user.User;

public interface JwtService {

    String getToken(User user);
    boolean isUsable(String jwt);
    String getUserInfo(String key);
    boolean isAdmin(String jwt);
}
