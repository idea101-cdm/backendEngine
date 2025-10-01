package com.idea101.backendengine.restaurant.service;

import com.idea101.backendengine.restaurant.entity.Outlet;
import com.idea101.backendengine.restaurant.repository.OutletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OutletService {

    private final OutletRepository outletRepository;

    public OutletService(OutletRepository outletRepository) {
        this.outletRepository = outletRepository;
    }

    @Transactional
    public Outlet setDefaultOutlet(Outlet outlet) {

        if (outlet.getName() == null || outlet.getName().isBlank()) {
            throw new IllegalArgumentException("Outlet name required");
        }
        if (outlet.getAddress() == null || outlet.getAddress().isBlank()) {
            throw new IllegalArgumentException("Outlet address required");
        }

        if (outlet.getIsActive() == null) outlet.setIsActive(true);
        if (outlet.getIsVerified() == null) outlet.setIsVerified(false);



        return outletRepository.save(outlet);
    }

}
