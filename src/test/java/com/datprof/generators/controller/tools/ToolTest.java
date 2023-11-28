package com.datprof.generators.controller.tools;

import com.datprof.generators.controller.subgenerators.tools.Tool;
import com.datprof.generators.model.patterns.ToolPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This class tests the {@link Tool}
 */
class ToolTest {

    private Tool tool;
    private Map<String, String> properties0;

    /**
     * This method is called before each test
     */
    @BeforeEach
    void setUp() {
        properties0 = new HashMap<>();
        properties0.put("formula", "A+B");
        properties0.put("length","3");
        properties0.put("start","100");
        properties0.put("step","1");
        properties0.put("padding-length","3");
        properties0.put("format","{1}{2}-{3}");
        properties0.put("input","0");
    }

    /**
     * This method tests the instantiation of the {@link Tool} class
     */
    @Test
    void testInstantiation() {
        ToolPattern pattern = new ToolPattern(properties0);
        tool = new Tool(pattern);
        assertNotNull(tool);
    }

    /**
     * This method tests the {@link Tool#initializeNumberMap(String)} method
     */
    @Test
    void testInitializeNumberMap() {
        ToolPattern pattern = new ToolPattern(properties0);
        tool = new Tool(pattern);
        Map<Character, Integer> numberMap = tool.initializeNumberMap("12");
        assertEquals(2, numberMap.size());
        assertEquals(1, numberMap.get('A'));
        assertEquals(2, numberMap.get('B'));
    }

    /**
     * This method tests that gets the correct expression of the formula
     */
    @Test
    void testGetExpression() {
        ToolPattern pattern = new ToolPattern(properties0);
        tool = new Tool(pattern);
        String expression = tool.getExpression("12", tool.getFormulaString());
        assertEquals("1+2", expression);
    }

    /**
     * This method test the {@link Tool#getToolFunction()} method
     * @throws Exception
     */
    @Test
    void testGetToolFunction() throws Exception {
        ToolPattern pattern = new ToolPattern(properties0);
        tool = new Tool(pattern);

        Method method = Tool.class.getDeclaredMethod("getToolFunction");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Function<String, String> function = (Function<String, String>) method.invoke(tool);

        String result = function.apply("12");
        assertEquals("3", result);
    }

    /**
     * This method tests the {@link Tool#applyFormula(String)} method
     */
    @Test
    void testApplyFormula() {
        ToolPattern pattern = new ToolPattern(properties0);
        tool = new Tool(pattern);
        int result = tool.applyFormula("12");
        assertEquals(3, result);
    }

    /**
     * This method tests the constructor method with null values
     */
    @Test
    void testConstructor_NullPattern() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Tool(null);
        });

        String expectedMessage = "No pattern has been passes to this generator";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the {@link Tool#getToolFunction()} method with null values
     */
    @Test
    void testGetToolFunction_NullValues() throws Exception {
        ToolPattern pattern = new ToolPattern(properties0);
        tool = new Tool(pattern);

        Method method = Tool.class.getDeclaredMethod("getToolFunction");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        Function<String, String> function = (Function<String, String>) method.invoke(tool);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> function.apply(null));

        String expectedMessage = "No values have been passed";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * This method tests the {@link Tool#getToolFunction()} method with empty values
     */
    @Test
    void testGetToolFunction_EmptyFormula() {
        properties0.put("formula", "");
        ToolPattern pattern = new ToolPattern(properties0);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            tool = new Tool(pattern);
        });

        String expectedMessage = "No formula was given";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

