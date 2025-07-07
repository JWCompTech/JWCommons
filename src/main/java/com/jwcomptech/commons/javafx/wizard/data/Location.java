package com.jwcomptech.commons.javafx.wizard.data;

import com.jwcomptech.commons.base.Validated;
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
