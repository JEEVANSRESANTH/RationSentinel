package com.rationsentinel.repository;

import com.rationsentinel.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockAllocationRepository
        extends JpaRepository<StockAllocation, Long> {

    // 🔒 Critical: prevents duplicate allocation for same period
    boolean existsByFpsAndCommodityAndAllocationMonthAndAllocationYear(
            FairPriceShop fps,
            CommodityType commodity,
            int allocationMonth,
            int allocationYear
    );

    // (Read APIs — to be used later)
    List<StockAllocation> findByStatus(AllocationStatus status);

    List<StockAllocation> findByFps_Id(Long fpsId);
}
