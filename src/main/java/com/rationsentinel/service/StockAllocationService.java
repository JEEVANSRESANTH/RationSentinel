package com.rationsentinel.service;

import com.rationsentinel.dto.StockAllocationResponseDTO;
import com.rationsentinel.entity.*;
import com.rationsentinel.entity.CommodityType;

import com.rationsentinel.repository.FairPriceShopRepository;
import com.rationsentinel.repository.StockAllocationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StockAllocationService {

    private final StockAllocationRepository stockAllocationRepository;
    private final FairPriceShopRepository fairPriceShopRepository;

    public StockAllocationService(StockAllocationRepository stockAllocationRepository,
                                  FairPriceShopRepository fairPriceShopRepository) {
        this.stockAllocationRepository = stockAllocationRepository;
        this.fairPriceShopRepository = fairPriceShopRepository;
    }

    // =========================
    // CREATE ALLOCATION (UNCHANGED)
    // =========================
    public StockAllocation createAllocation(Long fpsId,
                                            CommodityType commodity,
                                            int quantity,
                                            int month,
                                            int year) {

        FairPriceShop fps = fairPriceShopRepository.findById(fpsId)
                .orElseThrow(() -> new IllegalArgumentException("FPS not found"));

        if (quantity <= 0) {
            throw new IllegalArgumentException("Allocated quantity must be greater than zero");
        }

        boolean alreadyExists =
                stockAllocationRepository.existsByFpsAndCommodityAndAllocationMonthAndAllocationYear(
                        fps, commodity, month, year
                );

        if (alreadyExists) {
            throw new IllegalStateException(
                    "Allocation already exists for this FPS, commodity, and period"
            );
        }
        // Month validation
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Allocation month must be between 1 and 12");
        }


        if (year < 2020 || year > 2100) {
            throw new IllegalArgumentException("Allocation year is invalid");
        }

        if (commodity == null) {
            throw new IllegalArgumentException("Commodity must be provided");
        }


        StockAllocation allocation = new StockAllocation();
        allocation.setFps(fps);
        allocation.setCommodity(commodity);
        allocation.setQuantityAllocated(quantity);
        allocation.setAllocationMonth(month);
        allocation.setAllocationYear(year);
        allocation.setStatus(AllocationStatus.CREATED);
        allocation.setCreatedAt(LocalDateTime.now());

        return stockAllocationRepository.save(allocation);
    }

    // =========================
    // READ ALLOCATIONS (NEW)
    // =========================
    public Page<StockAllocationResponseDTO> getAllocations(
            Long fpsId,
            Integer allocationMonth,
            Integer allocationYear,
            CommodityType commodity,
            Pageable pageable
    ) {
        return stockAllocationRepository
                .findAllocationsWithFilters(
                        fpsId,
                        allocationMonth,
                        allocationYear,
                        commodity,
                        pageable
                )
                .map(a -> new StockAllocationResponseDTO(
                        a.getId(),
                        a.getFps().getId(),
                        a.getCommodity(),
                        a.getQuantityAllocated(),
                        a.getAllocationMonth(),
                        a.getAllocationYear(),
                        a.getStatus(),
                        a.getCreatedAt()
                ));
    }
}
