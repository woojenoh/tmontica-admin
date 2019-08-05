package com.internship.tmontica_admin.security;

import com.internship.tmontica_admin.security.exception.UnauthorizedException;
import com.internship.tmontica_admin.util.UserConfigValueName;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        final String token = request.getHeader(UserConfigValueName.JWT_TOKEN_HEADER_KEY);

        if(token != null && jwtService.isUsable(token)){
            return true;
        }

        throw new UnauthorizedException();

    }
}
