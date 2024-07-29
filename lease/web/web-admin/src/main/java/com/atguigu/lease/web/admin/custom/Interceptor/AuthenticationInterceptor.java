package com.atguigu.lease.web.admin.custom.Interceptor;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.login.LoginUser;
import com.atguigu.lease.common.login.LoginUserHolder;
import com.atguigu.lease.common.result.ResultCodeEnum;
import com.atguigu.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
//token校验拦截器
public class AuthenticationInterceptor implements HandlerInterceptor {


    //ture 放行  false到此为止
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //请求头中拿token
        String token = request.getHeader("access-token");

        Claims claims = JwtUtil.parseToken(token);
        Long userId = claims.get("userId", Long.class);
        String userName = claims.get("userName", String.class);
        LoginUser loginUser = new LoginUser(userId, userName);
        //拦截器解析token的信息，存入线程本地变量当中，之后的controller，Service，Mapper都可以直接获取用户信息
        LoginUserHolder.setLoginUser(loginUser);
        return true;
    }

    //请求处理完之后要及时销毁线程本地变量中的用户信息
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
