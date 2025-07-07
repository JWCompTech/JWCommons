package com.jwcomptech.commons.javafx.wizard.data;

import com.jwcomptech.commons.base.Validated;
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
