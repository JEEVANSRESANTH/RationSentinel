package com.rationsentinel.service;

import com.rationsentinel.dto.StockAllocationResponseDTO;
import com.rationsentinel.entity.*;
import com.rationsentinel.repository.FairPriceShopRepository;
import com.rationsentinel.repository.StockAllocationRepository;
import com.rationsentinel.repository.AllocationHistoryRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StockAllocationService {

    private final StockAllocationRepository stockAllocationRepository;
    private final FairPriceShopRepository fairPriceShopRepository;
    private final AllocationHistoryRepository historyRepo;

    public StockAllocationService(StockAllocationRepository stockAllocationRepository,
                                  FairPriceShopRepository fairPriceShopRepository,
                                  AllocationHistoryRepository historyRepo) {
        this.stockAllocationRepository = stockAllocationRepository;
        this.fairPriceShopRepository = fairPriceShopRepository;
        this.historyRepo = historyRepo;
    }

    // =========================
    // CREATE ALLOCATION
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
            throw new IllegalStateException("Allocation already exists for this FPS, commodity, and period");
        }

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
    // READ ALLOCATIONS
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

    // =========================
    // STATUS TRANSITION RULES
    // =========================
    private boolean isValidTransition(AllocationStatus current, AllocationStatus next) {
        return switch (current) {
            case CREATED -> (next == AllocationStatus.APPROVED || next == AllocationStatus.CANCELLED);
            case APPROVED -> (next == AllocationStatus.DISPATCHED || next == AllocationStatus.CANCELLED);
            case DISPATCHED -> (next == AllocationStatus.DELIVERED);
            default -> false;
        };
    }

    // =========================
    // APPROVE
    // =========================
    public StockAllocation approveAllocation(Long id, String role) {

        StockAllocation allocation = stockAllocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        if (!role.equals("DISTRICT_OFFICER")) {
            throw new IllegalStateException("Only DISTRICT_OFFICER can approve");
        }

        if (!isValidTransition(allocation.getStatus(), AllocationStatus.APPROVED)) {
            throw new IllegalStateException("Invalid approval state");
        }

        AllocationStatus oldStatus = allocation.getStatus();
        allocation.setStatus(AllocationStatus.APPROVED);

        StockAllocation saved = stockAllocationRepository.save(allocation);

        historyRepo.save(new AllocationHistory(id, oldStatus, AllocationStatus.APPROVED, role));

        return saved;
    }

    // =========================
    // CANCEL
    // =========================
    public StockAllocation cancelAllocation(Long id, String role) {

        StockAllocation allocation = stockAllocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        if (!role.equals("ADMIN") && !role.equals("DISTRICT_OFFICER")) {
            throw new IllegalStateException("Only ADMIN or DISTRICT_OFFICER can cancel");
        }

        if (!isValidTransition(allocation.getStatus(), AllocationStatus.CANCELLED)) {
            throw new IllegalStateException("Invalid cancellation state");
        }

        AllocationStatus oldStatus = allocation.getStatus();
        allocation.setStatus(AllocationStatus.CANCELLED);

        StockAllocation saved = stockAllocationRepository.save(allocation);

        historyRepo.save(new AllocationHistory(id, oldStatus, AllocationStatus.CANCELLED, role));

        return saved;
    }

    // =========================
    // DISPATCH
    // =========================
    public StockAllocation dispatchAllocation(Long id) {

        StockAllocation allocation = stockAllocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        if (!isValidTransition(allocation.getStatus(), AllocationStatus.DISPATCHED)) {
            throw new IllegalStateException("Cannot dispatch from current state");
        }

        AllocationStatus oldStatus = allocation.getStatus();
        allocation.setStatus(AllocationStatus.DISPATCHED);

        StockAllocation saved = stockAllocationRepository.save(allocation);

        historyRepo.save(new AllocationHistory(id, oldStatus, AllocationStatus.DISPATCHED, "SYSTEM"));

        return saved;
    }

    // =========================
    // DELIVER
    // =========================
    public StockAllocation deliverAllocation(Long id) {

        StockAllocation allocation = stockAllocationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        if (!isValidTransition(allocation.getStatus(), AllocationStatus.DELIVERED)) {
            throw new IllegalStateException("Cannot deliver from current state");
        }

        AllocationStatus oldStatus = allocation.getStatus();
        allocation.setStatus(AllocationStatus.DELIVERED);

        StockAllocation saved = stockAllocationRepository.save(allocation);

        historyRepo.save(new AllocationHistory(id, oldStatus, AllocationStatus.DELIVERED, "SYSTEM"));

        return saved;
    }
}
