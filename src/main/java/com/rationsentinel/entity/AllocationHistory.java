package com.rationsentinel.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class AllocationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long allocationId;

    @Enumerated(EnumType.STRING)
    private AllocationStatus oldStatus;

    @Enumerated(EnumType.STRING)
    private AllocationStatus newStatus;

    private String changedByRole;

    private LocalDateTime changedAt;

    public AllocationHistory(Long allocationId,
                             AllocationStatus oldStatus,
                             AllocationStatus newStatus,
                             String changedByRole) {
        this.allocationId = allocationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedByRole = changedByRole;
        this.changedAt = LocalDateTime.now();
    }

    public AllocationHistory() {}
}
