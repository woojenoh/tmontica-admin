package com.internship.tmontica_admin.config;


import com.internship.tmontica_admin.security.JwtInterceptor;
import com.internship.tmontica_admin.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    @Value("${menu.imagepath}")
    private String location;

    private final JwtService jwtService;
    private static final String[] EXCLUDE_PATH = {
            "/webjars/springfox-swagger-ui/**" , "/", "/csrf",
            "/api/users/signin", "/swagger*/**", "/images/**"
            , "/error/**", "/**/*.html", "/**/*.json", "/static/**", "/signin"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 모든 uri에 대해 http://localhost:18080, http://localhost:8180 도메인은 접근을 허용한다.
        registry.addMapping("/**")
                .allowedOrigins("*") //http://localhost:3000
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor(jwtService))
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATH);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:" + location);

    }
}