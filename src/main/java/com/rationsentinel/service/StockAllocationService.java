package com.rationsentinel.service;

import com.rationsentinel.entity.*;
import com.rationsentinel.repository.FairPriceShopRepository;
import com.rationsentinel.repository.StockAllocationRepository;
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

    public StockAllocation createAllocation(Long fpsId,
                                            CommodityType commodity,
                                            int quantity,
                                            int month,
                                            int year) {

        // 1️⃣ Validate FPS existence
        FairPriceShop fps = fairPriceShopRepository.findById(fpsId)
                .orElseThrow(() -> new IllegalArgumentException("FPS not found"));

        // 2️⃣ Quantity must be positive
        if (quantity <= 0) {
            throw new IllegalArgumentException("Allocated quantity must be greater than zero");
        }

        // 3️⃣ Prevent duplicate allocation (CRITICAL)
        boolean alreadyExists =
                stockAllocationRepository.existsByFpsAndCommodityAndAllocationMonthAndAllocationYear(
                        fps, commodity, month, year
                );

        if (alreadyExists) {
            throw new IllegalStateException(
                    "Allocation already exists for this FPS, commodity, and period"
            );
        }

        // 4️⃣ Create allocation decision record
        StockAllocation allocation = new StockAllocation();
        allocation.setFps(fps);
        allocation.setCommodity(commodity);
        allocation.setQuantityAllocated(quantity);
        allocation.setAllocationMonth(month);
        allocation.setAllocationYear(year);
        allocation.setStatus(AllocationStatus.CREATED);
        allocation.setCreatedAt(LocalDateTime.now());

        // 5️⃣ Persist (single source of truth)
        return stockAllocationRepository.save(allocation);
    }
}
