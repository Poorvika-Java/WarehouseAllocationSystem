package com.warehouse.entity;

import java.time.LocalDateTime;
import com.warehouse.enums.AllocationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "allocation_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllocationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "allocation_id")
    private Allocation allocation;

    @Enumerated(EnumType.STRING)
    private AllocationStatus status;

    private String remarks;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}