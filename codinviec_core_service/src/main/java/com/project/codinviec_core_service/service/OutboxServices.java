package com.project.codinviec_core_service.service;

import com.project.codinviec_core_service.entity.auth.OutboxEventEntity;

public interface OutboxServices {
    void addEventToOutBox(OutboxEventEntity outboxEventEntity);
}