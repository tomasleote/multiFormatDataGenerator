package com.datprof.generators.utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Utility class for parsing strings into other types.
 */
public class Parsers {

    private Parsers() {
    }

    /**
     * Parse a string using the given converter returning the the parsed object.
     * The converter function should return an object of type R.
     *
     * @param name      The name of the property.
     * @param s         String to parse.
     * @param converter The function that takes a string and returns an object of type R
     * @param <R>       The result object type.
     * @return R        The result
     * @throws IllegalArgumentException In case the converter throws an exception.
     */
    public static <R> R parse(final String name, final String s, final Function<String, R> converter) {
        try {
            return converter.apply(s);
        } catch (final Throwable e) {
            throw new IllegalArgumentException(String.format(
                    "Failed to parse property '%s' with input %s.",
                    name,
                    s == null ? "<null>" : "'" + s + "'"));
        }
    }

    /**
     * Safely get a string.
     *
     * @param s The string to get.
     * @return Optional.of(result) or Optional.empty()
     */
    public static Optional<String> asNonEmptyValue(final String s) {
        return Optional.ofNullable(s).filter(in -> !in.isEmpty());
    }

    /**
     * Parse the string into a list of trimmed strings using ',' as separator.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @return The list of separated strings.
     * @throws IllegalArgumentException In case the the property value is null or empty.
     */
    public static List<String> asStringList(final String name, final String s) {
        return asStringList(name, s, ",");
    }

    /**
     * Parse the string into a list of trimmed strings using the given separator.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @param sep  The separator.
     * @return The list of separated strings.
     * @throws IllegalArgumentException In case the the property value is null or empty.
     */
    public static List<String> asStringList(final String name, final String s, final String sep) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        return splitToList(stringToParse, sep);
    }

    private static List<String> splitToList(final String in, final String sep) {
        final String[] items = in.split(sep);
        final List<String> list = new ArrayList<>();
        for (final String item : items)
            list.add(item.trim());
        return list;
    }

    /**
     * Parse the string into a list of doubles using ',' as separator.
     *
     * @param name The name of the property.
     * @param in   The string to parse.
     * @return The list of separated and parsed doubles.
     * @throws IllegalArgumentException In case the the property value is null or empty or a string part can not be parsed into a Double.
     */
    public static List<Double> parseAsDoubleList(final String name, final String in) {
        return parseAsDoubleList(name, in, ",");
    }

    /**
     * Parse the string into a list of doubles using the given separator.
     *
     * @param name The name of the property.
     * @param in   The string to parse.
     * @return The list of separated and parsed doubles.
     * @throws IllegalArgumentException In case the the property value is null or empty or a string part can not be parsed into a Double.
     */
    public static List<Double> parseAsDoubleList(final String name, final String in, final String sep) {
        return parseAsList(name, in, Double::parseDouble, sep);
    }

    private static <R> List<R> parseAsList(final String name, final String in,
                                           final Function<String, R> converter, final String sep) {
        final String stringToParse = validateNotNullOrEmpty(name, in);
        final List<String> listOfDoubleStrings = splitToList(stringToParse, sep);
        final List<R> list = new ArrayList<>();
        for (int i = 0; i < listOfDoubleStrings.size(); i++)
            list.add(parse(name + "[" + i + "]", listOfDoubleStrings.get(i), converter));
        return list;
    }

    /**
     * Parse the string into a double.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @return The double value parsed.
     * @throws IllegalArgumentException In case the property value is null or empty or can not be parsed into a Double.
     */
    public static double parseAsDouble(final String name, final String s) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        return parse(name, stringToParse, Double::parseDouble);
    }

    /**
     * Parse the string into an DateTimeFormatter.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @return The DateTimeFormatter instance parsed.
     * @throws IllegalArgumentException In case the property value is null or empty or can not be parsed into a DateTimeFormatter.
     */
    public static DateTimeFormatter parseAsDateTimeFormatter(final String name, final String s) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        return parse(name, stringToParse, pattern -> new DateTimeFormatterBuilder()
                .appendPattern(pattern)
                .parseDefaulting(ChronoField.YEAR_OF_ERA, 1)
                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
                .parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
                .toFormatter());
    }

    /**
     * Parse the string into a ZonedDateTime object.
     *
     * @param name              The name of the property.
     * @param s                 The string to parse.
     * @param dateTimeFormatter The DateTimeFormatter used to parse the string.
     * @return The date/time value parsed.
     * @throws IllegalArgumentException In case the property value is null or empty or can not be parsed into a ZonedDateTime object.
     */
    public static ZonedDateTime parseAsZonedDateTime(final String name, final String s,
                                                     final DateTimeFormatter dateTimeFormatter) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        return parse(name, stringToParse, in -> ZonedDateTime.from(dateTimeFormatter.parse(in)));
    }

    /**
     * Parse the string into an integer.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @return The integer value parsed.
     * @throws IllegalArgumentException In case the property value is null or empty or can not be parsed into a int.
     */
    public static int parseAsInt(final String name, final String s) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        return parse(name, stringToParse, Integer::parseInt);
    }

    /**
     * Parse the string into a long.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @return The long value parsed.
     * @throws IllegalArgumentException In case the property value is null or empty or can not be parsed into a long.
     */
    public static long parseAsLong(final String name, final String s) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        return parse(name, stringToParse, Long::parseLong);
    }

    /**
     * Validate that the given string s is not null or the empty string.
     *
     * @param name The name of the property (for generating a proper error message)
     * @param s    The string to check.
     * @return The validated string.
     */
    public static String validateNotNullOrEmpty(final String name, final String s) {
        return asNonEmptyValue(s)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Missing property '%s'.", name)));
    }

    /**
     * Parse the string into a chronoUnit.
     *
     * @param name The name of the property.
     * @param s    The string to parse.
     * @return The ChronoUnit parsed.
     * @throws IllegalArgumentException In case the property value is null or empty or can not be parsed into a chronoUnit.
     */
    public static TemporalUnit parseAsChronoUnit(final String name, final String s) {
        final String stringToParse = validateNotNullOrEmpty(name, s);
        try {
            return ChronoUnit.valueOf(stringToParse.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Failed to parse property '%s' with input '%s'.", name, s));
        }
    }
}
