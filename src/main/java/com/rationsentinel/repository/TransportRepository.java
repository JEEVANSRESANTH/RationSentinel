package com.rationsentinel.repository;

import com.rationsentinel.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransportRepository extends JpaRepository<Transport, Long> {

    Optional<Transport> findByStockAllocation_Id(Long allocationId);
}
