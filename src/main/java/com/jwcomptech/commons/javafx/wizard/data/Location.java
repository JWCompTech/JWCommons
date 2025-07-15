package com.jwcomptech.commons.javafx.wizard.data;

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

import com.jwcomptech.commons.validators.Validated;
import com.jwcomptech.commons.consts.Numbers;
import com.jwcomptech.commons.validators.Condition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class Location extends Validated {
    private String id;
    private String name;
    private Region region;
    private int serversInstalled;
    private final int maxServers = Numbers.EIGHT;

    public Location(final String name, final Region region, final int serversInstalled) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.region = region;
        this.serversInstalled = serversInstalled;

        super.addToTrueVals(Condition.of(
                () -> serversInstalled < maxServers,
                        "Server limit reached for this location."
                ));
    }
}
