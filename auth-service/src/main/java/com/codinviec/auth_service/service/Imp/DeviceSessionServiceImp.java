package com.codinviec.auth_service.service.Imp;

import com.codinviec.auth_service.service.DeviceSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DeviceSessionServiceImp implements DeviceSessionService {

    private final RedisTemplate<String, String> redisTemplate;

    @Qualifier("limite_devices")
    private final DefaultRedisScript<String> limitDeviceScript;

    private static final int DEVICE_LIMIT = 5;

    /***
     * Key redis note
     */
    //    user_devices:{userId} = [các giá trị devices]
    private final String keyDevicesId = "user_devices:";

    @Override
    public void registerDevice(String userId, String deviceId,String keyRefreshTokenRedis) {
        String zsetKey = keyDevicesId + userId;
        long now = System.currentTimeMillis();

        redisTemplate.execute(
                limitDeviceScript,
                List.of(zsetKey),
                deviceId,
                String.valueOf(now),
                String.valueOf(DEVICE_LIMIT),
                String.valueOf(userId),
                keyRefreshTokenRedis
        );
    }

    @Override
    public void logoutDevice(String userId, String deviceId) {
        redisTemplate.opsForZSet()
                .remove(keyDevicesId + userId, deviceId);
    }

    @Override
    public void logoutAll(String userId) {
        String zsetKey = keyDevicesId + userId;

        Set<String> devices =
                redisTemplate.opsForZSet().range(zsetKey, 0, -1);

        if (devices != null) {
            devices.forEach(
                    d -> redisTemplate.delete("refresh:" + userId + ":" + d)
            );
        }
        redisTemplate.delete(zsetKey);
    }
}
