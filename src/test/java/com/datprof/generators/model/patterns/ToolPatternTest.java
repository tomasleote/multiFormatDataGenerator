package com.datprof.generators.model.patterns;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for the ToolPattern class
 */
class ToolPatternTest {

    private Map<String, String> properties;
    private String PROP_FORMULA;

    /**
     * Set up the properties for the pattern
     */
    @BeforeEach
    void setUp() throws Exception {
        properties = new HashMap<>();
        properties.put("length", "3");
        properties.put("start", "100");
        properties.put("step", "1");
        properties.put("padding-length", "3");
        properties.put("format", "{1}{2}-{3}");
        properties.put("input", "0");

        // Use Reflection API to get PROP_FORMULA
        Field field = ToolPattern.class.getDeclaredField("PROP_FORMULA");
        field.setAccessible(true);
        PROP_FORMULA = (String) field.get(null);
    }

    /**
     * Test the constructor of the ToolPattern class without a formula
     */
    @Test
     void testConstructorWithoutFormula() {
        ToolPattern toolPattern = new ToolPattern(properties);
        assertNull(toolPattern.getFormulaString());
    }

    /**
     * Test the constructor of the ToolPattern class with a formula
     */
    @Test
     void testConstructorWithFormula() {
        properties.put(PROP_FORMULA, "A+B+C");
        ToolPattern toolPattern = new ToolPattern(properties);
        assertEquals("A+B+C", toolPattern.getFormulaString());
    }

    /**
     * Test get format string
     */
    @Test
     void testGetFormat() {
        ToolPattern toolPattern = new ToolPattern(properties);
        assertEquals("{1}{2}-{3}", toolPattern.getFormat());
    }

    /**
     * Test if it has format
     */
    @Test
     void testHasFormat() {
        ToolPattern toolPattern = new ToolPattern(properties);
        assertTrue(toolPattern.hasFormat());
    }

    /**
     * Test get properties
     */
    @Test
     void testGetProperties() {
        properties.put(PROP_FORMULA, "A+B+C");
        ToolPattern toolPattern = new ToolPattern(properties);

        Map<String, String> expectedProperties = new HashMap<>();
        expectedProperties.put("length", "3");
        expectedProperties.put("start", "100");
        expectedProperties.put("step", "1");
        expectedProperties.put("padding-length", "3");
        expectedProperties.put("format", "{1}{2}-{3}");
        expectedProperties.put("input", "0");
        expectedProperties.put(PROP_FORMULA, "A+B+C");

        assertEquals(expectedProperties, toolPattern.getProperties());
    }

    /**
     * Test get input
     */
    @Test
     void testGetInput() {
        ToolPattern toolPattern = new ToolPattern(properties);
        assertEquals(0, toolPattern.getInput());
    }

    /**
     * Test get length
     */
    @Test
     void testGetLength() {
        ToolPattern toolPattern = new ToolPattern(properties);
        assertEquals(3, toolPattern.getLength());
    }

    /**
     * Test get start string
     */
    @Test
     void testGetStartStringException() {
        ToolPattern toolPattern = new ToolPattern(properties);
        Exception exception = assertThrows(IllegalArgumentException.class, toolPattern::getStartString);

        String expectedMessage = "This pattern does not have a starting string";
        String actualMessage = exception.getMessage();

        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

}
