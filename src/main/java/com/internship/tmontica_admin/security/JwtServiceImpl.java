package com.internship.tmontica_admin.security;

import com.internship.tmontica_admin.security.exception.UnauthorizedException;
import com.internship.tmontica_admin.user.AdminRole;
import com.internship.tmontica_admin.user.model.response.UserTokenInfoDTO;
import com.internship.tmontica_admin.util.JsonUtil;
import com.internship.tmontica_admin.util.UserConfigValueName;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{

    private static final Logger log = LoggerFactory.getLogger(JwtServiceImpl.class);
    private static final Long TOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24L; // 1day
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static final String SALT = "TMONTICA";
    private static final byte[] KEY = SALT.getBytes(StandardCharsets.UTF_8);

    @Override
    public String getToken(UserTokenInfoDTO userTokenInfoDTO) {

        System.out.println(userTokenInfoDTO.toJson());
        return Jwts.builder()
                .setSubject(userTokenInfoDTO.getId())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("regDate", System.currentTimeMillis())
                .claim("userInfo", userTokenInfoDTO.toJson())
                .signWith(signatureAlgorithm, KEY)
                .compact();
    }

    @Override
    public boolean isUsable(String jwt) {
        try{
            Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt);
            return true;
        } catch (JwtException e) {
            log.info("JwtException  : " + e.getMessage());
            throw new UnauthorizedException();
        }
    }

    @Override
    public boolean isAdmin(String jwt){

        String tokenRoleInfo = JsonUtil.getJsonElementValue(Jwts.parser().setSigningKey(KEY).parseClaimsJws(jwt).getBody().get("userInfo", String.class),"role");
        for(AdminRole adminRole : AdminRole.values()){
            if(adminRole.getRole().equals(tokenRoleInfo)){
                return true;
            }
        }

        return false;
    }
    public String getUserInfo(String key){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String jwtToken = request.getHeader(UserConfigValueName.JWT_TOKEN_HEADER_KEY);

        Jws<Claims> jws;

        try{
            jws = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(jwtToken);
        } catch (Exception e) {
            throw new UnauthorizedException();
        }

        return jws.getBody().get(key, String.class);
    }

}