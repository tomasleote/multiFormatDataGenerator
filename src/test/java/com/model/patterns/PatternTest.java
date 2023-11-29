package com.model.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the Pattern class
 */
class PatternTest {
    private Map<String, String> properties;

    /**
     * Set up the properties for the pattern
     */
    @BeforeEach
    void setUp() {
        properties = new HashMap<>();
        properties.put("length", "5");
        properties.put("format", "{1}{2}-{3}");
        properties.put("input", "0");
    }

    /**
     * Test the constructor of the Pattern class
     */
    @Test
    void testConstructor() {
        Pattern pattern = new Pattern(properties);

        assertEquals("{1}{2}-{3}", pattern.getFormatString());
        assertEquals(5, pattern.getLength());
        assertEquals(0, pattern.getInput());
    }

    /**
     * Test the constructor of the Pattern class with an invalid length
     */
    @Test
    void testConstructorNoLengthWithFormat() {
        properties.remove("length");

        assertThrows(IllegalArgumentException.class, () -> new Pattern(properties));
    }

    /**
     * Test the constructor of the Pattern class with an invalid format
     */
    @Test
    void testConstructorNoLengthNoFormat() {
        properties.remove("length");
        properties.remove("format");

        Pattern pattern = new Pattern(properties);
        assertEquals(0, pattern.getLength());
    }

    /**
     * Test the constructor of the Pattern class with an invalid input
     */
    @Test
    void testConstructorNoInput() {
        properties.remove("input");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Pattern(properties));

        String expectedMessage = "No input has been specified for this pattern";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test the constructor of the Pattern class with an invalid input
     */
    @Test
    void testConstructorNoProperties() {
        assertThrows(NullPointerException.class, () -> new Pattern(null));
    }

    /**
     * Test getting the properties of the pattern
     */
    @Test
    void testGetProperties() {
        Pattern pattern = new Pattern(properties);

        Map<String, String> actualProperties = pattern.getProperties();
        assertEquals(properties.get("length"), actualProperties.get("length"));
        assertEquals(properties.get("format"), actualProperties.get("format"));
        assertEquals(properties.get("input"), actualProperties.get("input"));
    }

    /**
     * Test getting the length of the pattern
     */
    @Test
    void testGetLength() {
        Pattern pattern = new Pattern(properties);
        assertEquals(5, pattern.getLength());
    }

    /**
     * Test getting the format string of the pattern
     */
    @Test
    void testGetFormatString() {
        Pattern pattern = new Pattern(properties);
        assertEquals("{1}{2}-{3}", pattern.getFormatString());
    }

    /**
     * Test getting the input of the pattern
     */
    @Test
    void testGetInput() {
        Pattern pattern = new Pattern(properties);
        assertEquals(0, pattern.getInput());
    }
}
