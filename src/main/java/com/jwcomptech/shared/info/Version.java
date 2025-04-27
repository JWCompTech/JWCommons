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
    private final StringValue main;
    private final IntegerValue major;
    private final IntegerValue minor;
    private final IntegerValue build;
    private final IntegerValue revision;
    private final IntegerValue number;

    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Version(String main, int major, int minor,
                   int build, int revision, int number) {
        this.main = StringValue.of(main);
        this.major = IntegerValue.of(major);
        this.minor = IntegerValue.of(minor);
        this.build = IntegerValue.of(build);
        this.revision = IntegerValue.of(revision);
        this.number = IntegerValue.of(number);
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    public Version(StringValue main, IntegerValue major, IntegerValue minor,
                   IntegerValue build, IntegerValue revision, IntegerValue number) {
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
    public boolean equals(Object o) {
        if (this == o) return true;

        if (null == o || getClass() != o.getClass()) return false;

        Version version = (Version) o;

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