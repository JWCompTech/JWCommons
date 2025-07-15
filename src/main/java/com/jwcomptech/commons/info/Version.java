package com.jwcomptech.commons.info;

/*-
 * #%L
 * JWCT Commons
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

import com.jwcomptech.commons.values.IntegerValue;
import com.jwcomptech.commons.values.StringValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * A Basic Version Object.
 *
 * @since 1.0.0-alpha
 */
public final class Version implements Serializable {
    private final StringValue main;
    private final IntegerValue major;
    private final IntegerValue minor;
    private final IntegerValue build;
    private final IntegerValue revision;
    private final IntegerValue number;

    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Version(final String main, final int major, final int minor,
                   final int build, final int revision, final int number) {
        this.main = StringValue.of(main);
        this.major = IntegerValue.of(major);
        this.minor = IntegerValue.of(minor);
        this.build = IntegerValue.of(build);
        this.revision = IntegerValue.of(revision);
        this.number = IntegerValue.of(number);
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Version(final StringValue main, final IntegerValue major, final IntegerValue minor,
                   final IntegerValue build, final IntegerValue revision, final IntegerValue number) {
        this.main = main;
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;
        this.number = number;
    }

    @Serial
    private static final long serialVersionUID = -7549565491159696340L;

    @SuppressWarnings("ConfusingMainMethod")
    public StringValue main() { return main; }

    public IntegerValue major() { return major; }

    public IntegerValue minor() { return minor; }

    public IntegerValue build() { return build; }

    public IntegerValue revision() { return revision; }

    public IntegerValue number() { return number; }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        final Version version = (Version) obj;

        return new EqualsBuilder()
                .append(main, version.main)
                .append(major, version.major)
                .append(minor, version.minor)
                .append(build, version.build)
                .append(revision, version.revision)
                .append(number, version.number)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(main)
                .append(major)
                .append(minor)
                .append(build)
                .append(revision)
                .append(number)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Main", main)
                .append("Major", major)
                .append("Minor", minor)
                .append("Build", build)
                .append("Revision", revision)
                .append("Number", number)
                .toString();
    }
}
