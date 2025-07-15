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
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class Region extends Validated {
    private String id;
    private String topLevelName;
    private String localLevelName;

    public Region(final String topLevelName,
                  final String localLevelName) {
        this(UUID.randomUUID().toString(), topLevelName, localLevelName);
    }

    public Region(final String id,
                  final String topLevelName,
                  final String localLevelName) {
        this.id = id;
        this.topLevelName = topLevelName;
        this.localLevelName = localLevelName;
    }

    public String getFullName() {
        return topLevelName + "-" + localLevelName;
    }
}
