package com.rationsentinel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "stock_allocations",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"fps_id", "commodity", "allocation_month", "allocation_year"}

        )
)
@Getter
@Setter
public class StockAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔒 Allocation is ALWAYS tied to FPS
    @ManyToOne(optional = false)
    @JoinColumn(name = "fps_id", nullable = false)
    private FairPriceShop fps;


    // 🔒 Controlled commodity values
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommodityType commodity;

    @Column(nullable = false)
    private int quantityAllocated;


    @Column(nullable = false)
    private int allocationMonth;

    @Column(nullable = false)
    private int allocationYear;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllocationStatus status;

    // 🔒 Metadata (not allocation period)
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
