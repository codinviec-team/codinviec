package com.project.codinviec_auth_service.service;

import com.project.codinviec_auth_service.entity.OutboxEventEntity;

public interface OutboxServices {
    void addEventToOutBox(OutboxEventEntity outboxEventEntity);
}
