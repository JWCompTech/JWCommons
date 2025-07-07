package com.jwcomptech.commons.javafx.controls.locations;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Optional;

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
