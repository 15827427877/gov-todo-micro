package com.gov.common.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/**
 * JWT工具类
 * 用于生成和验证JWT令牌
 * 
 * @author chengbin
 * @since 2026-04-19
 */
public class JwtUtils {

    // 密钥，实际项目中应该从配置文件读取
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor("gov-todo-system-secret-key-2026-04-20".getBytes());

    // 过期时间：24小时
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    // 签发者
    private static final String ISSUER = "gov-todo-system";

    /**
     * 生成JWT令牌
     * @param userId 用户ID
     * @param claims 自定义声明
     * @return JWT令牌
     */
    public static String generateToken(String userId, Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(userId)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .addClaims(claims)
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 生成JWT令牌（无自定义声明）
     * @param userId 用户ID
     * @return JWT令牌
     */
    public static String generateToken(String userId) {
        return generateToken(userId, null);
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 声明
     */
    public static Claims parseToken(String token) {
        if (!StringUtils.hasText(token)) {
            return null;
        }

        try {
            return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            LogUtils.error(JwtUtils.class, "解析JWT令牌失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从令牌中获取用户ID
     * @param token JWT令牌
     * @return 用户ID
     */
    public static String getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.getSubject() : null;
    }

    /**
     * 验证令牌是否有效
     * @param token JWT令牌
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }
            // 检查令牌是否过期
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 刷新令牌
     * @param token 旧令牌
     * @return 新令牌
     */
    public static String refreshToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        // 移除过期时间
        claims.remove("exp");
        claims.remove("iat");
        // 生成新令牌
        return generateToken(claims.getSubject(), claims);
    }
}
