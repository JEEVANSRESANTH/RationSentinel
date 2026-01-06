package com.rationsentinel.repository;

import com.rationsentinel.entity.*;
import com.rationsentinel.entity.AllocationStatus;
import com.rationsentinel.entity.CommodityType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // Existing read helpers (kept as-is)
    List<StockAllocation> findByStatus(AllocationStatus status);
    List<StockAllocation> findByFps_Id(Long fpsId);

    // ✅ NEW: Filtered + paginated read API
    @Query("""
        SELECT a FROM StockAllocation a
        WHERE (:fpsId IS NULL OR a.fps.id = :fpsId)
          AND (:allocationMonth IS NULL OR a.allocationMonth = :allocationMonth)
          AND (:allocationYear IS NULL OR a.allocationYear = :allocationYear)
          AND (:commodity IS NULL OR a.commodity = :commodity)
    """)
    Page<StockAllocation> findAllocationsWithFilters(
            @Param("fpsId") Long fpsId,
            @Param("allocationMonth") Integer allocationMonth,
            @Param("allocationYear") Integer allocationYear,
            @Param("commodity") CommodityType commodity,
            Pageable pageable
    );
}
