package com.jwcomptech.commons.javafx.wizard.services;

/*-
 * #%L
 * JWCommons
 * %%
 * Copyright (C) 2025 JWCompTech
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import com.jwcomptech.commons.javafx.wizard.data.Region;
import com.jwcomptech.commons.utils.SingletonManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
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
