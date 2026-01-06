package com.rationsentinel.dto;

import com.rationsentinel.entity.AllocationStatus;
import com.rationsentinel.entity.CommodityType;


import java.time.LocalDateTime;

public class StockAllocationResponseDTO {

    private Long id;
    private Long fpsId;
    private CommodityType commodity;
    private int allocatedQuantity;
    private int allocationMonth;
    private int allocationYear;
    private AllocationStatus status;
    private LocalDateTime createdAt;

    public StockAllocationResponseDTO(
            Long id,
            Long fpsId,
            CommodityType commodity,
            int allocatedQuantity,
            int allocationMonth,
            int allocationYear,
            AllocationStatus status,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.fpsId = fpsId;
        this.commodity = commodity;
        this.allocatedQuantity = allocatedQuantity;
        this.allocationMonth = allocationMonth;
        this.allocationYear = allocationYear;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getFpsId() {
        return fpsId;
    }

    public CommodityType getCommodity() {
        return commodity;
    }

    public int getAllocatedQuantity() {
        return allocatedQuantity;
    }

    public int getAllocationMonth() {
        return allocationMonth;
    }

    public int getAllocationYear() {
        return allocationYear;
    }

    public AllocationStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
