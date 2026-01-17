package com.rationsentinel.repository;

import com.rationsentinel.entity.AllocationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllocationHistoryRepository extends JpaRepository<AllocationHistory, Long> {
}
