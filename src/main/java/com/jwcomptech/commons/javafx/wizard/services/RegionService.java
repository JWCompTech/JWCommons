package com.jwcomptech.commons.javafx.wizard.services;

import com.jwcomptech.commons.javafx.wizard.data.Region;
import com.jwcomptech.commons.utils.SingletonManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RegionService {
    private final List<Region> regions = new ArrayList<>();

    public RegionService() {
        regions.add(new Region("Eastern_US", "New_Jersey"));
        regions.add(new Region("Eastern_US", "New_York"));
    }

    public List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    public Optional<Region> findById(String id) {
        return regions.stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    public static RegionService getInstance() {
        return SingletonManager.getInstance(RegionService.class, RegionService::new);
    }
}
