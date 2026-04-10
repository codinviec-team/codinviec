package com.codinviec.auth_service.util;

import com.codinviec.auth_service.dto.JwtUserDTO;
import com.codinviec.auth_service.exception.security.AccessTokenExceptionHandler;
import com.codinviec.auth_service.exception.security.RefreshTokenExceptionHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class JWTHepler  {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration.access}")
    private long expirationAccess;

    @Value("${jwt.expiration.refresh}")
    private long expirationRefresh;

    private final CookieHelper cookieHelper;

    @Qualifier("redisTemplateDb0")
    private final RedisTemplate<String, String> redisTemplateDb;

    private SecretKey keyParse;


    /***
     * Key redis note
     */
    //    user_devices:{userId} = [các giá trị devices]
    private final String keyDevicesId = "user_devices:";

    @PostConstruct
    public void init() {
        this.keyParse = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public void revokeAllTokens(String userId, String deviceId, String keyRefreshTokenRedis, HttpServletResponse response) {
//        xóa redis của refresh token
        redisTemplateDb.delete(keyRefreshTokenRedis+userId+":"+deviceId);
        cookieHelper.clearRefreshTokenCookie(response);
        cookieHelper.clearAccessTokenCookie(response);

    }


    public String createAccessToken(String roles,int tokenVersion, String userId,String devicesId) {
        try{
            long now = System.currentTimeMillis();
            long exp = now + expirationAccess;
            Date expirationDate = new Date(exp);
            String accessTokenReuslt = Jwts.builder()
                    .setSubject(roles)
                    .setIssuer(userId)
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .claim("tokenVersion", tokenVersion)
                    .claim("type", "access")
                    .claim("device", devicesId)
                    .signWith(keyParse)
                    .compact();
            return accessTokenReuslt;
        }catch (Exception e) {
            throw new AccessTokenExceptionHandler("Không thể tạo AccessToken!");
        }
    }

    public String createRefreshToken(String roles, String userId, String keyRefreshTokenRedis, int tokenVersion, String devicesId) {
        try{
            long now = System.currentTimeMillis();
            long exp = now + expirationRefresh;
            Date expirationDate = new Date(exp);

            String refreshToken = Jwts.builder()
                    .setSubject(roles)
                    .setIssuer(userId)
                    .setIssuedAt(new Date())
                    .setExpiration(expirationDate)
                    .claim("tokenVersion", tokenVersion)
                    .claim("type", "refresh")
                    .claim("device", devicesId)
                    .signWith(keyParse)
                    .compact();

            System.out.println("key : " +  keyRefreshTokenRedis+userId + ":" + devicesId);
            System.out.println("refreshToken : " +  refreshToken);
            System.out.println("expirationRefresh : " +  expirationRefresh);
//            đẩy lên redis
            redisTemplateDb.opsForValue().set(keyRefreshTokenRedis+userId + ":" + devicesId, refreshToken,Duration.ofMillis(exp - now));
            return  refreshToken;
        }catch (Exception e) {
            throw new RefreshTokenExceptionHandler("Không thể tạo RefreshToken");
        }
    }

    public JwtUserDTO verifyRefreshToken(String refreshToken,String keyRefreshTokenRedis,String keyVersionRedis, HttpServletResponse response) {
        try{
            Jws<Claims> tokenValidate = Jwts.parser()
                    .verifyWith(keyParse)
                    .build()
                    .parseSignedClaims(refreshToken);

            Claims claims = tokenValidate.getBody();

            String userId = claims.getIssuer();
            String role = claims.getSubject();

//          Kiểm tra type
            String type = claims.get("type", String.class);
            if (!"refresh".equals(type)) {
                throw new JwtException("Type is not valid!");
            }


//          Kiểm tra device trong token có khớp không
            String deviceId = claims.get("device", String.class);
            if (!checkDevicesIDToken(userId, deviceId)) {
                throw new JwtException("Devices is not valid");
            }

//          Kiểm tra versionToken
            Integer tokenVersionInToken = claims.get("tokenVersion", Integer.class);
            Integer tokenVersionInDb = getTokenVersion(userId, keyVersionRedis);
            if (!tokenVersionInToken.equals(tokenVersionInDb)) {
                throw new JwtException("Token revoked");
            }

//            Kiểm tra refreshh token có trên redis không
            String refreshTokenRedis = getRefreshToken(userId,keyRefreshTokenRedis,deviceId);
            if(refreshTokenRedis == null){
                throw new RefreshTokenExceptionHandler("Token hết hạn!");
            }

            return JwtUserDTO.builder()
                    .userId(userId)
                    .role(role)
                    .tokenVersion(tokenVersionInToken)
                    .deviceId(deviceId)
                    .build();
        }
        catch (ExpiredJwtException e){
            String userId = e.getClaims().get("userId").toString();
            String deviceId = e.getClaims().get("deviceId").toString();
            revokeAllTokens(userId,deviceId, keyRefreshTokenRedis,response);
            throw new RefreshTokenExceptionHandler("Token đã hết hạn");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getRefreshToken(String userId, String keyRefreshTokenRedis,String devicesId) {
        try{
            if(redisTemplateDb.hasKey(keyRefreshTokenRedis+userId+":"+devicesId )){
                return redisTemplateDb.opsForValue().get(keyRefreshTokenRedis+userId+":"+devicesId );
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public Integer getTokenVersion(String userId,String keyVersionRedis) {
        try{
            if(redisTemplateDb.hasKey(keyVersionRedis+userId)){
                return Integer.parseInt(Objects.requireNonNull(redisTemplateDb.opsForValue().get(keyVersionRedis+userId)));
            }
            return null;
        }catch (Exception e){
            return null;
        }
    }

    public boolean checkDevicesIDToken(String userId, String devicesId) {
        try {
            Double score = redisTemplateDb
                    .opsForZSet()
                    .score(keyDevicesId + userId, devicesId);

            return score != null;
        } catch (Exception e) {
            return false;
        }
    }
}
