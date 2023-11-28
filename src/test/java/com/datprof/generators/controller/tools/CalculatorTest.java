package com.datprof.generators.controller.tools;

import com.datprof.generators.controller.subgenerators.tools.Calculator;
import com.datprof.generators.model.patterns.ToolPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link Calculator}
 */
class CalculatorTest {

    private Calculator calculator;
    private Map<String, String> properties0;

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setUp() {
        properties0 = new HashMap<>();
        properties0.put("formula", "A+B");
        properties0.put("length","2");
        properties0.put("start","10");
        properties0.put("step","1");
        properties0.put("padding-length","2");
        properties0.put("format","{0}");
        properties0.put("input","0");
    }

    /**
     * Tests the instantiation.
     */
    @Test
    void testInstantiation() {
        ToolPattern pattern = new ToolPattern(properties0);
        calculator = new Calculator(pattern);
        assertNotNull(calculator);
    }

    /**
     * Tests that applyFormula() returns the correct value.
     */
    @Test
    void testApplyFormula() {
        ToolPattern pattern = new ToolPattern(properties0);
        calculator = new Calculator(pattern);
        String values = "12";
        String expected = "3";
        assertEquals(expected, calculator.applyFormula(values));
    }

    /**
     * Tests that generate() returns the correct value.
     */
    @Test
    void testGenerate() {
        ToolPattern pattern = new ToolPattern(properties0);
        calculator = new Calculator(pattern);
        String values = "12";
        String expected = "3";
        assertEquals(expected, calculator.generate(values));
    }
}
