package com.jwcomptech.shared.info;

import com.jwcomptech.shared.values.IntegerValue;
import com.jwcomptech.shared.values.StringValue;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serial;
import java.io.Serializable;

/** A Basic Version Object. */
public final class Version implements Serializable {
    private final StringValue Main;
    private final IntegerValue Major;
    private final IntegerValue Minor;
    private final IntegerValue Build;
    private final IntegerValue Revision;
    private final IntegerValue Number;

    public Version(String main, int major, int minor,
                   int build, int revision, int number) {
        Main = StringValue.of(main);
        Major = IntegerValue.of(major);
        Minor = IntegerValue.of(minor);
        Build = IntegerValue.of(build);
        Revision = IntegerValue.of(revision);
        Number = IntegerValue.of(number);
    }

    public Version(StringValue main, IntegerValue major, IntegerValue minor,
                   IntegerValue build, IntegerValue revision, IntegerValue number) {
        Main = main;
        Major = major;
        Minor = minor;
        Build = build;
        Revision = revision;
        Number = number;
    }

    @Serial
    private static final long serialVersionUID = -7549565491159696340L;

    public StringValue Main() { return Main; }

    public IntegerValue Major() { return Major; }

    public IntegerValue Minor() { return Minor; }

    public IntegerValue Build() { return Build; }

    public IntegerValue Revision() { return Revision; }

    public IntegerValue Number() { return Number; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        return new EqualsBuilder()
                .append(Main, version.Main)
                .append(Major, version.Major)
                .append(Minor, version.Minor)
                .append(Build, version.Build)
                .append(Revision, version.Revision)
                .append(Number, version.Number)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(Main)
                .append(Major)
                .append(Minor)
                .append(Build)
                .append(Revision)
                .append(Number)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("Main", Main)
                .append("Major", Major)
                .append("Minor", Minor)
                .append("Build", Build)
                .append("Revision", Revision)
                .append("Number", Number)
                .toString();
    }
}