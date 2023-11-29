package com.model.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Pattern class
 */
class SequentialASCIIPatternTest {
    private Map<String, String> properties;

    /**
     * Set up the properties for the pattern
     */
    @BeforeEach
    void setUp() {
        properties = new HashMap<>();
        properties.put("length", "5");
        properties.put("start", "aaa");
        properties.put("padding-length", "3");
        properties.put("list", "a,b,c");
        properties.put("format", "{1}{2}-{3}");
        properties.put("input", "0");
    }

    /**
     * Test the constructor of the Pattern class
     */
    @Test
    void testConstructor() {
        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);

        assertNotNull(pattern.getPattern());
        assertEquals("aaa", pattern.getStartString());
        assertEquals(5, pattern.getLength());
        assertEquals(3, pattern.getPadding());
        assertEquals("{1}{2}-{3}", pattern.getFormat());
    }

    /**
     * Test the constructor of the Pattern class with an invalid input
     */
    @Test
    void testConstructorNoList() {
        properties.remove("list");
        assertThrows(IllegalArgumentException.class, () -> new SequentialASCIIPattern(properties));
    }

    /**
     * Test the constructor of the Pattern class with no length.
     */
    @Test
    void testConstructorNoLength() {
        properties.remove("length");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new SequentialASCIIPattern(properties));

        String expectedMessage = "For formatting a value the length is needed as well.";
        String actualMessage = exception.getMessage();
        assertEquals(actualMessage, expectedMessage);
    }


    /**
     * Test the constructor of the Pattern class with no padding.
     */
    @Test
    void testConstructorNoPadding() {
        properties.remove("padding-length");

        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);
        assertEquals(0, pattern.getPadding());
    }

    /**
     * Test getting the properties of the pattern.
     */
    @Test
    void testGetProperties() {
        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);

        Map<String, String> actualProperties = pattern.getProperties();
        assertEquals(properties.get("length"), actualProperties.get("length"));
        assertEquals(properties.get("start"), actualProperties.get("start"));
        assertEquals(properties.get("padding-length"), actualProperties.get("padding-length"));
        assertEquals(properties.get("list"), actualProperties.get("list"));
        assertEquals(properties.get("format"), actualProperties.get("format"));
        assertEquals(properties.get("input"), actualProperties.get("input"));
    }

    /**
     * Test getting the format of the pattern.
     */
    @Test
    void testGetFormat() {
        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);
        assertEquals("{1}{2}-{3}", pattern.getFormat());
    }

    /**
     * Test format.
     */
    @Test
    void testHasFormat() {
        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);
        assertTrue(pattern.hasFormat());

        properties.remove("format");
        pattern = new SequentialASCIIPattern(properties);
        assertFalse(pattern.hasFormat());
    }

    /**
     * Test getting the input of the pattern.
     */
    @Test
    void testGetInput() {
        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);
        assertEquals(0, pattern.getInput());
    }

    /**
     * Test getting the start string of the pattern.
     */
    @Test
    void testGetStartString() {
        SequentialASCIIPattern pattern = new SequentialASCIIPattern(properties);
        assertEquals("aaa", pattern.getStartString());
    }



}
