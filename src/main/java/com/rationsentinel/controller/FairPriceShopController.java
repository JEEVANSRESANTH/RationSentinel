package com.rationsentinel.controller;

import com.rationsentinel.entity.FairPriceShop;
import com.rationsentinel.entity.User;
import com.rationsentinel.dto.FairPriceShopRequestDTO;
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
    public FairPriceShop createFps(@RequestBody FairPriceShopRequestDTO request) {

        User owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("FPS owner not found"));

        FairPriceShop fps = new FairPriceShop();
        fps.setDistrict(request.getDistrict());
        fps.setShopCode(request.getShopCode());
        fps.setOwner(owner);

        return fairPriceShopRepository.save(fps);
    }
}
