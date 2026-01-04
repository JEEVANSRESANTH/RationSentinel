package com.rationsentinel.controller;

import com.rationsentinel.dto.StockAllocationRequest;
import com.rationsentinel.entity.StockAllocation;
import com.rationsentinel.service.StockAllocationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/allocations")
public class StockAllocationController {

    private final StockAllocationService stockAllocationService;

    public StockAllocationController(StockAllocationService stockAllocationService) {
        this.stockAllocationService = stockAllocationService;
    }

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
}
