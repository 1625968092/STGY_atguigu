package com.atguigu.lease.common.utils;

import com.atguigu.lease.common.exception.LeaseException;
import com.atguigu.lease.common.result.ResultCodeEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtil {

    //构造key，秘钥已经删除
    public static SecretKey secretKey = Keys.hmacShaKeyFor("123123".getBytes());

    public static String createToken(Long userId,String userName){
        String jwt=Jwts.builder().
                setExpiration(new Date(System.currentTimeMillis()+3600000L*24L)).//设置过期时间  单位毫秒
                setSubject("LOGIN_USER").   //设置主题
                claim("userId",userId).  //声明自定义属性要用claim
                claim("username",userName).
                signWith(secretKey) //构造签名：输入密令key  选择签名算法
                .compact();   //打包
        return jwt;
    }

    //token校验方法
    public static Claims parseToken(String token){

        //如果token为空抛出异常
        if(token==null){
            throw new LeaseException(ResultCodeEnum.ADMIN_LOGIN_AUTH);
        }

        try{
            //使用秘钥创建解析器
            JwtParser parser = Jwts.parserBuilder().
                    setSigningKey(secretKey).build();
            Jws<Claims> claimsJws = parser.parseClaimsJws(token);
            return claimsJws.getBody();

        }catch (ExpiredJwtException e){
            //过期异常
            throw new LeaseException(ResultCodeEnum.TOKEN_EXPIRED);
        }catch (JwtException e){
            //其他异常
            throw new LeaseException(ResultCodeEnum.TOKEN_INVALID);
        }
    }

    public static void main(String[] args) {
        String token = createToken(8L, "17614235521");
        System.out.println(token);
    }
}
