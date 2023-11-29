package com.utils;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.time.ZonedDateTime;
import java.util.Optional;
import static com.utils.Parsers.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Parsers class provided by DATPROF.
 */
class ParsersTest {

    @Test
    void testParseIntoStringArray() {
        final String[] expected = {"1", "twee", "3", "vier", "5"};
        assertArrayEquals(expected, asStringList("values", "1, twee ,3, vier, 5").toArray());
    }

    @Test
     void testParseIntoStringArrayWithSeparator() {
        final String[] expected = {"1", "twee", "3", "vier", "5"};
        // The separator "|" needs to be escaped as it is a special character in regular expressions!
        assertArrayEquals(expected, asStringList("values", "1| twee |3| vier| 5", "\\|").toArray());
    }

    @Test
    void testParseIntoStringArrayWithNullProperty() {
        final Exception exception = assertThrows(IllegalArgumentException.class,
                () -> asStringList("values", null));
        assertEquals("Missing property 'values'.", exception.getMessage());
    }

    @Test
    void testParseIntoStringArrayWithEmptyProperty() {
        final Exception exception = assertThrows(IllegalArgumentException.class,
                () -> asStringList("values", ""));
        assertEquals("Missing property 'values'.", exception.getMessage());
    }

    @Test
    void testParseIntoDoubles() {
        final Double[] expected = {1.0, 2.5, 3.0, 4.567, 5.0};
        assertArrayEquals(expected, parseAsDoubleList("values", "1, 2.5 ,3, 4.567, 5").toArray());
    }

    @Test
    void testParseIntoDoublesWithSeparator() {
        final Double[] expected = {1.0, 2.5, 3.0, 4.567, 5.0};
        assertArrayEquals(expected, parseAsDoubleList("values", "1@ 2.5 @3@ 4.567@ 5", "@").toArray());
    }

    @Test
    void testParseIntoDoublesWithNullProperty() {
        final Exception exception = assertThrows(IllegalArgumentException.class,
                () -> parseAsDoubleList("values", null));
        assertEquals("Missing property 'values'.", exception.getMessage());
    }

    @Test
    void testParseIntoDoublesWithEmptyProperty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parseAsDoubleList("values", "");
        });
        assertEquals("Missing property 'values'.", exception.getMessage());
    }

    @Test
    void testParseIntoDoublesWithInvalidDouble() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parseAsDoubleList("values", "1, 2.345 ,3, vier, 5");
        });
        assertEquals("Failed to parse property 'values[3]' with input 'vier'.", exception.getMessage());
    }

    @Test
    void testOptionalString() {
        assertEquals(Optional.empty(), asNonEmptyValue(""));
    }

    @Test
    void testParseMissingDouble() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parseAsDouble("name", "");
        });
        assertEquals("Missing property 'name'.", exception.getMessage());
    }

    @Test
    void testParseInvalidDouble() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parseAsDouble("name", "not a double");
        });
        assertEquals("Failed to parse property 'name' with input 'not a double'.", exception.getMessage());
    }

    @Test
    void testParseDouble() {
        assertEquals(12.34, parseAsDouble("name", "12.34"), .0001);
    }


    @Test
    void testParseEmptyString() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parse("x", "", Integer::parseInt);
        });
        assertEquals("Failed to parse property 'x' with input ''.", exception.getMessage());
        //assertEquals(Optional.empty(), parse("x", "", Integer::parseInt));
    }

    @Test
    void testParseFailed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parse("x", "bla", Integer::parseInt);
        });
        assertEquals("Failed to parse property 'x' with input 'bla'.", exception.getMessage());
    }

    @Test
    void testParseNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parse("x", null, Integer::parseInt);
        });
        assertEquals("Failed to parse property 'x' with input <null>.", exception.getMessage());
        //assertEquals(Optional.empty(), parse("x", null, Integer::parseInt));
    }

    @Test
    void testParseSpaces() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            parse("x", "   ", Integer::parseInt);
        });
        assertEquals("Failed to parse property 'x' with input '   '.", exception.getMessage());
        //assertEquals(Optional.empty(), parse("x", "   ", Integer::parseInt));
    }

    @Test
    void testParsePositiveNumber() {
        assertEquals((Integer) 123, parse("x", "123", Integer::parseInt));
    }

    @Test
    void testParseZero() {
        assertEquals((Integer) 0, parse("x", "0", Integer::parseInt));
    }

    @Test
    void testParseNegativeNumber() {
        assertEquals((Integer) (-12), parse("x", "-12", Integer::parseInt));
    }

    @Test
    @Disabled("test dependent on winter/summertime...")
    void testTimeZones() {
        assertDateTimeUTC("2019-01-01T01:02:03Z", parseZDT("yyyy-MM-dd HH:mm:ss", "2019-01-01 01:02:03"));
        assertDateTimeUTC("2019-01-01T01:02:03+01:00", parseZDT("yyyy-MM-dd HH:mm:ss XXX", "2019-01-01 01:02:03 +01:00"));
        assertDateTimeUTC("2019-01-01T01:02:03+01:00", parseZDT("yyyy-MM-dd HH:mm:ss XXX", "2019-01-01 01:02:03 +01:00"));
        assertDateTimeUTC("2019-07-07T01:02:03Z", parseZDT("yyyy-MM-dd HH:mm:ss[ XXX]", "2019-07-07 01:02:03"));
        assertDateTimeUTC("2019-07-07T01:02:03+09:11", parseZDT("yyyy-MM-dd HH:mm:ss[ XXX]", "2019-07-07 01:02:03 +09:11"));
        assertDateTimeUTC("2019-07-07T01:02:03+02:00[Europe/Amsterdam]", parseZDT("yyyy-MM-dd HH:mm:ss[ VV]", "2019-07-07 01:02:03 Europe/Amsterdam"));
        assertDateTimeUTC("2019-01-01T01:02:03+01:00[Europe/Amsterdam]", parseZDT("yyyy-MM-dd HH:mm:ss[ VV]", "2019-01-01 01:02:03 Europe/Amsterdam"));
    }

    private void assertDateTimeUTC(final String expected, final ZonedDateTime actual) {
        // expected is a date/time with optional timezone region or offset in standard ISO format (ISO_ZONED_DATE_TIME)
        assertEquals(ZonedDateTime.parse(expected), ZonedDateTime.from(actual));
    }

    private ZonedDateTime parseZDT(final String pattern, final String value) {
        return parseAsZonedDateTime("x", value, parseAsDateTimeFormatter("x", pattern));
    }
}
