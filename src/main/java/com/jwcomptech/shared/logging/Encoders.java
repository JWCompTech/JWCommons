package com.jwcomptech.shared.logging;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public enum Encoders {
    /**
     * A {@link PatternLayoutEncoder} with the pattern "%msg%n".
     * @apiNote This encoder is automatically started after initialization.
     */
    LimitedEncoder(LoggingManager.createNewLogEncoder("%msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%-12d{YYYY-MM-dd HH:mm:ss} %level %logger{0} - %msg%n".
     * @apiNote This encoder is automatically started after initialization.
     */
    BasicEncoder(LoggingManager.createNewLogEncoder("%-12d{YYYY-MM-dd HH:mm:ss} %level %logger{0} - %msg%n")),
    /**
     * A {@link PatternLayoutEncoder} with the pattern
     * "%-12d{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{100} - %msg%n".
     * @apiNote This encoder is automatically started after initialization.
     */
    ExtendedEncoder(LoggingManager.createNewLogEncoder(
            "%-12d{YYYY-MM-dd HH:mm:ss.SSS} [%thread] %level %logger{100} - %msg%n"))
    ;

    private final PatternLayoutEncoder encoder;

    Encoders(final PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("encoder", encoder)
                .toString();
    }
}
