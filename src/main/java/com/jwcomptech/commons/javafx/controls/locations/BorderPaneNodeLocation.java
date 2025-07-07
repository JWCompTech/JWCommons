package com.jwcomptech.commons.javafx.controls.locations;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = true)
public class BorderPaneNodeLocation extends NodeLocation<BorderPanePosition> {
    public BorderPaneNodeLocation(final BorderPanePosition position) {
        put("position", position);
    }

    public Optional<BorderPanePosition> getPosition() {
        return Optional.ofNullable(get("position"));
    }
}
