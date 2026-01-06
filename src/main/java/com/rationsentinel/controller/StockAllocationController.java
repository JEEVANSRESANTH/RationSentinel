package com.rationsentinel.controller;

import com.rationsentinel.dto.StockAllocationRequest;
import com.rationsentinel.dto.StockAllocationResponseDTO;
import com.rationsentinel.entity.StockAllocation;
import com.rationsentinel.entity.CommodityType;
import com.rationsentinel.service.StockAllocationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/allocations")
public class StockAllocationController {

    private final StockAllocationService stockAllocationService;

    public StockAllocationController(StockAllocationService stockAllocationService) {
        this.stockAllocationService = stockAllocationService;
    }

    // =========================
    // CREATE (UNCHANGED)
    // =========================
    @PostMapping
    public StockAllocation createAllocation(
            @RequestBody StockAllocationRequest request
    ) {
        return stockAllocationService.createAllocation(
                request.getFpsId(),
                request.getCommodity(),
                request.getQuantity(),
                request.getMonth(),
                request.getYear()
        );
    }

    // =========================
    // READ (NEW)
    // =========================
    @GetMapping
    public Page<StockAllocationResponseDTO> getAllocations(
            @RequestParam(required = false) Long fpsId,
            @RequestParam(required = false) Integer allocationMonth,
            @RequestParam(required = false) Integer allocationYear,
            @RequestParam(required = false) CommodityType commodity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        // 🔒 HARDENING (THIS PART WAS MISSING BEFORE)
        if (page < 0) {
            page = 0;
        }

        if (size <= 0 || size > 50) {
            size = 10;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return stockAllocationService.getAllocations(
                fpsId,
                allocationMonth,
                allocationYear,
                commodity,
                pageable
        );
    }
}

