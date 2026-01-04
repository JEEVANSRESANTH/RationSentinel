package com.rationsentinel.config;

import com.rationsentinel.entity.Role;
import com.rationsentinel.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleDataInitializer {

    private final RoleRepository roleRepository;

    public RoleDataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void initRoles() {
        List<String> roles = List.of(
                "ADMIN",
                "DISTRICT_OFFICER",
                "TRANSPORTER",
                "FPS_OWNER"
        );

        for (String roleName : roles) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(roleName)));
        }

        System.out.println("✅ Roles initialized");
    }
}
