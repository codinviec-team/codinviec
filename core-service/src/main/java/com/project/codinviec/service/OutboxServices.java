package com.project.codinviec.service;

import com.project.codinviec.entity.auth.OutboxEventEntity;

public interface OutboxServices {
    void addEventToOutBox(OutboxEventEntity outboxEventEntity);
}