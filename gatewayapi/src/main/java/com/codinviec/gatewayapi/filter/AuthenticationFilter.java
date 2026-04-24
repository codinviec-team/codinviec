package com.codinviec.gatewayapi.filter;


import com.codinviec.gatewayapi.dto.JwtUserDTO;
import com.codinviec.gatewayapi.exception.security.AccessTokenExceptionHandler;
import com.codinviec.gatewayapi.util.CookieHelper;
import com.codinviec.gatewayapi.util.JWTHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    private final CookieHelper  cookieHelper;
    private final JWTHelper jwtHelper;
    /***
     * Key redis note
     */
    //    token:version:{userId} = giá trị version token
    private final String keyVersionRedis = "token:version:";


    /**
     * Public APIs – KHÔNG cần JWT
     */
    private static final List<String> PUBLIC_PATHS = List.of(
            "/api/auth/google",
            "/api/auth/google/callback",
            "/api/auth/login",
            "/api/auth/register"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        String path = exchange.getRequest().getURI().getPath();
        if (PUBLIC_PATHS.stream().anyMatch(path::startsWith)) {
            return chain.filter(exchange);
        }

        String token = cookieHelper.getAccessToken(request).orElse(null);

        // Không có token coi như chưa login
        if (token == null) {
            return chain.filter(exchange);
        }

//        Đã login
        ServerHttpResponse response = exchange.getResponse();
        JwtUserDTO jwtUserDTO = jwtHelper.verifyAccessToken(token, keyVersionRedis, response);
        if (jwtUserDTO != null) {
//                Gắn header lên request cho servives sau
            ServerHttpRequest mutatedRequest = request.mutate()
                    .header("X-User-Id", jwtUserDTO.getUserId())
                    .header("X-User-Roles", jwtUserDTO.getRole())
                    .header("X-User-Device", jwtUserDTO.getDeviceId())
                    .header("X-Token-Verson", String.valueOf(jwtUserDTO.getTokenVersion()))
                    .build();
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
        } else {
            throw new AccessTokenExceptionHandler();
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}