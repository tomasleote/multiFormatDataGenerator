package com.datprof.generators.model.patterns;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the SequentialPattern class
 */
class SequentialPatternTest {

    /**
     * Test pattern fields.
     */
    @Test
     void testPatternFields() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "100");
        properties.put("step", "10");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "0");

        SequentialPattern pattern = new SequentialPattern(properties);

        assertEquals(100L, pattern.getStart());
        assertEquals(10L, pattern.getStep());
        assertEquals(3, pattern.getLength());
        assertEquals(2L, pattern.getPadding());
        assertEquals(0, pattern.getInput());
    }

    /**
     * Test pattern default values.
     */
    @Test
     void testPatternDefaultValues() {
        Map<String, String> properties = new HashMap<>();
        properties.put("step", "10");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "0");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new SequentialPattern(properties));

        String expectedMessage = "No starting value has been specified for this pattern";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test get start string.
     */
    @Test
     void testGetStartString() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "100");
        properties.put("step", "10");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "0");

        SequentialPattern pattern = new SequentialPattern(properties);

        assertEquals("100", pattern.getStartString());
    }

    /**
     * Test get step string.
     */
    @Test
     void testGetStepString() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "100");
        properties.put("step", "10");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "0");

        SequentialPattern pattern = new SequentialPattern(properties);

        assertEquals("10", pattern.getStepString());
    }

    /**
     * Test get non numeric values
     */
    @Test
     void testNonNumericValues() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "one hundred");
        properties.put("step", "ten");
        properties.put("length", "three");
        properties.put("padding-length", "two");
        properties.put("input", "zero");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SequentialPattern(properties);
        });

        String value = properties.get("length");

        String expectedMessage = "Failed to parse property 'length' with input '" + value.toString() + "'.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test get empty values
     */
    @Test
     void testEmptyValues() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "");
        properties.put("step", "");
        properties.put("length", "");
        properties.put("padding-length", "");
        properties.put("input", "");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SequentialPattern(properties);
        });

        String expectedMessage = "Missing property 'length'.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test get null values
     */
    @Test
     void testNullValues() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", null);
        properties.put("step", null);
        properties.put("length", null);
        properties.put("padding-length", null);
        properties.put("input", null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SequentialPattern(properties);
        });

        System.out.println(exception.getMessage());

        String expectedMessage = "Missing property 'length'.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test get properties.
     */
    @Test
     void testGetProperties() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "100");
        properties.put("step", "1");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "3");
        properties.put("format", "{1}{2}-{3}");

        SequentialPattern pattern = new SequentialPattern(properties);

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("start", "100");
        expectedProperties.put("step", "1");
        expectedProperties.put("length", "3");
        expectedProperties.put("padding-length", "2");
        expectedProperties.put("input", "3");
        expectedProperties.put("format", "{1}{2}-{3}");

        assertEquals(expectedProperties, pattern.getProperties());
    }

    /**
     * Test if it has format.
     */
    @Test
     void testHasFormat() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "100");
        properties.put("step", "10");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "0");
        properties.put("format", "{1}{2}-{3}");

        SequentialPattern pattern = new SequentialPattern(properties);

        assertTrue(pattern.hasFormat());
        assertEquals("{1}{2}-{3}", pattern.getFormat());
    }


    /**
     * Test if it has no format.
     */
    @Test
     void testNoFormat() {
        Map<String, String> properties = new HashMap<>();
        properties.put("start", "100");
        properties.put("step", "10");
        properties.put("length", "3");
        properties.put("padding-length", "2");
        properties.put("input", "0");

        SequentialPattern pattern = new SequentialPattern(properties);

        assertFalse(pattern.hasFormat());
        assertNull(pattern.getFormat());
    }
}

