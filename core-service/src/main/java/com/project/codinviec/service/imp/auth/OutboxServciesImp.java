package com.project.codinviec.service.imp.auth;

import com.project.codinviec.entity.auth.OutboxEventEntity;
import com.project.codinviec.repository.OutboxRepository;
import com.project.codinviec.service.OutboxServices;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxServciesImp implements OutboxServices {
    private final OutboxRepository outboxRepository;

    @Override
    public void addEventToOutBox(OutboxEventEntity outboxEventEntity) {
        outboxRepository.save(outboxEventEntity);
    }
}
