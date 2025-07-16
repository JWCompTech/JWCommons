package com.jwcomptech.commons.values;

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

import com.jwcomptech.commons.consts.Literals;
import com.jwcomptech.commons.validators.Validated;
import com.jwcomptech.commons.utils.CollectionUtils;
import com.jwcomptech.commons.utils.RegExPatterns;
import com.jwcomptech.commons.validators.EmailValidator;
import com.jwcomptech.commons.utils.StringUtils;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.Charset;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.function.Function;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.jwcomptech.commons.consts.Literals.*;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNull;
import static com.jwcomptech.commons.validators.Preconditions.checkArgumentNotNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.apache.commons.lang3.StringUtils.stripStart;

/**
 * Provides immutable access to an {@link String}.
 *
 * @since 1.0.0-alpha
 */
@SuppressWarnings({"ClassWithTooManyMethods", "OverlyComplexClass", "unused"})
public final class StringValue extends Validated
        implements ImmutableValue<String> {
    private final String value;
    private static final Integer INDEX_NOT_FOUND = -1;
    /**
     * Required for serialization support.
     *
     * @see Serializable
     */
    @Serial
    private static final long serialVersionUID = -2993854030636280240L;

    private StringValue() {
        value = Literals.EMPTY;
    }

    private StringValue(final String input) {
        value = input;
    }

    @SuppressWarnings("TypeMayBeWeakened")
    private StringValue(final @NotNull StringBuilder input) {
        value = input.toString();
    }

    private StringValue(final @NotNull CharSequence input) {
        value = input.toString();
    }

    /**
     * A static instance of StringValue with the value as an empty string.
     */
    public static final StringValue EMPTY = new StringValue(Literals.EMPTY);

    /**
     * Creates a new StringValue instance with the specified default value.
     * @param defaultValue the value to set
     * @return a new StringValue instance with the specified default value
     */
    @Contract("_ -> new")
    public static @NotNull StringValue of(final String defaultValue) {
        return new StringValue(defaultValue);
    }

    /**
     * Creates a new StringValue instance with the specified default value.
     * @param defaultValue the value to set
     * @return a new StringValue instance with the specified default value
     */
    @Contract("_ -> new")
    public static @NotNull StringValue of(final StringBuilder defaultValue) {
        return new StringValue(defaultValue);
    }

    /**
     * Creates a new StringValue instance with the specified default value.
     * @param defaultValue the value to set
     * @return a new StringValue instance with the specified default value
     */
    @Contract("_ -> new")
    public static @NotNull StringValue of(final CharSequence defaultValue) {
        return new StringValue(defaultValue);
    }

    /**
     * Returns the string representation of the {@code boolean} argument.
     * @param bool a {@code boolean}.
     * @return if the argument is {@code true}, a string equal to
     *         {@code "true"} is returned; otherwise, a string equal to
     *         {@code "false"} is returned.
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final boolean bool) {
        return StringValue.of(String.valueOf(bool));
    }

    /**
     * Returns the string representation of the {@code BooleanValue} argument.
     * @param value a {@code BooleanValue}.
     * @return if the argument is {@code BooleanValue.TRUE}, a string equal to
     *         {@code "true"} is returned; otherwise, a string equal to
     *         {@code "false"} is returned.
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(@NotNull final BooleanValue value) {
        return StringValue.of(value.toString());
    }

    /**
     * Returns the string representation of the {@code char} argument.
     * @param c a {@code char}.
     * @return a string of length {@code 1} containing
     *         as its single character the argument {@code c}.
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final char c) {
        return StringValue.of(String.valueOf(c));
    }

    /**
     * Returns the string representation of the {@code int} argument.
     * <p>
     * The representation is exactly the one returned by the
     * {@code Integer.toString} method of one argument.
     * @param i an {@code int}.
     * @return a string representation of the {@code int} argument.
     * @see Integer#toString(int, int)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final int i) {
        return StringValue.of(Integer.toString(i));
    }

    /**
     * Returns the string representation of the {@code IntegerValue} argument.
     * @param value an {@code IntegerValue}.
     * @return a string representation of the {@code IntegerValue} argument.
     * @see Integer#toString(int, int)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(@NotNull final IntegerValue value) {
        return StringValue.of(String.valueOf(value));
    }

    /**
     * Returns the string representation of the {@code long} argument.
     * <p>
     * The representation is exactly the one returned by the
     * {@code Long.toString} method of one argument.
     * @param l a {@code long}.
     * @return a string representation of the {@code long} argument.
     * @see Long#toString(long)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final long l) {
        return StringValue.of(String.valueOf(l));
    }

    /**
     * Returns the string representation of the {@code LongValue} argument.
     * @param value a {@code LongValue}.
     * @return a string representation of the {@code LongValue} argument.
     * @see Long#toString(long)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final LongValue value) {
        return StringValue.of(String.valueOf(value));
    }

    /**
     * Returns the string representation of the {@code float} argument.
     * <p>
     * The representation is exactly the one returned by the
     * {@code Float.toString} method of one argument.
     * @param f a {@code float}.
     * @return a string representation of the {@code float} argument.
     * @see Float#toString(float)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final float f) {
        return StringValue.of(Float.toString(f));
    }

    /**
     * Returns the string representation of the {@code FloatValue} argument.
     * @param value a {@code FloatValue}.
     * @return a string representation of the {@code FloatValue} argument.
     * @see Float#toString(float)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final FloatValue value) {
        return StringValue.of(String.valueOf(value));
    }

    /**
     * Returns the string representation of the {@code double} argument.
     * <p>
     * The representation is exactly the one returned by the
     * {@code Double.toString} method of one argument.
     * @param d a {@code double}.
     * @return a string representation of the {@code double} argument.
     * @see Double#toString(double)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final double d) {
        return StringValue.of(Double.toString(d));
    }

    /**
     * Returns the string representation of the {@code DoubleValue} argument.
     * @param value a {@code DoubleValue}.
     * @return a string representation of the {@code Double} argument.
     * @see Double#toString(double)
     */
    @Contract("_ -> new")
    public static @NotNull StringValue valueOf(final DoubleValue value) {
        return StringValue.of(String.valueOf(value));
    }

    /**
     * Returns a new StringValue with an empty string as the initial value.
     * @return a new StringValue with an empty string as the initial value
     */
    @Contract(" -> new")
    public static @NotNull StringValue blank() {
        return new StringValue();
    }

    /**
     * Returns a new StringValue with a space as the initial value
     * @return a new StringValue with a space as the initial value
     */
    @Contract(" -> new")
    public static @NotNull StringValue space() {
        return new StringValue(" ");
    }

    /**
     * Converts this instance to a MutableStringValue.
     * @return this instance as a MutableStringValue
     */
    @Contract(" -> new")
    public @NotNull MutableStringValue toMutable() {
        return MutableStringValue.of(value);
    }

    /**
     * Adds the specified string to the end of the value.
     * @param input the string to add
     * @return the specified string added to the end of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToEnd(final String input) {
        return StringValue.of(value + input);
    }

    /**
     * Adds the specified string to the end of the value.
     * @param input the string to add
     * @return the specified string added to the end of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToEnd(final @NotNull StringValue input) {
        return StringValue.of(value + input.get());
    }

    /**
     * Adds the specified string to the end of the value.
     * @param input the string to add
     * @return the specified string added to the end of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToEnd(final @NotNull MutableStringValue input) {
        return StringValue.of(value + input.get());
    }

    /**
     * Adds the specified character to the end of the value.
     * @param input the string to add
     * @return the specified string added to the end of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToEnd(final char input) {
        return StringValue.of(value + input);
    }

    /**
     * Adds the specified string to the start of the value.
     * @param input the string to add
     * @return the specified string added to the start of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToStart(final String input) {
        return StringValue.of(input + value);
    }

    /**
     * Adds the specified string to the start of the value.
     * @param input the string to add
     * @return the specified string added to the start of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToStart(final @NotNull StringValue input) {
        return StringValue.of(input.get() + value);
    }

    /**
     * Adds the specified string to the start of the value.
     * @param input the string to add
     * @return the specified string added to the start of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToStart(final @NotNull MutableStringValue input) {
        return StringValue.of(input.get() + value);
    }

    /**
     * Adds the specified character to the start of the value.
     * @param input the string to add
     * @return the specified string added to the start of the value
     */
    @Contract("_ -> new")
    public @NotNull StringValue addToStart(final char input) {
        return StringValue.of(input + value);
    }

    /**
     * Converts the delimited string value into a map.
     * <p>Expects that "{@literal =}" separates the keys and values and a "{@literal &}" separates the key pairs.</p>
     * @return string representation of map
     * @throws IllegalArgumentException if the value is null
     */
    public @NotNull Map<StringValue, StringValue> convertToMap() {
        return CollectionUtils.convertStringToMap(value).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> StringValue.of(e.getKey()),
                        e -> StringValue.of(e.getValue()))
                );
    }

    /**
     * Converts the Base64 byte string to an object.
     * @return the converted object
     * @throws IllegalArgumentException if the value is null
     * @throws IOException if the conversion fails
     * @throws ClassNotFoundException if the converted object doesn't match a found object type
     */
    public @NotNull Object convertFromByteString()
            throws IOException, ClassNotFoundException {
        checkArgumentNotNull(value, cannotBeNull("value"));
        final var bytes = Base64.getDecoder().decode(value);
        try (final var bis = new ByteArrayInputStream(bytes); final ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    /**
     * Surrounds the value with quotes.
     * @return the value surrounded with quotes
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue quoteString() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of("\"%s\"".formatted(value));
    }

    /**
     * Removes quotes from the value.
     * @return the value with quotes removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue unquoteString() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of((value.startsWith("\"") && value.endsWith("\""))
                || (value.startsWith("'") && value.endsWith("'"))
                ? value.substring(1, value.length() - 1) : value);
    }

    /**
     * Converts the value to a Boolean.
     * @return the value of the parsed string
     * @throws IllegalArgumentException if the value does not match a boolean value or is null
     */
    public boolean toBoolean() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        final var value = this.value.toLowerCase(Locale.getDefault()).trim();
        return switch (value) {
            case "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled" -> true;
            case "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled" -> false;
            default -> throw new IllegalArgumentException("Input is not a boolean value.");
        };
    }

    /**
     * Checks if the value is a boolean.
     * @return true if the value matches a boolean string
     */
    public boolean isBoolean() {
        if(isBlank()) return false;
        final var value = this.value.toLowerCase(Locale.getDefault()).trim();
        return switch (value) {
            case "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled",
                 "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled" -> true;
            default -> false;
        };
    }

    /**
     * Checks if the value is a valid IPv4 address.
     * @return true the value is a valid IPv4 address
     * @throws IllegalArgumentException if the value is null
     */
    public boolean isValidIPAddress() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return value.matches(RegExPatterns.IPADDRESS.getRegex());
    }

    /**
     * Checks if the value is a valid URL.
     * @return true if the value is a valid URL
     * @throws IllegalArgumentException if the value is null
     */
    public boolean isValidUrl() {
        checkArgumentNotNull(value, cannotBeNullOrEmpty("value"));
        return value.matches(RegExPatterns.URL.getRegex());
    }

//    /**
//     * Checks to see if the value is a valid email address according to the RFC 2822 specification.
//     * @return true if the value is valid according to RFC 2822
//     * @throws IllegalArgumentException if the value is null
//     */
//    public boolean isValidEmailAddress() {
//        return isValidEmailAddress(false, false);
//    }
//
//    /**
//     * Checks to see if the value is a valid email address according to the RFC 2822 specification.
//     * @param allowQuotedIdentifiers specifies if quoted identifiers are allowed
//     * @return true if the value is valid according to RFC 2822
//     * @throws IllegalArgumentException if the value is null
//     */
//    public boolean isValidEmailAddress(final boolean allowQuotedIdentifiers) {
//        return isValidEmailAddress(allowQuotedIdentifiers, false);
//    }

//    /**
//     * Checks to see if the value is a valid email address according to the RFC 2822 specification.
//     * @param allowQuotedIdentifiers specifies if quoted identifiers are allowed
//     * @param allowDomainLiterals specifies if domain literals are allowed
//     * @return true if the value is valid according to RFC 2822
//     * @throws IllegalArgumentException if the value is null
//     */
//    public boolean isValidEmailAddress(final boolean allowQuotedIdentifiers, final boolean allowDomainLiterals) {
//        checkArgumentNotNull(value, cannotBeNull("value"));
//        return EmailValidator.builder()
//        .allowQuotedIdentifiers(allowQuotedIdentifiers)
//                .allowDomainLiterals(allowDomainLiterals)
//                .build().isValid(value);
//    }

    /**
     * Ensures that the value starts with a given prefix.
     * @param prefix prefix to check
     * @param ignoreCase if true ignores case
     * @return the value with the specified prefix
     * @throws IllegalArgumentException if the value or prefix are null
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue ensureStartsWith(final String prefix, final Boolean ignoreCase) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(prefix, cannotBeNull("prefix"));
        var startsWith = value.startsWith(prefix);
        if(!startsWith && ignoreCase) startsWith = value.startsWith(prefix.toUpperCase(Locale.getDefault()));
        if(!startsWith && ignoreCase) startsWith = value.startsWith(prefix.toLowerCase(Locale.getDefault()));
        return StringValue.of(startsWith ? value : prefix + value);
    }

    /**
     * Ensures that the value ends with a given suffix.
     * @param suffix suffix to check
     * @param ignoreCase if true ignores case
     * @return the value with the specified suffix
     * @throws IllegalArgumentException if the value or suffix are null
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue ensureEndsWith(final String suffix, final Boolean ignoreCase) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(suffix, cannotBeNull("suffix"));
        var endsWith = value.endsWith(suffix);
        if(!endsWith && ignoreCase) endsWith = value.startsWith(suffix.toUpperCase(Locale.getDefault()));
        if(!endsWith && ignoreCase) endsWith = value.startsWith(suffix.toLowerCase(Locale.getDefault()));
        return StringValue.of(endsWith ? value : value + suffix);
    }

    /**
     * Checks if the value ends with the specified suffix.
     * @param   suffix the suffix to check
     * @return  {@code true} if the character sequence represented by the
     *          argument is a suffix of the character sequence represented by
     *          this object; {@code false} otherwise. Note that the
     *          result will be {@code true} if the argument is the
     *          empty string or is equal to this {@code String} object
     *          as determined by the {@link #equals(Object)} method.
     * @throws IllegalArgumentException if the value or suffix are null
     * @see String#endsWith(String)
     */
    public boolean endsWith(final String suffix) {
        return value.endsWith(suffix);
    }

    /**
     * Checks if the value ends with a given suffix.
     * @param suffix suffix to check
     * @return true if the value ends with a given suffix
     * @throws IllegalArgumentException if the value or suffix are null
     */
    public boolean endsWithIgnoreCase(final String suffix) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(suffix, cannotBeNull("suffix"));
        return value.endsWith(suffix)
                || (value.length() >= suffix.length() && value.toLowerCase(Locale.getDefault())
                .endsWith(suffix.toLowerCase(Locale.getDefault())));
    }

    /**
     * Checks if the value starts with the specified prefix.
     * @param   prefix the prefix to check
     * @return  {@code true} if the character sequence represented by the
     *          argument is a prefix of the character sequence represented by
     *          this string; {@code false} otherwise.
     *          Note also that {@code true} will be returned if the
     *          argument is an empty string or is equal to this
     *          {@code String} object as determined by the
     *          {@link #equals(Object)} method.
     * @throws IllegalArgumentException if the value or suffix are null
     * @see String#startsWith(String)
     */
    public boolean startsWith(final String prefix) {
        return startsWith(prefix, 0);
    }

    /**
     * Checks if the substring of the value beginning at the
     * specified index starts with the specified prefix.
     * @param   prefix the prefix to check
     * @param   offset where to begin looking in this string.
     * @return  {@code true} if the character sequence represented by the
     *          argument is a prefix of the substring of this object starting
     *          at index {@code offset}; {@code false} otherwise.
     *          The result is {@code false} if {@code offset} is
     *          negative or greater than the length of this
     *          {@code String} object; otherwise the result is the same
     *          as the result of the expression
     *          <pre>
     *          this.substring(offset).startsWith(prefix)
     *          </pre>
     * @throws IllegalArgumentException if the value or suffix are null
     * @see String#startsWith(String, int)
     */
    public boolean startsWith(final String prefix, final int offset) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(prefix, cannotBeNull("prefix"));
        return value.startsWith(prefix, offset);
    }

    /**
     * Checks if the value starts with a given suffix.
     * @param prefix suffix to check
     * @return true if the value starts with a given suffix
     * @throws IllegalArgumentException if the value or suffix are null
     */
    public boolean startsWithIgnoreCase(final String prefix) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(prefix, cannotBeNull("prefix"));
        return value.startsWith(prefix)
                || (value.length() >= prefix.length() && value.toLowerCase(Locale.getDefault())
                .startsWith(prefix.toLowerCase(Locale.getDefault())));
    }

    /**
     * Returns the index within this string of the first occurrence of
     * the specified character. If a character with value
     * {@code ch} occurs in the character sequence represented by
     * this {@code String} object, then the index (in Unicode
     * code units) of the first such occurrence is returned. For
     * values of {@code ch} in the range from 0 to 0xFFFF
     * (inclusive), this is the smallest value <i>k</i> such that:
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * smallest value <i>k</i> such that:
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this string, then {@code -1} is returned.
     * @param   ch   a character (Unicode code point).
     * @return  the index of the first occurrence of the character in the
     *          character sequence represented by this string, or
     *          {@code -1} if the character does not occur.
     * @see String#indexOf(int)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue indexOf(final char ch) {
        return IntegerValue.of(value.indexOf(ch));
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified character, starting the search at the specified index.
     * <p>
     * If a character with value {@code ch} occurs in the
     * character sequence represented by this {@code String}
     * object at an index no smaller than {@code fromIndex}, then
     * the index of the first such occurrence is returned. For values
     * of {@code ch} in the range from 0 to 0xFFFF (inclusive),
     * this is the smallest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &gt;= fromIndex)
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * smallest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &gt;= fromIndex)
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this string at or after position {@code fromIndex}, then
     * {@code -1} is returned.
     * <p>
     * There is no restriction on the value of {@code fromIndex}. If it
     * is negative, it has the same effect as if it were zero: this entire
     * string may be searched. If it is greater than the length of this
     * string, it has the same effect as if it were equal to the length of
     * the value: {@code -1} is returned.
     * <p>All indices are specified in {@code char} values
     * (Unicode code units).
     * @param ch        a character (Unicode code point).
     * @param fromIndex the index to start the search from.
     * @return the index of the first occurrence of the character in the
     * character sequence represented by the value that is greater
     * than or equal to {@code fromIndex}, or {@code -1}
     * if the character does not occur.
     * @apiNote Unlike {@link #substring(int)}, for example, this method does not throw
     * an exception when {@code fromIndex} is outside the valid range.
     * Rather, it returns -1 when {@code fromIndex} is larger than the length of
     * the string.
     * This result is, by itself, indistinguishable from a genuine absence of
     * {@code ch} in the string.
     * If stricter behavior is needed, {@link #indexOf(char, int, int)}
     * should be considered instead.
     * On a {@link String} {@code s}, for example,
     * {@code s.indexOf(ch, fromIndex, s.length())} would throw if
     * {@code fromIndex} were larger than the string length, or were negative.
     * @see String#indexOf(int, int)
     */
    @Contract("_, _ -> new")
    public @NotNull IntegerValue indexOf(final char ch, final int fromIndex) {
        return IntegerValue.of(value.indexOf(ch, fromIndex));
    }

    /**
     * Returns the index within the value of the first occurrence of the
     * specified character, starting the search at {@code beginIndex} and
     * stopping before {@code endIndex}.
     * <p>If a character with value {@code ch} occurs in the
     * character sequence represented by this {@code String}
     * object at an index no smaller than {@code beginIndex} but smaller than
     * {@code endIndex}, then
     * the index of the first such occurrence is returned. For values
     * of {@code ch} in the range from 0 to 0xFFFF (inclusive),
     * this is the smallest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) &amp;&amp; (beginIndex &lt;= <i>k</i> &lt; endIndex)
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * smallest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) &amp;&amp; (beginIndex &lt;= <i>k</i> &lt; endIndex)
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this
     * string at or after position {@code beginIndex} and before position
     * {@code endIndex}, then {@code -1} is returned.
     * <p>All indices are specified in {@code char} values
     * (Unicode code units).
     * @param ch         a character (Unicode code point).
     * @param beginIndex the index to start the search from (included).
     * @param endIndex   the index to stop the search at (excluded).
     * @return the index of the first occurrence of the character in the
     * character sequence represented by this object that is greater
     * than or equal to {@code beginIndex} and less than {@code endIndex},
     * or {@code -1} if the character does not occur.
     * @throws StringIndexOutOfBoundsException if {@code beginIndex}
     *                                         is negative, or {@code endIndex} is larger than the length of
     *                                         this {@code String} object, or {@code beginIndex} is larger than
     *                                         {@code endIndex}.
     * @see String#indexOf(int, int, int)
     */
    @Contract("_, _, _ -> new")
    public @NotNull IntegerValue indexOf(final char ch, final int beginIndex, final int endIndex) {
        return IntegerValue.of(value.indexOf(ch, beginIndex, endIndex));
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring.
     * <p>The returned index is the smallest value {@code k} for which:
     * <pre>{@code
     * this.startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     * @param str the substring to search for.
     * @return the index of the first occurrence of the specified substring,
     * or {@code -1} if there is no such occurrence.
     * @see String#indexOf(String)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue indexOf(final String str) {
        return IntegerValue.of(value.indexOf(str));
    }

    /**
     * Returns the index within this string of the first occurrence of the
     * specified substring, starting at the specified index.
     * <p>The returned index is the smallest value {@code k} for which:
     * <pre>{@code
     *     k >= Math.min(fromIndex, this.length()) &&
     *                   this.startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     * @param str       the substring to search for.
     * @param fromIndex the index from which to start the search.
     * @return the index of the first occurrence of the specified substring,
     * starting at the specified index,
     * or {@code -1} if there is no such occurrence.
     * @apiNote Unlike {@link #substring(int)}, for example, this method does not throw
     * an exception when {@code fromIndex} is outside the valid range.
     * Rather, it returns -1 when {@code fromIndex} is larger than the length of
     * the string.
     * This result is, by itself, indistinguishable from a genuine absence of
     * {@code str} in the string.
     * If stricter behavior is needed, {@link #indexOf(String, int, int)}
     * should be considered instead.
     * On {@link String} {@code s} and a non-empty {@code str}, for example,
     * {@code s.indexOf(str, fromIndex, s.length())} would throw if
     * {@code fromIndex} were larger than the string length, or were negative.
     * @see String#indexOf(String, int)
     */
    @Contract("_, _ -> new")
    public @NotNull IntegerValue indexOf(final String str, final int fromIndex) {
        return IntegerValue.of(value.indexOf(str, fromIndex));
    }

    /**
     * Returns the index of the first occurrence of the specified substring
     * within the specified index range of {@code this} string.
     * <p>This method returns the same result as the one of the invocation
     * <pre>{@code
     *     s.substring(beginIndex, endIndex).indexOf(str) + beginIndex
     * }</pre>
     * if the index returned by {@link #indexOf(String)} is non-negative,
     * and returns -1 otherwise.
     * (No substring is instantiated, though.)
     * @param str        the substring to search for.
     * @param beginIndex the index to start the search from (included).
     * @param endIndex   the index to stop the search at (excluded).
     * @return the index of the first occurrence of the specified substring
     * within the specified index range,
     * or {@code -1} if there is no such occurrence.
     * @throws StringIndexOutOfBoundsException if {@code beginIndex}
     *                                         is negative, or {@code endIndex} is larger than the length of
     *                                         this {@code String} object, or {@code beginIndex} is larger than
     *                                         {@code endIndex}.
     * @see String#indexOf(String, int, int)
     */
    @Contract("_, _, _ -> new")
    public @NotNull IntegerValue indexOf(final String str, final int beginIndex, final int endIndex) {
        return IntegerValue.of(value.indexOf(str, beginIndex, endIndex));
    }

    /**
     * Case in-sensitive find of the first index within athe value.
     *
     * <p>A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches
     * an empty search CharSequence.</p>
     *
     * <pre>
     * indexOfIgnoreCase(null) [value = *]          = -1
     * indexOfIgnoreCase("") [value = ""]           = 0
     * indexOfIgnoreCase(" ") [value = " "]         = 0
     * indexOfIgnoreCase("a") [value = "aabaabaa"]  = 0
     * indexOfIgnoreCase("b") [value = "aabaabaa"]  = 2
     * indexOfIgnoreCase("ab") [value = "aabaabaa"] = 1
     * </pre>
     *
     * @param searchStr  the CharSequence to find, may be null
     * @return the first index of the search CharSequence,
     *  -1 if no match or {@code null} string input
     */
    public @NotNull IntegerValue indexOfIgnoreCase(final CharSequence searchStr) {
        return indexOfIgnoreCase(searchStr, 0);
    }

    /**
     * Case in-sensitive find of the first index within the value
     * from the specified position.
     *
     * <p>A negative start position is treated as zero.
     * An empty ("") search CharSequence always matches.
     * A start position greater than the string length only matches
     * an empty search CharSequence.</p>
     *
     * <pre>
     * indexOfIgnoreCase(null, *) [value = *]          = -1
     * indexOfIgnoreCase("", 0) [value = ""]           = 0
     * indexOfIgnoreCase("A", 0) [value = "aabaabaa"]  = 0
     * indexOfIgnoreCase("B", 0) [value = "aabaabaa"]  = 2
     * indexOfIgnoreCase("AB", 0) [value = "aabaabaa"] = 1
     * indexOfIgnoreCase("B", 3) [value = "aabaabaa"]  = 5
     * indexOfIgnoreCase("B", 9) [value = "aabaabaa"]  = -1
     * indexOfIgnoreCase("B", -1) [value = "aabaabaa"] = 2
     * indexOfIgnoreCase("", 2) [value = "aabaabaa"]   = 2
     * indexOfIgnoreCase("", 9) [value = "abc"]        = -1
     * </pre>
     *
     * @param searchStr  the CharSequence to find, may be null
     * @param startPos  the start position, negative treated as zero
     * @return the first index of the search CharSequence (always &ge; startPos),
     *  -1 if no match or {@code null} string input
     */
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public @NotNull IntegerValue indexOfIgnoreCase(final CharSequence searchStr, final int startPos) {
        int startPos_ = startPos;
        if (searchStr == null) {
            return IntegerValue.of(INDEX_NOT_FOUND);
        }
        if (startPos_ < 0) {
            startPos_ = 0;
        }
        final int endLimit = value.length() - searchStr.length() + 1;
        if (startPos_ > endLimit) {
            return IntegerValue.of(INDEX_NOT_FOUND);
        }
        if (searchStr.isEmpty()) {
            return IntegerValue.of(startPos_);
        }
        for (int i = startPos_; i < endLimit; i++) {
            if (regionMatches(value,true, i, searchStr, 0, searchStr.length())) {
                return IntegerValue.of(i);
            }
        }
        return IntegerValue.of(INDEX_NOT_FOUND);
    }

    /**
     * Green implementation of regionMatches.
     *
     * @param cs the {@link CharSequence} to be processed
     * @param ignoreCase whether to be case-insensitive
     * @param thisStart the index to start on the {@code cs} CharSequence
     * @param substring the {@link CharSequence} to be looked for
     * @param start the index to start on the {@code substring} CharSequence
     * @param length character length of the region
     * @return whether the region matched
     */
    @SuppressWarnings({"MethodWithTooManyParameters", "OverlyComplexMethod",
            "ValueOfIncrementOrDecrementUsed", "SameParameterValue"})
    private static boolean regionMatches(final CharSequence cs, final boolean ignoreCase, final int thisStart,
                                 final CharSequence substring, final int start, final int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        }
        int index1 = thisStart;
        int index2 = start;
        int tmpLen = length;

        // Extract these first so we detect NPEs the same as the java.lang.String version
        final int srcLen = cs.length() - thisStart;
        final int otherLen = substring.length() - start;

        // Check for invalid parameters
        if (thisStart < 0 || start < 0 || length < 0) {
            return false;
        }

        // Check that the regions are long enough
        if (srcLen < length || otherLen < length) {
            return false;
        }

        while (tmpLen-- > 0) {
            final char c1 = cs.charAt(index1++);
            final char c2 = substring.charAt(index2++);

            if (c1 == c2) {
                continue;
            }

            if (!ignoreCase) {
                return false;
            }

            // The real same check as in String.regionMatches():
            final char u1 = Character.toUpperCase(c1);
            final char u2 = Character.toUpperCase(c2);
            if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the index within this string of the last occurrence of
     * the specified character. For values of {@code ch} in the
     * range from 0 to 0xFFFF (inclusive), the index (in Unicode code
     * units) returned is the largest value <i>k</i> such that:
     * <blockquote><pre>
     * this.charAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * largest value <i>k</i> such that:
     * <blockquote><pre>
     * this.codePointAt(<i>k</i>) == ch
     * </pre></blockquote>
     * is true.  In either case, if no such character occurs in this
     * string, then {@code -1} is returned.  The
     * {@code String} is searched backwards starting at the last
     * character.
     * @param ch a character (Unicode code point).
     * @return the index of the last occurrence of the character in the
     * character sequence represented by this object, or
     * {@code -1} if the character does not occur.
     * @see String#lastIndexOf(int)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue lastIndexOf(final char ch) {
        return IntegerValue.of(value.lastIndexOf(ch));
    }

    /**
     * Returns the index within this string of the last occurrence of
     * the specified character, searching backward starting at the
     * specified index. For values of {@code ch} in the range
     * from 0 to 0xFFFF (inclusive), the index returned is the largest
     * value <i>k</i> such that:
     * <blockquote><pre>
     * (this.charAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &lt;= fromIndex)
     * </pre></blockquote>
     * is true. For other values of {@code ch}, it is the
     * largest value <i>k</i> such that:
     * <blockquote><pre>
     * (this.codePointAt(<i>k</i>) == ch) {@code &&} (<i>k</i> &lt;= fromIndex)
     * </pre></blockquote>
     * is true. In either case, if no such character occurs in this
     * string at or before position {@code fromIndex}, then
     * {@code -1} is returned.
     * <p>All indices are specified in {@code char} values
     * (Unicode code units).
     * @param ch        a character (Unicode code point).
     * @param fromIndex the index to start the search from. There is no
     *                  restriction on the value of {@code fromIndex}. If it is
     *                  greater than or equal to the length of this string, it has
     *                  the same effect as if it were equal to one less than the
     *                  length of this string: this entire string may be searched.
     *                  If it is negative, it has the same effect as if it were -1:
     *                  -1 is returned.
     * @return the index of the last occurrence of the character in the
     * character sequence represented by this object that is less
     * than or equal to {@code fromIndex}, or {@code -1}
     * if the character does not occur before that point.
     * @see String#lastIndexOf(int, int)
     */
    @Contract("_, _ -> new")
    public @NotNull IntegerValue lastIndexOf(final char ch, final int fromIndex) {
        return IntegerValue.of(value.lastIndexOf(ch, fromIndex));
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring.  The last occurrence of the empty string ""
     * is considered to occur at the index value {@code this.length()}.
     * <p>The returned index is the largest value {@code k} for which:
     * <pre>{@code
     * this.startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     * @param str the substring to search for.
     * @return the index of the last occurrence of the specified substring,
     * or {@code -1} if there is no such occurrence.
     * @see String#lastIndexOf(String)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue lastIndexOf(final String str) {
        return IntegerValue.of(value.lastIndexOf(str));
    }

    /**
     * Returns the index within this string of the last occurrence of the
     * specified substring, searching backward starting at the specified index.
     * <p>The returned index is the largest value {@code k} for which:
     * <pre>{@code
     *     k <= Math.min(fromIndex, this.length()) &&
     *                   this.startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     * @param str       the substring to search for.
     * @param fromIndex the index to start the search from.
     * @return the index of the last occurrence of the specified substring,
     * searching backward from the specified index,
     * or {@code -1} if there is no such occurrence.
     * @see String#lastIndexOf(String, int)
     */
    @Contract("_, _ -> new")
    public @NotNull IntegerValue lastIndexOf(final String str, final int fromIndex) {
        return IntegerValue.of(value.lastIndexOf(str, fromIndex));
    }

    /**
     * Returns a string that is a substring of this string. The
     * substring begins with the character at the specified index and
     * extends to the end of this string. <p>
     * Examples:
     * <blockquote><pre>
     * "unhappy".substring(2) returns "happy"
     * "Harbison".substring(3) returns "bison"
     * "emptiness".substring(9) returns "" (an empty string)
     * </pre></blockquote>
     * @param      beginIndex   the beginning index, inclusive.
     * @return     the specified substring.
     * @throws     IndexOutOfBoundsException  if
     *             {@code beginIndex} is negative or larger than the
     *             length of this {@code String} object.
     * @see String#substring(int)
     */
    @Contract("_ -> new")
    public @NotNull StringValue substring(final int beginIndex) {
        return StringValue.of(value.substring(beginIndex));
    }

    /**
     * Returns a string that is a substring of this string. The
     * substring begins at the specified {@code beginIndex} and
     * extends to the character at index {@code endIndex - 1}.
     * Thus, the length of the substring is {@code endIndex-beginIndex}.
     * <p>
     * Examples:
     * <blockquote><pre>
     * "hamburger".substring(4, 8) returns "urge"
     * "smiles".substring(1, 5) returns "mile"
     * </pre></blockquote>
     * @param      beginIndex   the beginning index, inclusive.
     * @param      endIndex     the ending index, exclusive.
     * @return     the specified substring.
     * @throws     IndexOutOfBoundsException  if the
     *             {@code beginIndex} is negative, or
     *             {@code endIndex} is larger than the length of
     *             this {@code String} object, or
     *             {@code beginIndex} is larger than
     *             {@code endIndex}.
     * @see String#substring(int, int)
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue substring(final int beginIndex, final int endIndex) {
        return StringValue.of(value.substring(beginIndex, endIndex));
    }

    /**
     * Returns a character sequence that is a subsequence of this string.
     * <p> An invocation of this method of the form
     * <blockquote><pre>
     * str.subSequence(begin,&nbsp;end)</pre></blockquote>
     * behaves in exactly the same way as the invocation
     * <blockquote><pre>
     * str.substring(begin,&nbsp;end)</pre></blockquote>
     * @apiNote
     * This method is defined so that the {@code String} class can implement
     * the {@link CharSequence} interface.
     * @param   beginIndex   the begin index, inclusive.
     * @param   endIndex     the end index, exclusive.
     * @return  the specified subsequence.
     * @throws  IndexOutOfBoundsException
     *          if {@code beginIndex} or {@code endIndex} is negative,
     *          if {@code endIndex} is greater than {@code length()},
     *          or if {@code beginIndex} is greater than {@code endIndex}
     * @see String#subSequence(int, int)
     */
    @Contract(pure = true)
    public @NotNull CharSequence subSequence(final int beginIndex, final int endIndex) {
        return value.subSequence(beginIndex, endIndex);
    }

    /**
     * Concatenates the specified string to the end of this string.
     * <p>
     * If the length of the argument string is {@code 0}, then this
     * {@code String} object is returned. Otherwise, a
     * {@code String} object is returned that represents a character
     * sequence that is the concatenation of the character sequence
     * represented by this {@code String} object and the character
     * sequence represented by the argument string.<p>
     * Examples:
     * <blockquote><pre>
     * "cares".concat("s") returns "caress"
     * "to".concat("get").concat("her") returns "together"
     * </pre></blockquote>
     * @param   str   the {@code String} that is concatenated to the end
     *                of this {@code String}.
     * @return  a string that represents the concatenation of this object's
     *          characters followed by the string argument's characters.
     * @see String#concat(String)
     */
    @Contract("_ -> new")
    public @NotNull StringValue concat(final String str) {
        return StringValue.of(value + str);
    }

    /**
     * Returns a string resulting from replacing all occurrences of
     * {@code oldChar} in this string with {@code newChar}.
     * <p>
     * If the character {@code oldChar} does not occur in the
     * character sequence represented by this {@code String} object,
     * then a reference to this {@code String} object is returned.
     * Otherwise, a {@code String} object is returned that
     * represents a character sequence identical to the character sequence
     * represented by this {@code String} object, except that every
     * occurrence of {@code oldChar} is replaced by an occurrence
     * of {@code newChar}.
     * <p>
     * Examples:
     * <blockquote><pre>
     * "mesquite in your cellar".replace('e', 'o')
     *         returns "mosquito in your collar"
     * "the war of baronets".replace('r', 'y')
     *         returns "the way of bayonets"
     * "sparring with a purple porpoise".replace('p', 't')
     *         returns "starring with a turtle tortoise"
     * "JonL".replace('q', 'x') returns "JonL" (no change)
     * </pre></blockquote>
     * @param   oldChar   the old character.
     * @param   newChar   the new character.
     * @return  a string derived from this string by replacing every
     *          occurrence of {@code oldChar} with {@code newChar}.
     * @see String#replace(char, char)
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue replace(final char oldChar, final char newChar) {
        return StringValue.of(value.replace(oldChar, newChar));
    }

    /**
     * Tells whether this string matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a>.
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .matches(}<i>regex</i>{@code )} yields exactly the
     * same result as the expression
     * <blockquote>
     * {@link java.util.regex.Pattern}.{@link java.util.regex.Pattern#matches(String,CharSequence)
     * matches(<i>regex</i>, <i>str</i>)}
     * </blockquote>
     * @param   regex
     *          the regular expression to which this string is to be matched
     * @return  {@code true} if, and only if, this string matches the
     *          given regular expression
     * @throws PatternSyntaxException
     *          if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     * @see String#matches(String)
     */
    public boolean matches(final String regex) {
        return value.matches(regex);
    }

    /**
     * Returns true if and only if this string contains the specified
     * sequence of char values.
     * @param charSeq the sequence to search for
     * @return true if this string contains {@code s}, false otherwise
     * @see String#contains(CharSequence)
     */
    public boolean contains(final CharSequence charSeq) {
        return value.contains(charSeq);
    }

    /**
     * Removes all occurrences of a character from within the value.
     *
     * <pre>
     * remove(null, *)       = null
     * remove("", *)         = ""
     * remove("queued", 'u') = "qeed"
     * remove("queued", 'z') = "queued"
     * </pre>
     *
     * @param remove  the char to search for and remove, may be null
     * @return the substring with the char removed if found,
     *  {@code null} if null String input
     */
    public StringValue remove(final char remove) {
        if (isEmpty() || value.indexOf(remove) == INDEX_NOT_FOUND) {
            return this;
        }
        final char[] chars = value.toCharArray();
        int pos = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != remove) {
                //noinspection ValueOfIncrementOrDecrementUsed
                chars[pos++] = chars[i];
            }
        }
        return StringValue.of(new String(chars, 0, pos));
    }

    /**
     * Removes all occurrences of a substring from within the value.
     *
     * <p>A {@code null} remove string will return the source string.
     * An empty ("") remove string will return the source string.</p>
     *
     * <pre>
     * remove(null, *)        = null
     * remove("", *)          = ""
     * remove(*, null)        = *
     * remove(*, "")          = *
     * remove("queued", "ue") = "qd"
     * remove("queued", "zz") = "queued"
     * </pre>
     *
     * @param remove  the String to search for and remove, may be null
     * @return the substring with the string removed if found,
     *  {@code null} if null String input
     * @since 2.1
     */
    public StringValue remove(final String remove) {
        if (isEmpty() || remove.isEmpty()) {
            return this;
        }
        return replace(remove, EMPTY.get(), -1);
    }

    /**
     * Replaces the first substring of this string that matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a> with the
     * given replacement.
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .replaceFirst(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * yields exactly the same result as the expression
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile(String) compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#matcher(CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceFirst(String) replaceFirst}(<i>repl</i>)
     * </code>
     * </blockquote>
     *
     *<p>
     * Note that backslashes ({@code \}) and dollar signs ({@code $}) in the
     * replacement string may cause the results to be different from if it were
     * being treated as a literal replacement string; see
     * {@link java.util.regex.Matcher#replaceFirst}.
     * Use {@link java.util.regex.Matcher#quoteReplacement} to suppress the special
     * meaning of these characters, if desired.
     * @param   regex
     *          the regular expression to which this string is to be matched
     * @param   replacement
     *          the string to be substituted for the first match
     * @return  The resulting {@code String}
     * @throws  PatternSyntaxException
     *          if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     * @see String#replaceFirst(String, String)
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue replaceFirst(final String regex, final String replacement) {
        return StringValue.of(value.replaceFirst(regex, replacement));
    }

    /**
     * Replaces each substring of this string that matches the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a> with the
     * given replacement.
     * <p> An invocation of this method of the form
     * <i>str</i>{@code .replaceAll(}<i>regex</i>{@code ,} <i>repl</i>{@code )}
     * yields exactly the same result as the expression
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile(String) compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#matcher(CharSequence) matcher}(<i>str</i>).{@link
     * java.util.regex.Matcher#replaceAll(String) replaceAll}(<i>repl</i>)
     * </code>
     * </blockquote>
     *
     *<p>
     * Note that backslashes ({@code \}) and dollar signs ({@code $}) in the
     * replacement string may cause the results to be different than if it were
     * being treated as a literal replacement string; see
     * {@link java.util.regex.Matcher#replaceAll Matcher.replaceAll}.
     * Use {@link java.util.regex.Matcher#quoteReplacement} to suppress the special
     * meaning of these characters, if desired.
     * @param   regex
     *          the regular expression to which this string is to be matched
     * @param   replacement
     *          the string to be substituted for each match
     * @return  The resulting {@code String}
     * @throws  PatternSyntaxException
     *          if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     * @see String#replaceAll(String, String)
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue replaceAll(final String regex, final String replacement) {
        return StringValue.of(value.replaceAll(regex, replacement));
    }

    /**
     * Replaces each substring of this string that matches the literal target
     * sequence with the specified literal replacement sequence. The
     * replacement proceeds from the beginning of the string to the end, for
     * example, replacing "aa" with "b" in the string "aaa" will result in
     * "ba" rather than "ab".
     * @param  target The sequence of char values to be replaced
     * @param  replacement The replacement sequence of char values
     * @return  The resulting string
     * @see String#replace(CharSequence, CharSequence)
     */
    @Contract("_, _ -> new")
    public @NotNull StringValue replace(final CharSequence target, final CharSequence replacement) {
        return StringValue.of(value.replace(target, replacement));
    }

    /**
     * Replaces all occurrences of a String within the value.
     *
     * <pre>
     * replace(*, *) [value = ""]         = ""
     * replace(null, *) [value = "any"]   = "any"
     * replace(*, null) [value = "any"]   = "any"
     * replace("", *) [value = "any"]     = "any"
     * replace("a", null) [value = "aba"] = "aba"
     * replace("a", "") [value = "aba"]   = "b"
     * replace("a", "z") [value = "aba"]  = "zbz"
     * </pre>
     *
     * @see #replace(String searchString, String replacement, int max)
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public StringValue replace(final String searchString, final String replacement) {
        return replace(searchString, replacement, -1);
    }

    /**
     * Replaces a String with another String inside the value,
     * for the first {@code max} values of the search String.
     *
     * <pre>
     * replace(*, *, *) [value = ""]           = ""
     * replace(null, *, *) [value = "any"]     = "any"
     * replace(*, null, *) [value = "any"]     = "any"
     * replace("", *, *) [value = "any"]       = "any"
     * replace(*, *, 0) [value = "any"]        = "any"
     * replace("a", null, -1) [value = "abaa"] = "abaa"
     * replace("a", "", -1) [value = "abaa"]   = "b"
     * replace("a", "z", 0) [value = "abaa"]   = "abaa"
     * replace("a", "z", 1) [value = "abaa"]   = "zbaa"
     * replace("a", "z", 2) [value = "abaa"]   = "zbza"
     * replace("a", "z", -1) [value = "abaa"]  = "zbzz"
     * </pre>
     *
     * @param searchString  the String to search for, may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    public StringValue replace(final String searchString, final String replacement, final int max) {
        return replace(searchString, replacement, max, false);
    }

    /**
     * Replaces a String with another String inside the value,
     * for the first {@code max} values of the search String,
     * case-sensitively/insensitively based on {@code ignoreCase} value.
     *
     * <pre>
     * replace(*, *, *, false) [value = ""]           = ""
     * replace(null, *, *, false) [value = "any"]     = "any"
     * replace(*, null, *, false) [value = "any"]     = "any"
     * replace("", *, *, false) [value = "any"]       = "any"
     * replace(*, *, 0, false) [value = "any"]        = "any"
     * replace("a", null, -1, false) [value = "abaa"] = "abaa"
     * replace("a", "", -1, false) [value = "abaa"]   = "b"
     * replace("a", "z", 0, false) [value = "abaa"]   = "abaa"
     * replace("A", "z", 1, false) [value = "abaa"]   = "abaa"
     * replace("A", "z", 1, true) [value = "abaa"]    = "zbaa"
     * replace("a", "z", 2, true) [value = "abAa"]    = "zbza"
     * replace("a", "z", -1, true) [value = "abAa"]   = "zbzz"
     * </pre>
     *
     * @param searchString  the String to search for (case-insensitive), may be null
     * @param replacement  the String to replace it with, may be null
     * @param max  maximum number of values to replace, or {@code -1} if no maximum
     * @param ignoreCase if true replace is case-insensitive, otherwise case-sensitive
     * @return the text with any replacements processed,
     *  {@code null} if null String input
     */
    @SuppressWarnings({"OverlyComplexMethod", "SameParameterValue"})
    private StringValue replace(final String searchString, final String replacement,
                                final int max, final boolean ignoreCase) {
        String searchString_ = searchString;
        int max_ = max;
        if (isEmpty() || searchString_.isEmpty() || replacement == null || max == 0) {
            return this;
        }
        if (ignoreCase) {
            searchString_ = searchString_.toLowerCase(Locale.getDefault());
        }
        int start = 0;
        IntegerValue end = ignoreCase ? indexOfIgnoreCase(searchString_, start) : indexOf(searchString_, start);
        if (Objects.equals(INDEX_NOT_FOUND, end.get())) {
            return this;
        }
        final int replLength = searchString_.length();
        int increase = Math.max(replacement.length() - replLength, 0);
        increase *= max < 0 ? 16 : Math.min(max, 64);
        final StringBuilder buf = new StringBuilder(value.length() + increase);
        while (!Objects.equals(INDEX_NOT_FOUND, end.get())) {
            buf.append(value).append(replacement);
            start = end.get() + replLength;
            //noinspection ValueOfIncrementOrDecrementUsed
            if (--max_ == 0) {
                break;
            }
            end = ignoreCase ? indexOfIgnoreCase(searchString_, start) : indexOf(searchString_, start);
        }
        buf.append(value, start, value.length());
        return StringValue.of(buf.toString());
    }

    /**
     * Splits this string around matches of the given
     * <a href="../util/regex/Pattern.html#sum">regular expression</a>.
     * <p> The array returned by this method contains each substring of this
     * string that is terminated by another substring that matches the given
     * expression or is terminated by the end of the string.  The substrings in
     * the array are in the order in which they occur in this string.  If the
     * expression does not match any part of the input then the resulting array
     * has just one element, namely this string.
     * <p> When there is a positive-width match at the beginning of this
     * string then an empty leading substring is included at the beginning
     * of the resulting array. A zero-width match at the beginning however
     * never produces such empty leading substring.
     * <p> The {@code limit} parameter controls the number of times the
     * pattern is applied and therefore affects the length of the resulting
     * array.
     * <ul>
     *    <li><p>
     *    If the <i>limit</i> is positive then the pattern will be applied
     *    at most <i>limit</i>&nbsp;-&nbsp;1 times, the array's length will be
     *    no greater than <i>limit</i>, and the array's last entry will contain
     *    all input beyond the last matched delimiter.</p></li>
     *    <li><p>
     *    If the <i>limit</i> is zero then the pattern will be applied as
     *    many times as possible, the array can have any length, and trailing
     *    empty strings will be discarded.</p></li>
     *    <li><p>
     *    If the <i>limit</i> is negative then the pattern will be applied
     *    as many times as possible and the array can have any length.</p></li>
     * </ul>
     * <p> The string {@code "boo:and:foo"}, for example, yields the
     * following results with these parameters:
     * <blockquote><table class="plain">
     * <caption style="display:none">Split example showing regex, limit, and result</caption>
     * <thead>
     * <tr>
     *     <th scope="col">Regex</th>
     *     <th scope="col">Limit</th>
     *     <th scope="col">Result</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr><th scope="row" rowspan="3" style="font-weight:normal">:</th>
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">2</th>
     *     <td>{@code { "boo", "and:foo" }}</td></tr>
     * <tr><!-- : -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">5</th>
     *     <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><!-- : -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">-2</th>
     *     <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><th scope="row" rowspan="3" style="font-weight:normal">o</th>
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">5</th>
     *     <td>{@code { "b", "", ":and:f", "", "" }}</td></tr>
     * <tr><!-- o -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">-2</th>
     *     <td>{@code { "b", "", ":and:f", "", "" }}</td></tr>
     * <tr><!-- o -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">0</th>
     *     <td>{@code { "b", "", ":and:f" }}</td></tr>
     * </tbody>
     * </table></blockquote>
     * <p> An invocation of this method of the form
     * <i>str.</i>{@code split(}<i>regex</i>{@code ,}&nbsp;<i>n</i>{@code )}
     * yields the same result as the expression
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile(String) compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#split(CharSequence,int) split}(<i>str</i>,&nbsp;<i>n</i>)
     * </code>
     * </blockquote>
     * @param  regex
     *         the delimiting regular expression
     * @param  limit
     *         the result threshold, as described above
     * @return  the array of strings computed by splitting this string
     *          around matches of the given regular expression
     * @throws  PatternSyntaxException
     *          if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     * @see String#split(String, int)
     */
    public StringValue @NotNull [] split(final String regex, final int limit) {
        return Arrays.stream(value.split(regex, limit))
                .map(StringValue::of).toArray(StringValue[]::new);
    }

    /**
     * Splits this string around matches of the given regular expression and
     * returns both the strings and the matching delimiters.
     * <p> The array returned by this method contains each substring of this
     * string that is terminated by another substring that matches the given
     * expression or is terminated by the end of the string.
     * Each substring is immediately followed by the subsequence (the delimiter)
     * that matches the given expression, <em>except</em> for the last
     * substring, which is not followed by anything.
     * The substrings in the array and the delimiters are in the order in which
     * they occur in the input.
     * If the expression does not match any part of the input then the resulting
     * array has just one element, namely this string.
     * <p> When there is a positive-width match at the beginning of this
     * string then an empty leading substring is included at the beginning
     * of the resulting array. A zero-width match at the beginning however
     * never produces such empty leading substring nor the empty delimiter.
     * <p> The {@code limit} parameter controls the number of times the
     * pattern is applied and therefore affects the length of the resulting
     * array.
     * <ul>
     *    <li> If the <i>limit</i> is positive then the pattern will be applied
     *    at most <i>limit</i>&nbsp;-&nbsp;1 times, the array's length will be
     *    no greater than 2 &times; <i>limit</i> - 1, and the array's last
     *    entry will contain all input beyond the last matched delimiter.</li>
     *    <li> If the <i>limit</i> is zero then the pattern will be applied as
     *    many times as possible, the array can have any length, and trailing
     *    empty strings will be discarded.</li>
     *    <li> If the <i>limit</i> is negative then the pattern will be applied
     *    as many times as possible and the array can have any length.</li>
     * </ul>
     * <p> The input {@code "boo:::and::foo"}, for example, yields the following
     * results with these parameters:
     * <table class="plain" style="margin-left:2em;">
     * <caption style="display:none">Split example showing regex, limit, and result</caption>
     * <thead>
     * <tr>
     *     <th scope="col">Regex</th>
     *     <th scope="col">Limit</th>
     *     <th scope="col">Result</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr><th scope="row" rowspan="3" style="font-weight:normal">:+</th>
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">2</th>
     *     <td>{@code { "boo", ":::", "and::foo" }}</td></tr>
     * <tr><!-- : -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">5</th>
     *     <td>{@code { "boo", ":::", "and", "::", "foo" }}</td></tr>
     * <tr><!-- : -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">-1</th>
     *     <td>{@code { "boo", ":::", "and", "::", "foo" }}</td></tr>
     * <tr><th scope="row" rowspan="3" style="font-weight:normal">o</th>
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">5</th>
     *     <td>{@code { "b", "o", "", "o", ":::and::f", "o", "", "o", "" }}</td></tr>
     * <tr><!-- o -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">-1</th>
     *     <td>{@code { "b", "o", "", "o", ":::and::f", "o", "", "o", "" }}</td></tr>
     * <tr><!-- o -->
     *     <th scope="row" style="font-weight:normal; text-align:right; padding-right:1em">0</th>
     *     <td>{@code { "b", "o", "", "o", ":::and::f", "o", "", "o" }}</td></tr>
     * </tbody>
     * </table>
     * @apiNote An invocation of this method of the form
     * <i>str.</i>{@code splitWithDelimiters(}<i>regex</i>{@code ,}&nbsp;<i>n</i>{@code )}
     * yields the same result as the expression
     * <blockquote>
     * <code>
     * {@link java.util.regex.Pattern}.{@link
     * java.util.regex.Pattern#compile(String) compile}(<i>regex</i>).{@link
     * java.util.regex.Pattern#splitWithDelimiters(CharSequence,int) splitWithDelimiters}(<i>str</i>,&nbsp;<i>n</i>)
     * </code>
     * </blockquote>
     * @param  regex
     *         the delimiting regular expression
     * @param  limit
     *         the result threshold, as described above
     * @return  the array of strings computed by splitting this string
     *          around matches of the given regular expression, alternating
     *          substrings and matching delimiters
     * @see String#splitWithDelimiters(String, int)
     */
    public StringValue @NotNull [] splitWithDelimiters(final String regex, final int limit) {
        return Arrays.stream(value.splitWithDelimiters(regex, limit))
                .map(StringValue::of).toArray(StringValue[]::new);
    }

    /**
     * Splits this string around matches of the given <a
     * href="../util/regex/Pattern.html#sum">regular expression</a>.
     * <p> This method works as if by invoking the two-argument {@link
     * #split(String, int) split} method with the given expression and a limit
     * argument of zero.  Trailing empty strings are therefore not included in
     * the resulting array.
     * <p> The string {@code "boo:and:foo"}, for example, yields the following
     * results with these expressions:
     * <blockquote><table class="plain">
     * <caption style="display:none">Split examples showing regex and result</caption>
     * <thead>
     * <tr>
     *  <th scope="col">Regex</th>
     *  <th scope="col">Result</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr><th scope="row" style="text-weight:normal">:</th>
     *     <td>{@code { "boo", "and", "foo" }}</td></tr>
     * <tr><th scope="row" style="text-weight:normal">o</th>
     *     <td>{@code { "b", "", ":and:f" }}</td></tr>
     * </tbody>
     * </table></blockquote>
     * @param  regex
     *         the delimiting regular expression
     * @return  the array of strings computed by splitting this string
     *          around matches of the given regular expression
     * @throws  PatternSyntaxException
     *          if the regular expression's syntax is invalid
     * @see java.util.regex.Pattern
     * @see String#split(String)
     */
    public StringValue @NotNull [] split(final String regex) {
        return Arrays.stream(value.split(regex))
                .map(StringValue::of).toArray(StringValue[]::new);
    }

    /**
     * Converts all the characters in this {@code String} to lower
     * case using the rules of the default locale. This method is equivalent to
     * {@code toLowerCase(Locale.getDefault())}.
     * @apiNote This method is locale sensitive, and may produce unexpected
     * results if used for strings that are intended to be interpreted locale
     * independently.
     * Examples are programming language identifiers, protocol keys, and HTML
     * tags.
     * For instance, {@code "TITLE".toLowerCase()} in a Turkish locale
     * returns {@code "t\u0131tle"}, where '\u0131' is the
     * LATIN SMALL LETTER DOTLESS I character.
     * To obtain correct results for locale insensitive strings, use
     * {@code toLowerCase(Locale.ROOT)}.
     * @return  the {@code String}, converted to lowercase.
     * @see String#toLowerCase()
     * @see String#toLowerCase(Locale)
     * @see String#toUpperCase()
     * @see String#toUpperCase(Locale)
     */
    @Contract(" -> new")
    public @NotNull StringValue toLowerCase() {
        return StringValue.of(value.toLowerCase(Locale.getDefault()));
    }

    /**
     * Converts all the characters in this {@code String} to lower
     * case using the rules of the given {@code Locale}.  Case mapping is based
     * on the Unicode Standard version specified by the {@link Character Character}
     * class. Since case mappings are not always 1:1 char mappings, the resulting {@code String}
     * and this {@code String} may differ in length.
     * <p>
     * Examples of lowercase mappings are in the following table:
     * <table class="plain">
     * <caption style="display:none">Lowercase mapping examples showing language code of locale, upper case, lower case, and description</caption>
     * <thead>
     * <tr>
     *   <th scope="col">Language Code of Locale</th>
     *   <th scope="col">Upper Case</th>
     *   <th scope="col">Lower Case</th>
     *   <th scope="col">Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">&#92;u0130</th>
     *   <td>&#92;u0069</td>
     *   <td>capital letter I with dot above -&gt; small letter i</td>
     * </tr>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">&#92;u0049</th>
     *   <td>&#92;u0131</td>
     *   <td>capital letter I -&gt; small letter dotless i </td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">French Fries</th>
     *   <td>french fries</td>
     *   <td>lowercased all chars in String</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">
     *       &Iota;&Chi;&Theta;&Upsilon;&Sigma;</th>
     *   <td>&iota;&chi;&theta;&upsilon;&sigma;</td>
     *   <td>lowercased all chars in String</td>
     * </tr>
     * </tbody>
     * </table>
     * @param locale use the case transformation rules for this locale
     * @return the {@code String}, converted to lowercase.
     * @see String#toLowerCase()
     * @see String#toLowerCase(Locale)
     * @see String#toUpperCase()
     * @see String#toUpperCase(Locale)
     */
    @Contract("_ -> new")
    public @NotNull StringValue toLowerCase(final Locale locale) {
        return StringValue.of(value.toLowerCase(locale));
    }

    /**
     * Converts all the characters in this {@code String} to upper
     * case using the rules of the default locale. This method is equivalent to
     * {@code toUpperCase(Locale.getDefault())}.
     *
     * @apiNote This method is locale sensitive, and may produce unexpected
     * results if used for strings that are intended to be interpreted locale
     * independently.
     * Examples are programming language identifiers, protocol keys, and HTML
     * tags.
     * For instance, {@code "title".toUpperCase()} in a Turkish locale
     * returns {@code "T\u0130TLE"}, where '\u0130' is the
     * LATIN CAPITAL LETTER I WITH DOT ABOVE character.
     * To obtain correct results for locale insensitive strings, use
     * {@code toUpperCase(Locale.ROOT)}.
     * @return  the {@code String}, converted to uppercase.
     * @see String#toLowerCase()
     * @see String#toLowerCase(Locale)
     * @see String#toUpperCase()
     * @see String#toUpperCase(Locale)
     */
    @Contract(" -> new")
    public @NotNull StringValue toUpperCase() {
        return StringValue.of(value.toUpperCase(Locale.getDefault()));
    }

    /**
     * Converts all the characters in this {@code String} to upper
     * case using the rules of the given {@code Locale}. Case mapping is based
     * on the Unicode Standard version specified by the {@link Character Character}
     * class. Since case mappings are not always 1:1 char mappings, the resulting {@code String}
     * and this {@code String} may differ in length.
     * <p>
     * Examples of locale-sensitive and 1:M case mappings are in the following table:
     * <table class="plain">
     * <caption style="display:none">Examples of locale-sensitive and 1:M case mappings. Shows Language code of locale, lower case, upper case, and description.</caption>
     * <thead>
     * <tr>
     *   <th scope="col">Language Code of Locale</th>
     *   <th scope="col">Lower Case</th>
     *   <th scope="col">Upper Case</th>
     *   <th scope="col">Description</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">&#92;u0069</th>
     *   <td>&#92;u0130</td>
     *   <td>small letter i -&gt; capital letter I with dot above</td>
     * </tr>
     * <tr>
     *   <td>tr (Turkish)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">&#92;u0131</th>
     *   <td>&#92;u0049</td>
     *   <td>small letter dotless i -&gt; capital letter I</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">&#92;u00df</th>
     *   <td>&#92;u0053 &#92;u0053</td>
     *   <td>small letter sharp s -&gt; two letters: SS</td>
     * </tr>
     * <tr>
     *   <td>(all)</td>
     *   <th scope="row" style="font-weight:normal; text-align:left">Fahrvergn&uuml;gen</th>
     *   <td>FAHRVERGN&Uuml;GEN</td>
     *   <td></td>
     * </tr>
     * </tbody>
     * </table>
     * @param locale use the case transformation rules for this locale
     * @return the {@code String}, converted to uppercase.
     * @see String#toLowerCase()
     * @see String#toLowerCase(Locale)
     * @see String#toUpperCase()
     * @see String#toUpperCase(Locale)
     */
    @Contract("_ -> new")
    public @NotNull StringValue toUpperCase(final Locale locale) {
        return StringValue.of(value.toUpperCase(locale));
    }

    /**
     * Returns a string whose value is this string, with all leading
     * and trailing space removed, where space is defined
     * as any character whose codepoint is less than or equal to
     * {@code 'U+0020'} (the space character).
     * <p>
     * If this {@code String} object represents an empty character
     * sequence, or the first and last characters of character sequence
     * represented by this {@code String} object both have codes
     * that are not space (as defined above), then a
     * reference to this {@code String} object is returned.
     * <p>
     * Otherwise, if all characters in this string are space (as
     * defined above), then a  {@code String} object representing an
     * empty string is returned.
     * <p>
     * Otherwise, let <i>k</i> be the index of the first character in the
     * string whose code is not a space (as defined above) and let
     * <i>m</i> be the index of the last character in the string whose code
     * is not a space (as defined above). A {@code String}
     * object is returned, representing the substring of this string that
     * begins with the character at index <i>k</i> and ends with the
     * character at index <i>m</i>-that is, the result of
     * {@code this.substring(k, m + 1)}.
     * <p>
     * This method may be used to trim space (as defined above) from
     * the beginning and end of a string.
     * @return  a string whose value is this string, with all leading
     *          and trailing space removed, or this string if it
     *          has no leading or trailing space.
     */
    @Contract(" -> new")
    public @NotNull StringValue trim() {
        return StringValue.of(value.trim());
    }

    /**
     * Returns a string whose value is this string, with all leading
     * {@linkplain Character#isWhitespace(int) white space} removed.
     * <p>
     * If this {@code String} object represents an empty string,
     * or if all code points in this string are
     * {@linkplain Character#isWhitespace(int) white space}, then an empty string
     * is returned.
     * <p>
     * Otherwise, returns a substring of this string beginning with the first
     * code point that is not a {@linkplain Character#isWhitespace(int) white space}
     * up to and including the last code point of this string.
     * <p>
     * This method may be used to trim
     * {@linkplain Character#isWhitespace(int) white space} from
     * the beginning of a string.
     *
     * @return  a string whose value is this string, with all leading white
     *          space removed
     *
     * @see Character#isWhitespace(int)
     * @see String#stripLeading()
     */
    @Contract(" -> new")
    public @NotNull StringValue stripLeading() {
        return StringValue.of(value.stripLeading());
    }

    /**
     * Returns a string whose value is this string, with all trailing
     * {@linkplain Character#isWhitespace(int) white space} removed.
     * <p>
     * If this {@code String} object represents an empty string,
     * or if all characters in this string are
     * {@linkplain Character#isWhitespace(int) white space}, then an empty string
     * is returned.
     * <p>
     * Otherwise, returns a substring of this string beginning with the first
     * code point of this string up to and including the last code point
     * that is not a {@linkplain Character#isWhitespace(int) white space}.
     * <p>
     * This method may be used to trim
     * {@linkplain Character#isWhitespace(int) white space} from
     * the end of a string.
     *
     * @return  a string whose value is this string, with all trailing white
     *          space removed
     *
     * @see Character#isWhitespace(int)
     * @see String#stripTrailing()
     */
    @Contract(" -> new")
    public @NotNull StringValue stripTrailing() {
        return StringValue.of(value.stripTrailing());
    }

    /**
     * Returns a stream of lines extracted from this string,
     * separated by line terminators.
     * <p>
     * A <i>line terminator</i> is one of the following:
     * a line feed character {@code "\n"} (U+000A),
     * a carriage return character {@code "\r"} (U+000D),
     * or a carriage return followed immediately by a line feed
     * {@code "\r\n"} (U+000D U+000A).
     * <p>
     * A <i>line</i> is either a sequence of zero or more characters
     * followed by a line terminator, or it is a sequence of one or
     * more characters followed by the end of the string. A
     * line does not include the line terminator.
     * <p>
     * The stream returned by this method contains the lines from
     * this string in the order in which they occur.
     *
     * @apiNote This definition of <i>line</i> implies that an empty
     *          string has zero lines and that there is no empty line
     *          following a line terminator at the end of a string.
     *
     * @implNote This method provides better performance than
     *           split("\R") by supplying elements lazily and
     *           by faster search of new line terminators.
     *
     * @return  the stream of lines extracted from this string
     * @see String#lines()
     */
    public @NotNull Stream<StringValue> lines() {
        return value.lines().map(StringValue::of);
    }

    /**
     * Adjusts the indentation of each line of this string based on the value of
     * {@code n}, and normalizes line termination characters.
     * <p>
     * This string is conceptually separated into lines using
     * {@link String#lines()}. Each line is then adjusted as described below
     * and then suffixed with a line feed {@code "\n"} (U+000A). The resulting
     * lines are then concatenated and returned.
     * <p>
     * If {@code n > 0} then {@code n} spaces (U+0020) are inserted at the
     * beginning of each line.
     * <p>
     * If {@code n < 0} then up to {@code n}
     * {@linkplain Character#isWhitespace(int) white space characters} are removed
     * from the beginning of each line. If a given line does not contain
     * sufficient white space then all leading
     * {@linkplain Character#isWhitespace(int) white space characters} are removed.
     * Each white space character is treated as a single character. In
     * particular, the tab character {@code "\t"} (U+0009) is considered a
     * single character; it is not expanded.
     * <p>
     * If {@code n == 0} then the line remains unchanged. However, line
     * terminators are still normalized.
     *
     * @param n  number of leading
     *           {@linkplain Character#isWhitespace(int) white space characters}
     *           to add or remove
     *
     * @return string with indentation adjusted and line endings normalized
     *
     * @see String#lines()
     * @see String#isBlank()
     * @see Character#isWhitespace(int)
     * @see String#indent(int)
     */
    @Contract("_ -> new")
    public @NotNull StringValue indent(final int n) {
        return StringValue.of(value.indent(n));
    }

    /**
     * Removes the last character from the value.
     * @return the value with last character removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue removeLastCharacter() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.substring(0, value.length() - 1));
    }

    /**
     * Removes the specified number of characters removed from the end of the value.
     * @param number the number of characters to remove
     * @return the value with the specified number of characters removed from the end
     * @throws IllegalArgumentException if the value is null
     */
    @Contract("_ -> new")
    public @NotNull StringValue removeLastCharacters(final int number) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.substring(0, value.length() - number));
    }

    /**
     * Removes the first character from the value.
     * @return the value with first character removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue removeFirstCharacter() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.substring(1));
    }

    /**
     * Removes the specified number of characters removed from the beginning of the value.
     * @param number the number of characters to remove
     * @return the value with the specified number of characters removed from the beginning
     * @throws IllegalArgumentException if the value is null
     */
    @Contract("_ -> new")
    public @NotNull StringValue removeFirstCharacters(final int number) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.substring(number));
    }

    /**
     * Removes all characters except alphanumeric from the value.
     * @return the value with all characters except alphanumeric removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue removeAllSpecialCharacters() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.replaceAll(RegExPatterns.SPECIAL_CHARS.getRegex(), ""));
    }

    /**
     * Removes all alphanumeric characters from the value.
     * @return the value with all alphanumeric characters removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue removeAllAlphanumericCharacters() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.replaceAll(RegExPatterns.ALPHANUMERIC.getRegex(), ""));
    }

    /**
     * Removes all letters from the value.
     * @return the value with all letters removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue removeAllLetters() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.replaceAll(RegExPatterns.ALPHA.getRegex(), ""));
    }

    /**
     * Removes all numbers from the value.
     * @return the value with all numbers removed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue removeAllNumbers() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.replaceAll(RegExPatterns.NUMERIC.getRegex(), ""));
    }

    /**
     * Reverses the characters in the value.
     * @return the value with characters reversed
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue reverse() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(new StringBuilder(value).reverse().toString());
    }

    /**
     * Returns the first part of the value, up until the character c. If c is not found in the
     * value the whole string is returned.
     * @param c the character to find
     * @return the edited value
     * @throws IllegalArgumentException if the value is null
     */
    @Contract("_ -> new")
    public @NotNull StringValue leftOf(final char c) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        final var index = value.indexOf(c);
        if (index >= 0) return StringValue.of(value.substring(0, index));
        return this;
    }

    /**
     * Returns right part of the value, after the character c. If c is not found in the
     * value the whole string is returned.
     * @param c the character to find
     * @return the edited value
     * @throws IllegalArgumentException if the value is null
     */
    @Contract("_ -> new")
    public @NotNull StringValue rightOf(final char c) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        final var index = value.indexOf(c);
        if (index >= 0) return StringValue.of(value.substring(index + 1));
        return this;
    }

    /**
     * Returns first character in the value or empty string if the value is empty.
     * @return first character in the value
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue firstChar() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.length() > 1 ? value.substring(0, 1) : value);
    }

    /**
     * Returns last character in the value or empty string if the value is empty.
     * @return last character in the value
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue lastChar() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.length() > 1 ? value.substring(value.length() - 1, 1) : value);
    }

    /**
     * Returns first number of characters in the value or empty string if the value is empty.
     * @param number number of characters to retrieve
     * @return first number of characters in the value
     * @throws IllegalArgumentException if the value is null
     */
    @Contract("_ -> new")
    public @NotNull StringValue firstChars(final int number) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.length() < number ? value : value.substring(0, number));
    }

    /**
     * Returns last number of characters in the value or empty string if the value is empty.
     * @param number number of characters to retrieve
     * @return last number of characters in the value
     * @throws IllegalArgumentException if the value is null
     */
    @Contract("_ -> new")
    public @NotNull StringValue lastChars(final int number) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.length() < number ? value : value.substring(number + 1));
    }

    /**
     * Capitalizes all words in the value.
     * @return the value with all words capitalized
     * @throws IllegalArgumentException if the value is null or empty
     */
    @Contract(" -> new")
    public @NotNull StringValue toTitleCase() {
        checkArgumentNotNullOrEmpty(value, cannotBeNullOrEmpty("value"));
        final var words = value.trim().split(SPACE);
        return StringValue.of(Arrays.stream(words)
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1) + ' ')
                .collect(Collectors.joining())
                .trim());
    }

    /**
     * Checks if the value is a number that may be negative or
     * contain a decimal point using the default locale.
     * <p>
     *     This method is more robust than most isNumeric methods
     *     in other libraries because it checks for a maximum of one
     *     minus sign and checks for a maximum of one decimal point.
     *     It also does not use pattern matching or number parsing
     *     with exception checking thus increasing speed.
     * @return true if the value is a number
     */
    public boolean isNumeric() {
        return isNumeric(Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Checks if the value is a number that may be negative or
     * contain a decimal point using the specified locale.
     * <p>
     *     This method is more robust than most isNumeric methods
     *     in other libraries because it checks for a maximum of one
     *     minus sign and checks for a maximum of one decimal point.
     *     It also does not use pattern matching or number parsing
     *     with exception checking thus increasing speed.
     * @param locale the locale to use
     * @return true if the value is a number
     * @throws IllegalArgumentException if locale is null
     */
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public boolean isNumeric(final Locale locale) {
        checkArgumentNotNull(locale, cannotBeNull("locale"));
        //Check for null or blank string
        if(isBlank()) return false;

        //Retrieve the minus sign and decimal separator characters from the current Locale
        final char localeMinusSign = DecimalFormatSymbols.getInstance(locale).getMinusSign();
        final char localeDecimalSeparator = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();

        //Check if first character is a minus sign
        final boolean isNegative = value.charAt(0) == localeMinusSign;
        //Check if string is not just a minus sign
        if (isNegative && value.length() == 1) return false;

        boolean isDecimalSeparatorFound = false;

        //If the string has a minus sign ignore the first character
        final int startCharIndex = isNegative ? 1 : 0;

        //Check if each character is a number or a decimal separator
        //and make sure string only has a maximum of one decimal separator
        for (int i = startCharIndex; i < value.length(); i++) {
            if(!Character.isDigit(value.charAt(i))) {
                if(value.charAt(i) == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                } else return false;
            }
        }
        return true;
    }

    /**
     * Converts this StringValue to a DoubleValue.
     * @return this value as a DoubleValue
     */
    public @NotNull DoubleValue toDouble() {
        if(isNumeric()) {
            return DoubleValue.of(value);
        } else {
            throw new IllegalArgumentException("This StringValue is not a number.");
        }
    }

    /**
     * Converts this StringValue to a FloatValue.
     * @return this value as a FloatValue
     */
    public @NotNull FloatValue toFloat() {
        if(isNumeric()) {
            return FloatValue.of(value);
        } else {
            throw new IllegalArgumentException("This StringValue is not a number.");
        }
    }

    /**
     * Converts this StringValue to an IntegerValue.
     * @return this value as an IntegerValue
     */
    public @NotNull IntegerValue toInteger() {
        if(isNumeric()) {
            return IntegerValue.of(value);
        } else {
            throw new IllegalArgumentException("This StringValue is not a number.");
        }
    }

    /**
     * Converts this StringValue to a LongValue.
     * @return this value as a LongValue
     */
    public @NotNull LongValue toLong() {
        if(isNumeric()) {
            return LongValue.of(value);
        } else {
            throw new IllegalArgumentException("This StringValue is not a number.");
        }
    }

    /**
     * <p>
     * Unwraps the value from a character.
     * @param wrapChar the character used to unwrap
     * @throws IllegalArgumentException if the value or wrapChar are null
     * @return unwrapped String or the original value if it is not quoted properly with the wrapChar
     */
    public StringValue unwrap(final char wrapChar) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(wrapChar, cannotBeNull("wrapChar"));
        if (isEmpty() || wrapChar == CharUtils.NUL) {
            return this;
        }

        if (value.charAt(0) == wrapChar && value.charAt(value.length() - 1) == wrapChar) {
            final int startIndex = 0;
            final int endIndex = value.length() - 1;
            return StringValue.of(value.substring(startIndex + 1, endIndex));
        }

        return this;
    }

    /**
     * Returns the value with first char uppercase.
     * @return the value with first char uppercase
     * @throws IllegalArgumentException if the value is null
     */
    @Contract(" -> new")
    public @NotNull StringValue uppercaseFirst() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.length() > 1
                ? value.substring(0, 1).toUpperCase(Locale.getDefault()) + value.substring(1)
                : value.toUpperCase(Locale.getDefault()));
    }

    /**
     * Returns the value with first char uppercase.
     * @param locale use the case transformation rules for this locale
     * @return the value with first char uppercase
     * @throws IllegalArgumentException if the value or Locale are null
     */
    @Contract("_ -> new")
    public @NotNull StringValue uppercaseFirst(final Locale locale) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(locale, LOCALE_CANNOT_BE_NULL);
        return StringValue.of(value.length() > 1
                ? value.substring(0, 1).toUpperCase(locale) + value.substring(1)
                : value.toUpperCase(locale));
    }

    /**
     * Returns the value with first char lowercase.
     * @return the value with first char lowercase
     * @throws IllegalArgumentException if the value is null or empty
     */
    @Contract(" -> new")
    public @NotNull StringValue lowercaseFirst() {
        checkArgumentNotNull(value, cannotBeNull("value"));
        return StringValue.of(value.length() > 1
                ? value.substring(0, 1).toLowerCase(Locale.getDefault()) + value.substring(1)
                : value.toLowerCase(Locale.getDefault()));
    }

    /**
     * Returns the value with first char lowercase.
     * @param locale the locale to use for the conversion
     * @return the value with first char lowercase
     * @throws IllegalArgumentException if the value is null or empty
     */
    @Contract("_ -> new")
    public @NotNull StringValue lowercaseFirst(final Locale locale) {
        checkArgumentNotNull(value, cannotBeNull("value"));
        checkArgumentNotNull(locale, LOCALE_CANNOT_BE_NULL);
        return StringValue.of(value.length() > 1
                ? value.substring(0, 1).toLowerCase(locale) + value.substring(1)
                : value.toLowerCase(locale));
    }

    /**
     * <p>Checks if the value is empty (""), null or whitespace only.</p>
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     * @return {@code true} if the value is null, empty or whitespace only
     * @see Character#isWhitespace(int)
     * @see String#isBlank()
     */
    public boolean isBlank() {
        return (value == null || value.isEmpty())
                || IntStream.range(0, value.length())
                .allMatch(i -> Character.isWhitespace(value.charAt(i)));
    }

    /**
     * Returns a string whose value is this string, with incidental
     * {@linkplain Character#isWhitespace(int) white space} removed from
     * the beginning and end of every line.
     * <p>
     * Incidental {@linkplain Character#isWhitespace(int) white space}
     * is often present in a text block to align the content with the opening
     * delimiter. For example, in the following code, dots represent incidental
     * {@linkplain Character#isWhitespace(int) white space}:
     * <blockquote><pre>
     * String html = """
     * ..............&lt;html&gt;
     * ..............    &lt;body&gt;
     * ..............        &lt;p&gt;Hello, world&lt;/p&gt;
     * ..............    &lt;/body&gt;
     * ..............&lt;/html&gt;
     * ..............""";
     * </pre></blockquote>
     * This method treats the incidental
     * {@linkplain Character#isWhitespace(int) white space} as indentation to be
     * stripped, producing a string that preserves the relative indentation of
     * the content. Using | to visualize the start of each line of the string:
     * <blockquote><pre>
     * |&lt;html&gt;
     * |    &lt;body&gt;
     * |        &lt;p&gt;Hello, world&lt;/p&gt;
     * |    &lt;/body&gt;
     * |&lt;/html&gt;
     * </pre></blockquote>
     * First, the individual lines of this string are extracted. A <i>line</i>
     * is a sequence of zero or more characters followed by either a line
     * terminator or the end of the string.
     * If the string has at least one line terminator, the last line consists
     * of the characters between the last terminator and the end of the string.
     * Otherwise, if the string has no terminators, the last line is the start
     * of the string to the end of the string, in other words, the entire
     * string.
     * A line does not include the line terminator.
     * <p>
     * Then, the <i>minimum indentation</i> (min) is determined as follows:
     * <ul>
     *   <li><p>For each non-blank line (as defined by {@link String#isBlank()}),
     *   the leading {@linkplain Character#isWhitespace(int) white space}
     *   characters are counted.</p>
     *   </li>
     *   <li><p>The leading {@linkplain Character#isWhitespace(int) white space}
     *   characters on the last line are also counted even if
     *   {@linkplain String#isBlank() blank}.</p>
     *   </li>
     * </ul>
     * <p>The <i>min</i> value is the smallest of these counts.
     * <p>
     * For each {@linkplain String#isBlank() non-blank} line, <i>min</i> leading
     * {@linkplain Character#isWhitespace(int) white space} characters are
     * removed, and any trailing {@linkplain Character#isWhitespace(int) white
     * space} characters are removed. {@linkplain String#isBlank() Blank} lines
     * are replaced with the empty string.
     * <p>
     * Finally, the lines are joined into a new string, using the LF character
     * {@code "\n"} (U+000A) to separate lines.
     * @apiNote
     * This method's primary purpose is to shift a block of lines as far as
     * possible to the left, while preserving relative indentation. Lines
     * that were indented the least will thus have no leading
     * {@linkplain Character#isWhitespace(int) white space}.
     * The result will have the same number of line terminators as this string.
     * If this string ends with a line terminator then the result will end
     * with a line terminator.
     * @implSpec
     * This method treats all {@linkplain Character#isWhitespace(int) white space}
     * characters as having equal width. As long as the indentation on every
     * line is consistently composed of the same character sequences, then the
     * result will be as described above.
     * @return string with incidental indentation removed and line
     *         terminators normalized
     * @see String#lines()
     * @see String#isBlank()
     * @see String#indent(int)
     * @see Character#isWhitespace(int)
     * @see String#stripIndent()
     */
    @Contract(" -> new")
    public @NotNull StringValue stripIndent() {
        return StringValue.of(value.stripIndent());
    }

    /**
     * Returns a string whose value is this string, with escape sequences
     * translated as if in a string literal.
     * <p>
     * Escape sequences are translated as follows;
     * <table class="striped">
     *   <caption style="display:none">Translation</caption>
     *   <thead>
     *   <tr>
     *     <th scope="col">Escape</th>
     *     <th scope="col">Name</th>
     *     <th scope="col">Translation</th>
     *   </tr>
     *   </thead>
     *   <tbody>
     *   <tr>
     *     <th scope="row">{@code \b}</th>
     *     <td>backspace</td>
     *     <td>{@code U+0008}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \t}</th>
     *     <td>horizontal tab</td>
     *     <td>{@code U+0009}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \n}</th>
     *     <td>line feed</td>
     *     <td>{@code U+000A}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \f}</th>
     *     <td>form feed</td>
     *     <td>{@code U+000C}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \r}</th>
     *     <td>carriage return</td>
     *     <td>{@code U+000D}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \s}</th>
     *     <td>space</td>
     *     <td>{@code U+0020}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \"}</th>
     *     <td>double quote</td>
     *     <td>{@code U+0022}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \'}</th>
     *     <td>single quote</td>
     *     <td>{@code U+0027}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \\}</th>
     *     <td>backslash</td>
     *     <td>{@code U+005C}</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \0 - \377}</th>
     *     <td>octal escape</td>
     *     <td>code point equivalents</td>
     *   </tr>
     *   <tr>
     *     <th scope="row">{@code \<line-terminator>}</th>
     *     <td>continuation</td>
     *     <td>discard</td>
     *   </tr>
     *   </tbody>
     * </table>
     * @implNote
     * This method does <em>not</em> translate Unicode escapes such as "{@code \u2022}".
     * Unicode escapes are translated by the Java compiler when reading input characters and
     * are not part of the string literal specification.
     * @throws IllegalArgumentException when an escape sequence is malformed.
     * @return String with escape sequences translated.
     * @see String#translateEscapes()
     */
    @Contract(" -> new")
    public @NotNull StringValue translateEscapes() {
        return StringValue.of(value.translateEscapes());
    }

    /**
     * Compares this string to another string.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@code
     * String} object that represents the same sequence of characters as this
     * object.
     *
     * <p>For finer-grained String comparison, refer to
     * {@link java.text.Collator}.
     *
     * @param  other
     *         The object to compare this {@code String} against
     *
     * @return  {@code true} if the given object represents a {@code String}
     *          equivalent to this string, {@code false} otherwise
     *
     * @see  #compareTo(String)
     * @see  #equalsIgnoreCase(String)
     */
    public boolean equalsValue(final @NotNull String other) {
        return value.equals(other);
    }

    /**
     * Compares this {@code String} to another {@code String}, ignoring case
     * considerations.  Two strings are considered equal ignoring case if they
     * are of the same length and corresponding Unicode code points in the two
     * strings are equal ignoring case.
     * <p> Two Unicode code points are considered the same
     * ignoring case if at least one of the following is true:
     * <ul>
     *   <li> The two Unicode code points are the same (as compared by the
     *        {@code ==} operator)
     *   <li> Calling {@code Character.toLowerCase(Character.toUpperCase(int))}
     *        on each Unicode code point produces the same result
     * </ul>
     * <p>Note that this method does <em>not</em> take locale into account, and
     * will result in unsatisfactory results for certain locales.  The
     * {@link java.text.Collator} class provides locale-sensitive comparison.
     * @param  anotherString
     *         The {@code String} to compare this {@code String} against
     * @return  {@code true} if the argument is not {@code null} and it
     *          represents an equivalent {@code String} ignoring case; {@code
     *          false} otherwise
     * @see #equals(Object)
     * @see String#equalsIgnoreCase(String)
     */
    public boolean equalsIgnoreCase(final String anotherString) {
        return value.equalsIgnoreCase(anotherString);
    }

    /**
     * Returns the {@code char} value at the
     * specified index. An index ranges from {@code 0} to
     * {@code length() - 1}. The first {@code char} value of the sequence
     * is at index {@code 0}, the next at index {@code 1},
     * and so on, as for array indexing.
     * <p>If the {@code char} value specified by the index is a
     * <a href="Character.html#unicode">surrogate</a>, the surrogate
     * value is returned.
     * @param index   the index of the {@code char} value.
     * @return the {@code char} value at the specified index of this string.
     *         The first {@code char} value is at index {@code 0}.
     * @throws IndexOutOfBoundsException  if the {@code index}
     *         argument is negative or not less than the length of this
     *         string.
     * @see String#charAt(int)
     */
    public char charAt(final int index) {
        return value.charAt(index);
    }

    /**
     * <p>Checks if the value is empty ("") or null.</p>
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the value.
     * That functionality is available in isBlank().</p>
     * @return {@code true} if the value is empty or null
     * @see String#isEmpty()
     */
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    /**
     * <p>Strips whitespace from the start and end of a String.</p>
     * <p>A {@code null} input String returns {@code null}.</p>
     * @return the stripped value, {@code null} if null value
     * @see String#strip()
     */
    @Contract(" -> new")
    public @NotNull StringValue strip() {
        return strip(null);
    }

    /**
     * <p>Strips any of a set of characters from the start and end of the value.
     * This is similar to {@link String#trim()} but allows the characters
     * to be stripped to be controlled.</p>
     * <p>A {@code null} value returns {@code null}.
     * An empty value ("") returns the empty value.</p>
     * <p>If the stripChars String is {@code null}, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.
     * Alternatively use {@link #strip()}.</p>
     * @param stripChars  the characters to remove, null treated as whitespace
     * @return the stripped value, {@code null} if null value
     * @see String#strip()
     * @see StringUtils#strip(String)
     */
    @Contract("_ -> new")
    public @NotNull StringValue strip(final String stripChars) {
        if (value.isEmpty()) return StringValue.EMPTY;
        final String newValue = stripStart(value, stripChars);
        return StringValue.of(stripEnd(newValue, stripChars));
    }

    /**
     * Returns a stream of {@code int} zero-extending the {@code char} values
     * from this sequence.  Any char which maps to a {@linkplain
     * Character##unicode surrogate code point} is passed through
     * uninterpreted.
     * @return an IntStream of char values from this sequence
     * @see String#chars()
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull IntStream chars() {
        return value.chars();
    }

    /**
     * Returns a stream of code point values from this sequence.  Any surrogate
     * pairs encountered in the sequence are combined as if by {@linkplain
     * Character#toCodePoint Character.toCodePoint} and the result is passed
     * to the stream. Any other code units, including ordinary BMP characters,
     * unpaired surrogates, and undefined code units, are zero-extended to
     * {@code int} values which are then passed to the stream.
     *
     * @return an IntStream of Unicode code points from this sequence
     * @see String#codePoints()
     */
    @Contract(value = " -> new", pure = true)
    public @NotNull IntStream codePoints() {
        return value.codePoints();
    }

    /**
     * Converts this string to a new character array.
     * @return  a newly allocated character array whose length is the length
     *          of this string and whose contents are initialized to contain
     *          the character sequence represented by this string.
     */
    @Contract(value = " -> new", pure = true)
    public char @NotNull [] toCharArray() {
        return value.toCharArray();
    }

    /**
     * Formats using this string as the format string, and the supplied
     * arguments.
     * @implSpec This method is equivalent to {@code String.format(this, args)}.
     * @param  args Arguments referenced by the format specifiers in this string.
     * @return  A formatted string
     * @see  String#format(String,Object...)
     * @see  Formatter
     * @see String#formatted(Object...)
     */
    @Contract("_ -> new")
    public @NotNull StringValue formatted(final Object... args) {
        return StringValue.of(value.formatted(args));
    }

    /**
     * Returns a string whose value is the concatenation of this
     * string repeated {@code count} times.
     * <p>
     * If this string is empty or count is zero then the empty
     * string is returned.
     * @param count number of times to repeat
     * @return A string composed of this string repeated
     *         {@code count} times or the empty string if this
     *         string is empty or count is zero
     * @throws  IllegalArgumentException if the {@code count} is negative.
     * @see String#repeat(int)
     */
    @Contract("_ -> new")
    public @NotNull StringValue repeat(final int count) {
        return StringValue.of(value.repeat(count));
    }

    /**
     * This method allows the application of a function to {@code this}
     * string. The function should expect a single String argument
     * and produce an {@code R} result.
     * <p>
     * Any exception thrown by {@code f.apply()} will be propagated to the
     * caller.
     * @param function a function to apply
     * @param <R> the type of the result
     * @return the result of applying the function to this string
     * @see Function
     * @see String#transform(Function)
     */
    public <R> R transform(final Function<? super String, ? extends R> function) {
        return value.transform(function);
    }

    /**
     * Encodes this {@code String} into a sequence of bytes using the given
     * {@linkplain Charset charset}, storing the result into a
     * new byte array.
     * <p> This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement byte array.  The
     * {@link java.nio.charset.CharsetEncoder} class should be used when more
     * control over the encoding process is required.
     * @param charset
     *        The {@linkplain Charset} to be used to encode
     *        the {@code String}
     * @return The resultant byte array
     * @see String#getBytes(Charset)
     */
    @Contract(pure = true)
    public byte @NotNull [] getBytes(@NotNull final Charset charset) {
        return value.getBytes(charset);
    }

    /**
     * Compares this string to the specified {@code CharSequence}.  The
     * result is {@code true} if and only if this {@code String} represents the
     * same sequence of char values as the specified sequence. Note that if the
     * {@code CharSequence} is a {@code StringBuffer} then the method
     * synchronizes on it.
     * <p>For finer-grained String comparison, refer to
     * {@link java.text.Collator}.
     * @param cs The sequence to compare this {@code String} against
     * @return {@code true} if this {@code String} represents the same
     *         sequence of char values as the specified sequence, {@code
     *         false} otherwise
     * @see String#contentEquals(CharSequence)
     */
    public boolean contentEquals(@NotNull final CharSequence cs) {
        return value.contentEquals(cs);
    }

    /**
     * Tests if two string regions are equal.
     * <p>
     * A substring of this {@code String} object is compared to a substring
     * of the argument other. The result is true if these substrings
     * represent identical character sequences. The substring of this
     * {@code String} object to be compared begins at index {@code toffset}
     * and has length {@code len}. The substring of other to be compared
     * begins at index {@code ooffset} and has length {@code len}. The
     * result is {@code false} if and only if at least one of the following
     * is true:
     * <ul><li>{@code toffset} is negative.
     * <li>{@code ooffset} is negative.
     * <li>{@code toffset+len} is greater than the length of this
     * {@code String} object.
     * <li>{@code ooffset+len} is greater than the length of the other
     * argument.
     * <li>There is some nonnegative integer <i>k</i> less than {@code len}
     * such that:
     * {@code this.charAt(toffset + }<i>k</i>{@code ) != other.charAt(ooffset + }
     * <i>k</i>{@code )}
     * </ul>
     * <p>Note that this method does <em>not</em> take locale into account.  The
     * {@link java.text.Collator} class provides locale-sensitive comparison.
     * @param   toffset   the starting offset of the subregion in this string.
     * @param   other     the string argument.
     * @param   ooffset   the starting offset of the subregion in the string
     *                    argument.
     * @param   len       the number of characters to compare.
     * @return  {@code true} if the specified subregion of this string
     *          exactly matches the specified subregion of the string argument;
     *          {@code false} otherwise.
     * @see String#regionMatches(int, String, int, int)
     */
    public boolean regionMatches(final int toffset, @NotNull final String other, final int ooffset, final int len) {
        return value.regionMatches(toffset, other, ooffset, len);
    }

    /**
     * Returns the character (Unicode code point) at the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 0} to
     * {@link #length()}{@code  - 1}.
     * <p> If the {@code char} value specified at the given index
     * is in the high-surrogate range, the following index is less
     * than the length of this {@code String}, and the
     * {@code char} value at the following index is in the
     * low-surrogate range, then the supplementary code point
     * corresponding to this surrogate pair is returned. Otherwise,
     * the {@code char} value at the given index is returned.
     * @param      index the index to the {@code char} values
     * @return     the code point value of the character at the
     *             {@code index}
     * @throws     IndexOutOfBoundsException  if the {@code index}
     *             argument is negative or not less than the length of this
     *             string.
     * @see String#codePointAt(int)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue codePointAt(final int index) {
        return IntegerValue.of(value.codePointAt(index));
    }

    /**
     * Copies characters from this string into the destination character
     * array.
     * <p>
     * The first character to be copied is at index {@code srcBegin};
     * the last character to be copied is at index {@code srcEnd-1}
     * (thus the total number of characters to be copied is
     * {@code srcEnd-srcBegin}). The characters are copied into the
     * subarray of {@code dst} starting at index {@code dstBegin}
     * and ending at index:
     * <blockquote><pre>
     *     dstBegin + (srcEnd-srcBegin) - 1
     * </pre></blockquote>
     * @param      srcBegin   index of the first character in the string
     *                        to copy.
     * @param      srcEnd     index after the last character in the string
     *                        to copy.
     * @param      dst        the destination array.
     * @param      dstBegin   the start offset in the destination array.
     * @throws    IndexOutOfBoundsException If any of the following
     *            is true:
     *            <ul><li>{@code srcBegin} is negative.
     *            <li>{@code srcBegin} is greater than {@code srcEnd}
     *            <li>{@code srcEnd} is greater than the length of this
     *                string
     *            <li>{@code dstBegin} is negative
     *            <li>{@code dstBegin+(srcEnd-srcBegin)} is larger than
     *                {@code dst.length}</ul>
     * @see String#getChars(int, int, char[], int)
     */
    public void getChars(final int srcBegin, final int srcEnd, final char @NotNull [] dst, final int dstBegin) {
        value.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * Returns the index within this {@code String} that is
     * offset from the given {@code index} by
     * {@code codePointOffset} code points. Unpaired surrogates
     * within the text range given by {@code index} and
     * {@code codePointOffset} count as one code point each.
     * @param index the index to be offset
     * @param codePointOffset the offset in code points
     * @return the index within this {@code String}
     * @throws    IndexOutOfBoundsException if {@code index}
     *   is negative or larger than the length of this
     *   {@code String}, or if {@code codePointOffset} is positive
     *   and the substring starting with {@code index} has fewer
     *   than {@code codePointOffset} code points,
     *   or if {@code codePointOffset} is negative and the substring
     *   before {@code index} has fewer than the absolute value
     *   of {@code codePointOffset} code points.
     * @see String#offsetByCodePoints(int, int)
     */
    @Contract("_, _ -> new")
    public @NotNull IntegerValue offsetByCodePoints(final int index, final int codePointOffset) {
        return IntegerValue.of(value.offsetByCodePoints(index, codePointOffset));
    }

    /**
     * Compares two strings lexicographically, ignoring case
     * differences. This method returns an integer whose sign is that of
     * calling {@code compareTo} with case folded versions of the strings
     * where case differences have been eliminated by calling
     * {@code Character.toLowerCase(Character.toUpperCase(int))} on
     * each Unicode code point.
     * <p>
     * Note that this method does <em>not</em> take locale into account,
     * and will result in an unsatisfactory ordering for certain locales.
     * The {@link java.text.Collator} class provides locale-sensitive comparison.
     * @param   str   the {@code String} to be compared.
     * @return  a negative integer, zero, or a positive integer as the
     *          specified String is greater than, equal to, or less
     *          than this String, ignoring case considerations.
     * @see     java.text.Collator
     * @see     #codePoints()
     * @see String#compareToIgnoreCase(String)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue compareToIgnoreCase(@NotNull final String str) {
        return IntegerValue.of(value.compareToIgnoreCase(str));
    }

    /**
     * Encodes this {@code String} into a sequence of bytes using the named
     * charset, storing the result into a new byte array.
     * <p> The behavior of this method when this string cannot be encoded in
     * the given charset is unspecified.  The {@link
     * java.nio.charset.CharsetEncoder} class should be used when more control
     * over the encoding process is required.
     * @param  charsetName
     *         The name of a supported {@linkplain Charset
     *         charset}
     * @return  The resultant byte array
     * @throws  UnsupportedEncodingException
     *          If the named charset is not supported
     * @see String#getBytes(String)
     */
    @Contract(pure = true)
    public byte @NotNull [] getBytes(@NotNull final String charsetName) throws UnsupportedEncodingException {
        return value.getBytes(charsetName);
    }

    /**
     * Compares this string to the specified {@code StringBuffer}.  The result
     * is {@code true} if and only if this {@code String} represents the same
     * sequence of characters as the specified {@code StringBuffer}. This method
     * synchronizes on the {@code StringBuffer}.
     * <p>For finer-grained String comparison, refer to
     * {@link java.text.Collator}.
     * @param  sb The {@code StringBuffer} to compare this {@code String} against
     * @return  {@code true} if this {@code String} represents the same
     *          sequence of characters as the specified {@code StringBuffer},
     *          {@code false} otherwise
     * @see String#contentEquals(StringBuffer)
     */
    public boolean contentEquals(@NotNull final StringBuffer sb) {
        return value.contentEquals(sb);
    }

    /**
     * Returns the number of Unicode code points in the specified text
     * range of this {@code String}. The text range begins at the
     * specified {@code beginIndex} and extends to the
     * {@code char} at index {@code endIndex - 1}. Thus, the
     * length (in {@code char}s) of the text range is
     * {@code endIndex-beginIndex}. Unpaired surrogates within
     * the text range count as one code point each.
     *
     * @param beginIndex the index to the first {@code char} of
     * the text range.
     * @param endIndex the index after the last {@code char} of
     * the text range.
     * @return the number of Unicode code points in the specified text
     * range
     * @throws    IndexOutOfBoundsException if the
     * {@code beginIndex} is negative, or {@code endIndex}
     * is larger than the length of this {@code String}, or
     * {@code beginIndex} is larger than {@code endIndex}.
     * @see String#codePointCount(int, int)
     */
    @Contract("_, _ -> new")
    public @NotNull IntegerValue codePointCount(final int beginIndex, final int endIndex) {
        return IntegerValue.of(value.codePointCount(beginIndex, endIndex));
    }

    /**
     * Tests if two string regions are equal.
     * <p>
     * A substring of this {@code String} object is compared to a substring
     * of the argument {@code other}. The result is {@code true} if these
     * substrings represent Unicode code point sequences that are the same,
     * ignoring case if and only if {@code ignoreCase} is true.
     * The sequences {@code tsequence} and {@code osequence} are compared,
     * where {@code tsequence} is the sequence produced as if by calling
     * {@code this.substring(toffset, toffset + len).codePoints()} and
     * {@code osequence} is the sequence produced as if by calling
     * {@code other.substring(ooffset, ooffset + len).codePoints()}.
     * The result is {@code true} if and only if all the following
     * are true:
     * <ul><li>{@code toffset} is non-negative.
     * <li>{@code ooffset} is non-negative.
     * <li>{@code toffset+len} is less than or equal to the length of this
     * {@code String} object.
     * <li>{@code ooffset+len} is less than or equal to the length of the other
     * argument.
     * <li>if {@code ignoreCase} is {@code false}, all pairs of corresponding Unicode
     * code points are equal integer values; or if {@code ignoreCase} is {@code true},
     * {@link Character#toLowerCase(int) Character.toLowerCase(}
     * {@link Character#toUpperCase(int)}{@code )} on all pairs of Unicode code points
     * results in equal integer values.
     * </ul>
     * <p>Note that this method does <em>not</em> take locale into account,
     * and will result in unsatisfactory results for certain locales when
     * {@code ignoreCase} is {@code true}.  The {@link java.text.Collator} class
     * provides locale-sensitive comparison.
     * @param   ignoreCase   if {@code true}, ignore case when comparing
     *                       characters.
     * @param   toffset      the starting offset of the subregion in this
     *                       string.
     * @param   other        the string argument.
     * @param   ooffset      the starting offset of the subregion in the string
     *                       argument.
     * @param   len          the number of characters (Unicode code units -
     *                       16bit {@code char} value) to compare.
     * @return  {@code true} if the specified subregion of this string
     *          matches the specified subregion of the string argument;
     *          {@code false} otherwise. Whether the matching is exact
     *          or case-insensitive depends on the {@code ignoreCase}
     *          argument.
     * @see     #codePoints()
     * @see String#regionMatches(boolean, int, String, int, int)
     */
    public boolean regionMatches(final boolean ignoreCase, final int toffset, @NotNull final String other,
                                 final int ooffset, final int len) {
        return value.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    /**
     * Returns the character (Unicode code point) before the specified
     * index. The index refers to {@code char} values
     * (Unicode code units) and ranges from {@code 1} to {@link
     * CharSequence#length() length}.
     * <p> If the {@code char} value at {@code (index - 1)}
     * is in the low-surrogate range, {@code (index - 2)} is not
     * negative, and the {@code char} value at {@code (index -
     * 2)} is in the high-surrogate range, then the
     * supplementary code point value of the surrogate pair is
     * returned. If the {@code char} value at {@code index -
     * 1} is an unpaired low-surrogate or a high-surrogate, the
     * surrogate value is returned.
     * @param     index the index following the code point that should be returned
     * @return    the Unicode code point value before the given index.
     * @throws    IndexOutOfBoundsException if the {@code index}
     *            argument is less than 1 or greater than the length
     *            of this string.
     * @see String#codePointBefore(int)
     */
    @Contract("_ -> new")
    public @NotNull IntegerValue codePointBefore(final int index) {
        return IntegerValue.of(value.codePointBefore(index));
    }

    /**
     * Encodes this {@code String} into a sequence of bytes using the
     * {@link Charset#defaultCharset() default charset}, storing the result
     * into a new byte array.
     * <p> The behavior of this method when this string cannot be encoded in
     * the default charset is unspecified.  The {@link
     * java.nio.charset.CharsetEncoder} class should be used when more control
     * over the encoding process is required.
     * @return  The resultant byte array
     * @see String#getBytes()
     */
    @Contract(value = " -> new", pure = true)
    public byte @NotNull [] getBytes() {
        return value.getBytes();
    }

    /**
     * Returns the value.
     * @return the stored value
     */
    public String stringValue() {
        return value;
    }

    /**
     * Returns the length of this string.
     * The length is equal to the number of <a href="Character.html#unicode">Unicode
     * code units</a> in the string.
     *
     * @return  the length of the sequence of characters represented by this
     *          object.
     * @see String#length()
     */
    @Contract(" -> new")
    public @NotNull IntegerValue length() {
        return IntegerValue.of(value.length());
    }

    /**
     * Returns the value.
     * @return the stored value
     */
    @Override
    public String get() {
        return value;
    }

    /**
     * Compares this StringValue to another in ascending order.
     *
     * @param other  the other StringValue to compare to, not null
     * @return negative if this is less, zero if equal, positive if greater
     *  where false is less than true
     */
    public int compareTo(final @NotNull StringValue other) {
        return value.compareTo(other.value);
    }

    /**
     * Compares two strings lexicographically.
     * The comparison is based on the Unicode value of each character in
     * the strings. The character sequence represented by this
     * {@code String} object is compared lexicographically to the
     * character sequence represented by the argument string. The result is
     * a negative integer if this {@code String} object
     * lexicographically precedes the argument string. The result is a
     * positive integer if this {@code String} object lexicographically
     * follows the argument string. The result is zero if the strings
     * are equal; {@code compareTo} returns {@code 0} exactly when
     * the {@link #equals(Object)} method would return {@code true}.
     * <p>
     * This is the definition of lexicographic ordering. If two strings are
     * different, then either they have different characters at some index
     * that is a valid index for both strings, or their lengths are different,
     * or both. If they have different characters at one or more index
     * positions, let <i>k</i> be the smallest such index; then the string
     * whose character at position <i>k</i> has the smaller value, as
     * determined by using the {@code <} operator, lexicographically precedes the
     * other string. In this case, {@code compareTo} returns the
     * difference of the two character values at position {@code k} in
     * the two string -- that is, the value:
     * <blockquote><pre>
     * this.charAt(k)-anotherString.charAt(k)
     * </pre></blockquote>
     * If there is no index position at which they differ, then the shorter
     * string lexicographically precedes the longer string. In this case,
     * {@code compareTo} returns the difference of the lengths of the
     * strings -- that is, the value:
     * <blockquote><pre>
     * this.length()-anotherString.length()
     * </pre></blockquote>
     *
     * <p>For finer-grained String comparison, refer to
     * {@link java.text.Collator}.
     *
     * @param   anotherString   the {@code String} to be compared.
     * @return  the value {@code 0} if the argument string is equal to
     *          this string; a value less than {@code 0} if this string
     *          is lexicographically less than the string argument; and a
     *          value greater than {@code 0} if this string is
     *          lexicographically greater than the string argument.
     * @see String#compareTo(String)
     */
    public int compareTo(@NotNull final String anotherString) {
        return value.compareTo(anotherString);
    }

    @Override
    public int compareTo(@NotNull final ImmutableValue<String> other) {
        return this.compareTo(other.get());
    }

    /**
     * Returns an {@link Optional} containing the nominal descriptor for this
     * instance, which is the instance itself.
     *
     * @return an {@link Optional} describing the {@linkplain String} value
     */
    @Contract(value = " -> new", pure = true)
    @Override
    public @NotNull Optional<String> describeConstable() {
        return value.describeConstable();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;

        if (obj == null || getClass() != obj.getClass()) return false;

        final StringValue that = (StringValue) obj;

        return new EqualsBuilder().appendSuper(super.equals(obj)).append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(value).toHashCode();
    }

    /**
     * Returns the base String value
     * @return  the String value
     */
    @Override
    public String toString() {
        return value;
    }
}
