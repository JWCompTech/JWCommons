package com.jwcomptech.shared.utils;

import org.apache.commons.lang3.CharUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jwcomptech.shared.Literals.*;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNull;
import static com.jwcomptech.shared.utils.CheckIf.checkArgumentNotNullOrEmpty;
import static org.apache.commons.lang3.StringUtils.stripEnd;
import static org.apache.commons.lang3.StringUtils.stripStart;

/**
 * Contains methods dealing with strings.
 * @since 0.0.1
 */
@SuppressWarnings({"ClassWithTooManyMethods", "OverlyComplexClass", "unused"})
public final class StringUtils {
    /**
     * Converts a map into a delimited string value.
     * A "{@literal =}" separates the keys and values and a "{@literal &}" separates the key pairs.
     * @param stringMap map to convert
     * @return string representation of map
     * @throws IllegalArgumentException if stringMap is null
     */
    public static String convertMapToString(final Map<String, String> stringMap) {
        return CollectionUtils.convertMapToString(stringMap);
    }

    /**
     * Converts a delimited string value into a map.
     * <p>Expects that "{@literal =}" separates the keys and values and a "{@literal &}" separates the key pairs.</p>
     * @param input map to convert
     * @return string representation of map
     * @throws IllegalArgumentException if input is null
     */
    public static Map<String, String> convertStringToMap(final String input) {
        return CollectionUtils.convertStringToMap(input);
    }

    /**
     * Converts an object to a Base64 byte string to use for socket communication.
     * @param object the object to convert to a Base64 byte string
     * @return the Base64 byte string
     * @throws IllegalArgumentException if object is null
     * @throws IOException if the conversion fails
     */
    public static String convertToByteString(final Object object) throws IOException {
        checkArgumentNotNull(object, cannotBeNull("object"));
        try (final var bos = new ByteArrayOutputStream(); final ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            final var byteArray = bos.toByteArray();
            return Base64.getEncoder().encodeToString(byteArray);
        }
    }

    /**
     * Converts a byte array to an object.
     * @param bytes the byte array to convert to an object
     * @return the converted object
     * @throws IllegalArgumentException if bytes is null
     * @throws IOException if the conversion fails
     * @throws ClassNotFoundException if the converted object doesn't match a found object type
     */
    public static Object convertFromByteArray(final byte[] bytes) throws IOException, ClassNotFoundException {
        checkArgumentNotNull(bytes, cannotBeNull("bytes"));
        try (final var bis = new ByteArrayInputStream(bytes); final ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    /**
     * Converts a Base64 byte string to an object.
     * @param byteString the Base64 byte string to convert to an object
     * @return the converted object
     * @throws IllegalArgumentException if byteString is null
     * @throws IOException if the conversion fails
     * @throws ClassNotFoundException if the converted object doesn't match a found object type
     */
    public static Object convertFromByteString(final String byteString)
            throws IOException, ClassNotFoundException {
        checkArgumentNotNull(byteString, cannotBeNull("byteString"));
        final var bytes = Base64.getDecoder().decode(byteString);
        try (final var bis = new ByteArrayInputStream(bytes); final ObjectInput in = new ObjectInputStream(bis)) {
            return in.readObject();
        }
    }

    /**
     * Surrounds a string with quotes.
     * @param input string to edit
     * @return a string surrounded with quotes
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String quoteString(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return "\"%s\"".formatted(input);
    }

    /**
     * Removes quotes from a string.
     * @param input string to edit
     * @return a string with quotes removed
     * @throws IllegalArgumentException if input is null
     */
    public static String unquoteString(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return (input.startsWith("\"") && input.endsWith("\""))
                || (input.startsWith("'") && input.endsWith("'"))
                ? input.substring(1, input.length() - 1) : input;

    }

    /**
     * Converts a string to a Boolean.
     * @param input String to check
     * @return true if string does match a boolean
     * @throws IllegalArgumentException if input does not match a boolean value or is null
     */
    public static @NotNull Boolean toBoolean(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        final var value = input.toLowerCase(Locale.getDefault()).trim();
        return switch (value) {
            case "true", "t", "yes", "y", "1", "succeeded", "succeed", "enabled" -> true;
            case "false", "f", "no", "n", "0", "-1", "failed", "fail", "disabled" -> false;
            default -> throw new IllegalArgumentException("Input is not a boolean value.");
        };
    }

    /**
     * Checks if a string is a valid IPv4 address.
     * @param input string to check
     * @return true if string is a valid IPv4 address
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull Boolean isValidIPAddress(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.matches("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-4])\\.){3}"
                + "([0-9]|[1-9][0-9]|1[0-9‌​]{2}|2[0-4][0-9]|25[0-4])$");
    }

    /**
     * Checks if a string is a valid URL.
     * @param input string to check
     * @return true if string is a valid URL
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull Boolean isValidUrl(final String input) {
        checkArgumentNotNull(input, cannotBeNullOrEmpty("input"));
        return input.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]");
    }

    /**
     * Checks to see if the specified string is a valid email address according to the RFC 2822 specification.
     * @param input the email address string to test for validity
     * @return true if the given text is valid according to RFC 2822, false otherwise
     * @throws IllegalArgumentException if input is null
     */
    public static boolean isValidEmailAddress(final String input) { return isValidEmailAddress(input,
            false, false); }

    /**
     * Checks to see if the specified string is a valid email address according to the RFC 2822 specification.
     * @param input the email address string to test for validity
     * @param allowQuotedIdentifiers specifies if quoted identifiers are allowed
     * @return true if the given text is valid according to RFC 2822, false otherwise
     * @throws IllegalArgumentException if input is null
     */
    public static boolean isValidEmailAddress(final String input, final boolean allowQuotedIdentifiers) {
        return isValidEmailAddress(input, allowQuotedIdentifiers, false);
    }

    /**
     * Checks to see if the specified string is a valid email address according to the RFC 2822 specification.
     * @param input the email address string to test for validity
     * @param allowQuotedIdentifiers specifies if quoted identifiers are allowed
     * @param allowDomainLiterals specifies if domain literals are allowed
     * @return true if the given text is valid according to RFC 2822, false otherwise
     * @throws IllegalArgumentException if input is null
     */
    public static boolean isValidEmailAddress(final String input,
                                              final boolean allowQuotedIdentifiers, final boolean allowDomainLiterals) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        final var validator = new EmailValidator(allowQuotedIdentifiers, allowDomainLiterals);
        return validator.validPattern.matcher(input).matches();
    }

    /**
     * Ensures that a string starts with a given prefix.
     * @param input string to check
     * @param prefix prefix to check
     * @param ignoreCase if true ignores case
     * @return string with the specified prefix
     * @throws IllegalArgumentException if input or prefix is null
     */
    public static @NotNull String ensureStartsWith(final String input, final String prefix, final Boolean ignoreCase) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        checkArgumentNotNull(prefix, cannotBeNull("prefix"));
        var startsWith = input.startsWith(prefix);
        if(!startsWith && ignoreCase) startsWith = input.startsWith(prefix.toUpperCase(Locale.getDefault()));
        if(!startsWith && ignoreCase) startsWith = input.startsWith(prefix.toLowerCase(Locale.getDefault()));
        return startsWith ? input : prefix + input;
    }

    /**
     * Ensures that a string ends with a given suffix.
     * @param input string to check
     * @param suffix suffix to check
     * @param ignoreCase if true ignores case
     * @return string with the specified suffix
     * @throws IllegalArgumentException if input or suffix is null
     */
    public static @NotNull String ensureEndsWith(final String input, final String suffix, final Boolean ignoreCase) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        checkArgumentNotNull(suffix, cannotBeNull("suffix"));
        var endsWith = input.endsWith(suffix);
        if(!endsWith && ignoreCase) endsWith = input.startsWith(suffix.toUpperCase(Locale.getDefault()));
        if(!endsWith && ignoreCase) endsWith = input.startsWith(suffix.toLowerCase(Locale.getDefault()));
        return endsWith ? input : input + suffix;
    }

    /**
     * Checks if a string ends with a given suffix.
     * @param input string to check
     * @param suffix suffix to check
     * @return true if string ends with a given suffix
     * @throws IllegalArgumentException if input or suffix is null
     */
    public static boolean endsWithIgnoreCase(final String input, final String suffix) {
        return endsWithIgnoreCase(input, suffix, Locale.getDefault());
    }

    /**
     * Checks if a string ends with a given suffix.
     * @param input string to check
     * @param suffix suffix to check
     * @param locale the locale to use
     * @return true if string ends with a given suffix
     * @throws IllegalArgumentException if input or suffix is null
     */
    public static boolean endsWithIgnoreCase(final String input, final String suffix, final Locale locale) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        checkArgumentNotNull(suffix, cannotBeNull("suffix"));
        return input.endsWith(suffix)
                || (input.length() >= suffix.length() && input.toLowerCase(locale).endsWith(suffix.toLowerCase(locale)));
    }

    /**
     * Checks if a string starts with a given suffix.
     * @param input string to check
     * @param prefix suffix to check
     * @return true if string starts with a given suffix
     * @throws IllegalArgumentException if input or suffix is null
     */
    public static boolean startsWithIgnoreCase(final String input, final String prefix) {
        return startsWithIgnoreCase(input, prefix, Locale.getDefault());
    }

    /**
     * Checks if a string starts with a given suffix.
     * @param input string to check
     * @param prefix suffix to check
     * @param locale the locale to use
     * @return true if string starts with a given suffix
     * @throws IllegalArgumentException if input or suffix is null
     */
    public static boolean startsWithIgnoreCase(final String input, final String prefix, final Locale locale) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        checkArgumentNotNull(prefix, cannotBeNull("prefix"));
        return input.startsWith(prefix)
                || (input.length() >= prefix.length() && input.toLowerCase(locale)
                .startsWith(prefix.toLowerCase(locale)));
    }

    /**
     * Removes the last character from a string.
     * @param input string to edit
     * @return string with last character removed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeLastCharacter(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.substring(0, input.length() - 1);
    }

    /**
     * Removes the specified number of characters removed from the end of a string.
     * @param input string to edit
     * @param number the number of characters to remove
     * @return string with the specified number of characters removed from the end
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeLastCharacters(final String input, final int number) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.substring(0, input.length() - number);
    }

    /**
     * Removes the first character from a string.
     * @param input string to edit
     * @return string with first character removed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeFirstCharacter(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.substring(1);
    }

    /**
     * Removes the specified number of characters removed from the beginning of a string.
     * @param input string to edit
     * @param number the number of characters to remove
     * @return string with the specified number of characters removed from the beginning
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeFirstCharacters(final String input, final int number) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.substring(number);
    }

    /**
     * Removes all characters except alphanumeric from specified string.
     * @param input string to edit
     * @return string with all characters except alphanumeric removed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeAllSpecialCharacters(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.replaceAll("[^a-zA-Z0-9]+","");
    }

    /**
     * Removes all alphanumeric characters from specified string.
     * @param input string to edit
     * @return string with all alphanumeric characters removed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeAllAlphanumericCharacters(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.replaceAll("[a-zA-Z0-9]+","");
    }

    /**
     * Removes all letters from specified string.
     * @param input string to edit
     * @return string with all letters removed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeAllLetters(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.replaceAll("[a-zA-Z]+","");
    }

    /**
     * Removes all numbers from specified string.
     * @param input string to edit
     * @return string with all numbers removed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String removeAllNumbers(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.replaceAll("[0-9]+","");
    }

    /**
     * Reverses the characters in the specified string.
     * @param input string to edit
     * @return string with characters reversed
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String reverse(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return new StringBuilder(input).reverse().toString();
    }

    /**
     * Returns the first part of the string, up until the character c. If c is not found in the
     * string the whole string is returned.
     * @param input the string to edit
     * @param c the character to find
     * @return edited string
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String leftOf(final String input, final char c) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        final var index = input.indexOf(c);
        if (0 <= index) return input.substring(0, index);
        return input;
    }

    /**
     * Returns right part of the string, after the character c. If c is not found in the
     * string the whole string is returned.
     * @param input the string to edit
     * @param c the character to find
     * @return edited string
     * @throws IllegalArgumentException if input is null
     */
    public static @NotNull String rightOf(final String input, final char c) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        final var index = input.indexOf(c);
        if (0 <= index) return input.substring(index + 1);
        return input;
    }

    /**
     * Returns first character in a string or empty string if the input is empty.
     * @param input string to check
     * @return first character in a string
     * @throws IllegalArgumentException if input is null
     */
    public static String firstChar(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return 1 < input.length() ? input.substring(0, 1) : input;
    }

    /**
     * Returns last character in a string or empty string if the input is empty.
     * @param input string to check
     * @return last character in a string
     * @throws IllegalArgumentException if input is null
     */
    public static String lastChar(final String input) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return 1 < input.length() ? input.substring(input.length() - 1, 1) : input;
    }

    /**
     * Returns first number of characters in a string or empty string if the input is empty.
     * @param input string to check
     * @param number number of characters to retrieve
     * @return first number of characters in a string
     * @throws IllegalArgumentException if input is null
     */
    public static String firstChars(final String input, final int number) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.length() < number ? input : input.substring(0, number);
    }

    /**
     * Returns last number of characters in a string or empty string if the input is empty.
     * @param input string to check
     * @param number number of characters to retrieve
     * @return last number of characters in a string
     * @throws IllegalArgumentException if input is null
     */
    public static String lastChars(final String input, final int number) {
        checkArgumentNotNull(input, cannotBeNull("input"));
        return input.length() < number ? input : input.substring(number + 1);
    }

    /**
     * Capitalizes all words in a string.
     * @param input string to edit
     * @return string with all words capitalized
     * @throws IllegalArgumentException if input is null or empty
     */
    public static @NotNull String toTitleCase(final String input) {
        checkArgumentNotNullOrEmpty(input, cannotBeNullOrEmpty("input"));
        final var words = input.trim().split(" ");
        return Arrays.stream(words)
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1) + ' ')
                .collect(Collectors.joining())
                .trim();
    }

    /**
     * Return a not null string.
     * @param input String
     * @return empty string if it is null otherwise the string passed in as parameter.
     */
    @Contract(value = "!null -> param1", pure = true)
    public static @NotNull String nonNull(final String input) { return input == null ? "" : input; }

    /**
     * Checks if the specified String is a number that may be negative or
     * contain a decimal point using the default locale.
     * <p>
     *     This method is more robust than most isNumeric methods
     *     in other libraries because it checks for a maximum of one
     *     minus sign and checks for a maximum of one decimal point.
     *     It also does not use pattern matching or number parsing
     *     with exception checking thus increasing speed.
     * <p>
     *     Examples:
     *     StringUtils.isNumeric("1"); - true
     *     StringUtils.isNumeric("1.5"); - true
     *     StringUtils.isNumeric("1.556"); - true
     *     StringUtils.isNumeric("1..5"); - false
     *     StringUtils.isNumeric("1.5D"); - false
     *     StringUtils.isNumeric("1A.5"); - false
     * <p>
     *     StringUtils.isNumeric("-1"); - true
     *     StringUtils.isNumeric("-1.5"); - true
     *     StringUtils.isNumeric("-1.556"); - true
     *     StringUtils.isNumeric("-1..5"); - false
     *     StringUtils.isNumeric("-1.5D"); - false
     *     StringUtils.isNumeric("-1A.5"); - false
     * <p>
     *     StringUtils.isNumeric("-"); - false
     *     StringUtils.isNumeric("--1"); - false
     * </p>
     * @param input string to check
     * @return true if input is a number
     */
    public static boolean isNumeric(final CharSequence input) {
        return isNumeric(input, Locale.getDefault(Locale.Category.FORMAT));
    }

    /**
     * Checks if the specified String is a number that may be negative or
     * contain a decimal point using the specified locale.
     * <p>
     *     This method is more robust than most isNumeric methods
     *     in other libraries because it checks for a maximum of one
     *     minus sign and checks for a maximum of one decimal point.
     *     It also does not use pattern matching or number parsing
     *     with exception checking thus increasing speed.
     * <p>
     *     Examples:
     *     StringUtils.isNumeric("1"); - true
     *     StringUtils.isNumeric("1.5"); - true
     *     StringUtils.isNumeric("1.556"); - true
     *     StringUtils.isNumeric("1..5"); - false
     *     StringUtils.isNumeric("1.5D"); - false
     *     StringUtils.isNumeric("1A.5"); - false
     * <p>
     *     StringUtils.isNumeric("-1"); - true
     *     StringUtils.isNumeric("-1.5"); - true
     *     StringUtils.isNumeric("-1.556"); - true
     *     StringUtils.isNumeric("-1..5"); - false
     *     StringUtils.isNumeric("-1.5D"); - false
     *     StringUtils.isNumeric("-1A.5"); - false
     * <p>
     *     StringUtils.isNumeric("-"); - false
     *     StringUtils.isNumeric("--1"); - false
     * </p>
     * @param input string to check
     * @param locale the locale to use
     * @return true if input is a number
     * @throws IllegalArgumentException if locale is null
     */
    @SuppressWarnings("MethodWithMultipleReturnPoints")
    public static boolean isNumeric(final CharSequence input, final Locale locale) {
        checkArgumentNotNull(locale, cannotBeNull("locale"));
        //Check for null or blank string
        if(isBlank(input)) return false;

        //Retrieve the minus sign and decimal separator characters from the current Locale
        final char localeMinusSign = DecimalFormatSymbols.getInstance(locale).getMinusSign();
        final char localeDecimalSeparator = DecimalFormatSymbols.getInstance(locale).getDecimalSeparator();

        //Check if first character is a minus sign
        final boolean isNegative = input.charAt(0) == localeMinusSign;
        //Check if string is not just a minus sign
        if (isNegative && 1 == input.length()) return false;

        boolean isDecimalSeparatorFound = false;

        //If the string has a minus sign ignore the first character
        final int startCharIndex = isNegative ? 1 : 0;

        //Check if each character is a number or a decimal separator
        //and make sure string only has a maximum of one decimal separator
        for (int i = startCharIndex; i < input.length(); i++) {
            if(!Character.isDigit(input.charAt(i))) {
                if(input.charAt(i) == localeDecimalSeparator && !isDecimalSeparatorFound) {
                    isDecimalSeparatorFound = true;
                } else return false;
            }
        }
        return true;
    }

    /**
     * <p>
     * Unwraps a given string from a character.
     * </p>
     *
     * <pre>
     * StringUtils.unwrap(null, null)         = null
     * StringUtils.unwrap(null, '\0')         = null
     * StringUtils.unwrap(null, '1')          = null
     * StringUtils.unwrap("\'abc\'", '\'')    = "abc"
     * StringUtils.unwrap("AABabcBAA", 'A')  = "ABabcBA"
     * StringUtils.unwrap("A", '#')           = "A"
     * StringUtils.unwrap("#A", '#')          = "#A"
     * StringUtils.unwrap("A#", '#')          = "A#"
     * </pre>
     *
     * @param input the String to be unwrapped, can be null
     * @param wrapChar the character used to unwrap
     * @throws IllegalArgumentException if input or wrapChar are null
     * @return unwrapped String or the original string if it is not quoted properly with the wrapChar
     */
    public static @NotNull String unwrap(final String input, final char wrapChar) {
        checkArgumentNotNull(input, cannotBeNull("str"));
        checkArgumentNotNull(wrapChar, cannotBeNull("wrapChar"));
        if (input.isEmpty() || CharUtils.NUL == wrapChar) {
            return input;
        }

        if (input.charAt(0) == wrapChar && input.charAt(input.length() - 1) == wrapChar) {
            final int startIndex = 0;
            final int endIndex = input.length() - 1;
            return input.substring(startIndex + 1, endIndex);
        }

        return input;
    }

    /**
     * Returns string with first char uppercase.
     * @param input string to edit
     * @return string with first char uppercase
     * @throws IllegalArgumentException if input is null
     */

    public static @NotNull String uppercaseFirst(final String input) {
        checkArgumentNotNull(input, INPUT_CANNOT_BE_NULL);
        return 1 < input.length()
                ? input.substring(0, 1).toUpperCase(Locale.getDefault()) + input.substring(1)
                : input.toUpperCase(Locale.getDefault());
    }

    /**
     * Returns string with first char uppercase.
     * @param input string to edit
     * @param locale use the case transformation rules for this locale
     * @return string with first char uppercase
     * @throws IllegalArgumentException if input or locale is null
     */

    public static @NotNull String uppercaseFirst(final String input, final Locale locale) {
        checkArgumentNotNull(input, INPUT_CANNOT_BE_NULL);
        checkArgumentNotNull(locale, LOCALE_CANNOT_BE_NULL);
        return 1 < input.length()
                ? input.substring(0, 1).toUpperCase(locale) + input.substring(1)
                : input.toUpperCase(locale);
    }

    /**
     * Returns string with first char lowercase.
     * @param input string to edit
     * @return string with first char lowercase
     * @throws IllegalArgumentException if input is null or empty
     */
    public static @NotNull String lowercaseFirst(final String input) {
        checkArgumentNotNull(input, INPUT_CANNOT_BE_NULL);
        return 1 < input.length()
                ? input.substring(0, 1).toLowerCase(Locale.getDefault()) + input.substring(1)
                : input.toLowerCase(Locale.getDefault());
    }

    /**
     * Returns string with first char lowercase.
     * @param input string to edit
     * @return string with first char lowercase
     * @throws IllegalArgumentException if input is null or empty
     */
    public static @NotNull String lowercaseFirst(final String input, final Locale locale) {
        checkArgumentNotNull(input, INPUT_CANNOT_BE_NULL);
        checkArgumentNotNull(locale, LOCALE_CANNOT_BE_NULL);
        return 1 < input.length()
                ? input.substring(0, 1).toLowerCase(locale) + input.substring(1)
                : input.toLowerCase(locale);
    }

    /**
     * <p>Checks if a CharSequence is empty (""), null or whitespace only.</p>
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * @param input the CharSequence to check, may be null
     * @return {@code true} if input is null, empty or whitespace only
     */
    public static boolean isBlank(final CharSequence input) {
        return (input == null || input.isEmpty())
                || IntStream.range(0, input.length())
                .allMatch(i -> Character.isWhitespace(input.charAt(i)));
    }

    /**
     * <p>Strips whitespace from the start and end of a String.</p>
     *
     * <p>A {@code null} input String returns {@code null}.</p>
     *
     * @param input the String to remove whitespace from, may be null
     * @return the stripped String, {@code null} if null String input
     * @see String#strip()
     */
    public static String strip(final String input) {
        return strip(input, null);
    }

    /**
     * <p>Strips any of a set of characters from the start and end of a String.
     * This is similar to {@link String#trim()} but allows the characters
     * to be stripped to be controlled.</p>
     *
     * <p>A {@code null} input String returns {@code null}.
     * An empty string ("") input returns the empty string.</p>
     *
     * <p>If the stripChars String is {@code null}, whitespace is
     * stripped as defined by {@link Character#isWhitespace(char)}.
     * Alternatively use {@link #strip(String)}.</p>
     *
     * @param input the String to remove characters from, may be null
     * @param stripChars the characters to remove, null treated as whitespace
     * @return the stripped String, {@code null} if null String input
     * @see String#strip()
     */
    public static String strip(final @NotNull String input, final String stripChars) {
        if (input.isEmpty()) return input;
        String input_ = stripStart(input, stripChars);
        return stripEnd(input_, stripChars);
    }

    /** Prevents instantiation of this utility class. */
    private StringUtils() { }
}
