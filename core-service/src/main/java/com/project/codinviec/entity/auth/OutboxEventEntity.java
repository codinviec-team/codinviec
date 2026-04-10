package com.project.codinviec.entity.auth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outbox_events")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OutboxEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String eventType;
    private String payload;
    private String status;
    private LocalDateTime createdDate;
    private LocalDateTime publishedAt;
    private int retryCount;
    private LocalDateTime nextRetryAt;
}
