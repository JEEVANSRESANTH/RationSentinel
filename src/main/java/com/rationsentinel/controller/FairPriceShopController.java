package com.rationsentinel.controller;

import com.rationsentinel.entity.FairPriceShop;
import com.rationsentinel.entity.User;
import com.rationsentinel.repository.FairPriceShopRepository;
import com.rationsentinel.repository.UserRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fps")
public class FairPriceShopController {

    private final FairPriceShopRepository fairPriceShopRepository;
    private final UserRepository userRepository;

    public FairPriceShopController(FairPriceShopRepository fairPriceShopRepository,
                                   UserRepository userRepository) {
        this.fairPriceShopRepository = fairPriceShopRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public FairPriceShop createFps(@RequestBody FairPriceShop request) {

        User owner = userRepository.findById(request.getOwner().getId())
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        FairPriceShop fps = new FairPriceShop();
        fps.setShopCode(request.getShopCode());
        fps.setDistrict(request.getDistrict());
        fps.setOwner(owner);

        return fairPriceShopRepository.save(fps);
    }
}
