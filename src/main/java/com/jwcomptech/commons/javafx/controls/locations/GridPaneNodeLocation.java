package com.jwcomptech.commons.javafx.controls.locations;

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

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Optional;

@SuppressWarnings("unused")
@Value
@EqualsAndHashCode(callSuper = true)
public class GridPaneNodeLocation extends NodeLocation<Integer> {

    public GridPaneNodeLocation(final int columnIndex,
                                final int rowIndex) {
        put("columnIndex", columnIndex);
        put("rowIndex", rowIndex);
        put("colspan", 1);
        put("rowspan", 1);
    }

    public GridPaneNodeLocation(final int columnIndex,
                                final int rowIndex,
                                final int colspan,
                                final int rowspan) {
        put("columnIndex", columnIndex);
        put("rowIndex", rowIndex);
        put("colspan", colspan);
        put("rowspan", rowspan);
    }

    public Optional<Integer> getColumnIndex() {
        return Optional.ofNullable(get("columnIndex"));
    }

    public Optional<Integer> getRowIndex() {
        return Optional.ofNullable(get("rowIndex"));
    }

    public Optional<Integer> getColspan() {
        return Optional.ofNullable(get("colspan"));
    }

    public Optional<Integer> getRowspan() {
        return Optional.ofNullable(get("rowspan"));
    }
}
