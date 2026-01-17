package com.rationsentinel.service;

import com.rationsentinel.entity.*;
import com.rationsentinel.repository.TransportRepository;
import com.rationsentinel.repository.StockAllocationRepository;
import com.rationsentinel.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransportService {

    private final TransportRepository transportRepository;
    private final StockAllocationRepository allocationRepository;
    private final UserRepository userRepository;

    public TransportService(TransportRepository transportRepository,
                            StockAllocationRepository allocationRepository,
                            UserRepository userRepository) {
        this.transportRepository = transportRepository;
        this.allocationRepository = allocationRepository;
        this.userRepository = userRepository;
    }

    public Transport assignTransporter(Long allocationId, Long transporterId, String vehicleNumber) {

        StockAllocation allocation = allocationRepository.findById(allocationId)
                .orElseThrow(() -> new IllegalArgumentException("Allocation not found"));

        if (!allocation.getStatus().equals(AllocationStatus.APPROVED)) {
            throw new IllegalStateException("Allocation must be approved first");
        }

        if (transportRepository.findByStockAllocation_Id(allocationId).isPresent()) {
            throw new IllegalStateException("Transport already assigned for this allocation");
        }

        User transporter = userRepository.findById(transporterId)
                .orElseThrow(() -> new IllegalArgumentException("Transporter not found"));

        Transport transport = new Transport();
        transport.setStockAllocation(allocation);
        transport.setTransporter(transporter);
        transport.setVehicleNumber(vehicleNumber);
        transport.setDispatchTime(LocalDateTime.now());
        transport.setStatus(TransportStatus.ASSIGNED);

        return transportRepository.save(transport);
    }

    public Transport markInTransit(Long allocationId) {

        Transport transport = transportRepository.findByStockAllocation_Id(allocationId)
                .orElseThrow(() -> new IllegalArgumentException("Transport not found for allocation"));

        transport.setStatus(TransportStatus.IN_TRANSIT);
        transport.setDispatchTime(LocalDateTime.now());

        return transportRepository.save(transport);
    }

    public Transport confirmDelivery(Long allocationId) {

        Transport transport = transportRepository.findByStockAllocation_Id(allocationId)
                .orElseThrow(() -> new IllegalArgumentException("Transport not found"));

        transport.setStatus(TransportStatus.DELIVERED_TO_FPS);
        transport.setActualArrivalTime(LocalDateTime.now());

        transport.getStockAllocation().setStatus(AllocationStatus.DELIVERED);

        return transportRepository.save(transport);
    }
}
