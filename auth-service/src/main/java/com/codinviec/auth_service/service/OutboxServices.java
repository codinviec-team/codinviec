package com.codinviec.auth_service.service;

import com.codinviec.auth_service.entity.OutboxEventEntity;

public interface OutboxServices {
    void addEventToOutBox(OutboxEventEntity outboxEventEntity);
}
