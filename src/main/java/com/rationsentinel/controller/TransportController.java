package com.rationsentinel.controller;

import com.rationsentinel.entity.Transport;
import com.rationsentinel.service.TransportService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transports")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    // =========================
    // ASSIGN TRANSPORTER
    // =========================
    @PostMapping("/assign")
    public Transport assignTransporter(@RequestParam Long allocationId,
                                       @RequestParam Long transporterId,
                                       @RequestParam String vehicleNumber) {
        return transportService.assignTransporter(allocationId, transporterId, vehicleNumber);
    }

    // =========================
    // MARK IN TRANSIT
    // =========================
    @PutMapping("/{allocationId}/in-transit")
    public Transport markInTransit(@PathVariable Long allocationId) {
        return transportService.markInTransit(allocationId);
    }

    // =========================
    // CONFIRM DELIVERY AT FPS
    // =========================
    @PutMapping("/{allocationId}/delivered")
    public Transport confirmDelivery(@PathVariable Long allocationId) {
        return transportService.confirmDelivery(allocationId);
    }
}
