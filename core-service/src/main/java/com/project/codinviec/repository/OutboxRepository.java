package com.project.codinviec.repository;

import com.project.codinviec.entity.auth.OutboxEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxEventEntity, String> {
    // Repository query
    @Query("SELECT e FROM OutboxEventEntity e WHERE " +
            "(e.status = 'PENDING') OR " +
            "(e.status = 'FAILED' AND e.nextRetryAt <= :now)")
    List<OutboxEventEntity> findPendingAndFailedEvents(@Param("now") LocalDateTime now);
}

