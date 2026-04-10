package com.codinviec.auth_service.service;

public interface DeviceSessionService {
    void registerDevice(String userId, String deviceId,String keyRefreshTokenRedis);
    void logoutDevice(String userId, String deviceId);
    void logoutAll(String userId);
}
