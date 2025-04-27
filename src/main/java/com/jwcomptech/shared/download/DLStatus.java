package com.jwcomptech.shared.download;

import com.jwcomptech.shared.enums.BaseEnum;

public enum DLStatus implements BaseEnum<String> {
    DOWNLOADING("Downloading"),
    PAUSED("Paused"),
    COMPLETE("Complete"),
    CANCELLED("Cancelled"),
    ERROR("Error"),
    IDLE("Idle");

    final String value;

    DLStatus(String value) { this.value = value; }

    @Override
    public String getValue() { return value; }

    @Override
    public String toString() { return value; }
}
