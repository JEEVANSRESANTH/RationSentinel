package com.rationsentinel.repository;

import com.rationsentinel.entity.FairPriceShop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FairPriceShopRepository extends JpaRepository<FairPriceShop, Long> {

    Optional<FairPriceShop> findByShopCode(String shopCode);
}
