package com.project.codinviec_auth_service.service.Imp;

import com.project.codinviec_auth_service.entity.OutboxEventEntity;
import com.project.codinviec_auth_service.repository.OutboxRepository;
import com.project.codinviec_auth_service.service.OutboxServices;
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
