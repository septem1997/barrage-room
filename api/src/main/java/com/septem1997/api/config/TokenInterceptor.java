package com.septem1997.api.config;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.naming.ServiceUnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    private JwtConfig jwtConfig ;
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        //开发用
        if (request.getHeader("dev") != null){
            return true;
        }

        // 地址过滤

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())){
            return true;
        }
        String uri = request.getRequestURI();
        if (uri.contains("/user/login")||uri.contains("/user/signup")){
            return true;
        }
        // Token 验证
        String token = request.getHeader(JwtConfig.HEADER);
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(JwtConfig.HEADER);
        }
        if(StringUtils.isEmpty(token)){
            throw new ServiceUnavailableException(JwtConfig.HEADER+ "不能为空");
        }
        Claims claims = jwtConfig.getTokenClaim(token);
        if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
            throw new ServiceUnavailableException(JwtConfig.HEADER + "失效，请重新登录");
        }
        //设置 identityId 用户身份ID
        request.setAttribute("identityId", claims.getSubject());
        return true;
    }
}