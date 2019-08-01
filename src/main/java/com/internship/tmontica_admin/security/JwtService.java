package com.internship.tmontica_admin.security;

import com.internship.tmontica_admin.user.model.response.UserTokenInfoDTO;

public interface JwtService {

    String getToken(UserTokenInfoDTO userTokenInfoDTO);
    boolean isUsable(String jwt);
    String getUserInfo(String key);
}
