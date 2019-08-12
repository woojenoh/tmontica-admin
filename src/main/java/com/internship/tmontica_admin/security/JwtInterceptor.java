package com.internship.tmontica_admin.security;

import com.internship.tmontica_admin.security.exception.UnauthorizedException;
import com.internship.tmontica_admin.util.UserConfigValueName;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

        System.out.println(request.getRequestURL().toString());

        if(CorsUtils.isPreFlightRequest(request)){
            response.setStatus(response.SC_OK);
            return true;
        }

        final String token = request.getHeader(UserConfigValueName.JWT_TOKEN_HEADER_KEY);

        if(token != null && jwtService.isUsable(token) && jwtService.isAdmin(token)){
            return true;
        }

        throw new UnauthorizedException();

    }
}
