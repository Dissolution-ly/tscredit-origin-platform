package com.tscredit.origin.user.utils;


import com.tscredit.common.response.ErrorMessage;
import com.tscredit.common.response.LogicException;
import com.tscredit.platform.redis.config.RedisUtil;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;


public class JwtUtil {

    /**
     * jwt加密
     *
     * @param param 参数(参数不是完全加密，可能被反编译，不要存储机密数据)
     * @param key   键
     * @param salt  键2
     * @param exp   过期时间，如果为null则 永不过期
     * @return token
     */
    public static String encode(Map<String, Object> param, String key, String salt, Date exp) {
        if (salt != null) {
            key += salt;
        }

        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, key)
                .setClaims(param)
                .setIssuedAt(new Date());  //签发时间

        if (exp != null) { //过期时间
            jwtBuilder = jwtBuilder.setExpiration(exp);
        }

        return jwtBuilder.compact();
    }


    /**
     * 解密
     *
     * @param token 令牌
     * @param key   键
     * @param salt  键2
     * @return map数据
     */
    public static Map<String, Object> decode(String token, String key, String salt) {
        if (salt != null) {
            key += salt;
        }

        try {
            return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * 从请求头 或 请求参数获取 token, 并解密   (仅解密token,不校验 redis 可用性)
     */
    public static Map<String, Object> decodeToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            throw LogicException.errorMessage(ErrorMessage.USER_NO_TOKEN);
        }

        // TODO 解密数据
//        Map<String, Object> decode = JwtUtil.decode(token, Constant.JWT_KEY, getIpAddr(request));
        Map<String, Object> decode = null;
        if (MapUtils.isEmpty(decode)) {
            throw LogicException.errorMessage(ErrorMessage.USER_TOKEN_LOSE);
        }
        return decode;
    }


    /**
     * 从请求头 或 请求参数获取 token, 并解密(解密后验证redis可用性) ,若redis可用,返回解密信息   (仅解密token,不校验 redis 可用性)
     */
    public static Map<String, Object> token(HttpServletRequest request) {
        // 解密 token
        Map<String, Object> token = decodeToken(request);

        // 根据userId 到 redis 中查找信息
        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        if (redisUtil.get(getRedisKeyByToken(token)) == null) {
//            Set<String> keys = redisUtil.getKeysByPattern(Constant.REDIS_USER + MapUtils.getString(token, "userId") + ":*");
//            if (CollectionUtils.isEmpty(keys)) {
            throw LogicException.errorMessage(ErrorMessage.USER_TOKEN_LOSE);
//            } else {
//                throw LogicException.errorMessage(ErrorMessage.USER_DIFFERENT_PLACE);
//            }
        }

        return token;
    }

    /**
     * 根据 JWT用户信息 ，生成用户 redis Key
     */
    public static String getRedisKeyByToken(Map<String, Object> token) {
        String uuid = MapUtils.getString(token, "uuid");
        String userId = MapUtils.getString(token, "userId");
//        return Constant.OBJECT_NAME + Constant.REDIS_USER + userId + ":" + uuid;
        return null;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值
     *
     * @return ip
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        //System.out.println("x-forwarded-for ip: " + ip);
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
            //System.out.println("Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            //System.out.println("WL-Proxy-Client-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
            //System.out.println("HTTP_CLIENT_IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            //System.out.println("HTTP_X_FORWARDED_FOR ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
            //System.out.println("X-Real-IP ip: " + ip);
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            //System.out.println("getRemoteAddr ip: " + ip);
        }
        //System.out.println("获取客户端ip: " + ip);
        return ip;
    }
}
