package com.jwcomptech.commons.javafx.wizard.data;

import com.jwcomptech.commons.base.Validated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class Company extends Validated {
    private String id;
    private String name;
    private String brandCss;
    private List<Location> locations;

    public Company(final String name, final String brandCss) {
        this(UUID.randomUUID().toString(), name, brandCss);
    }

    public Company(final String id, final String name, final String brandCss) {
        this.id = id;
        this.name = name;
        this.brandCss = brandCss;
        this.locations = new ArrayList<>();
    }
}
