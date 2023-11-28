package com.datprof.generators.controller.tools;

import com.datprof.generators.controller.subgenerators.tools.Evaluator;
import com.datprof.generators.model.patterns.ToolPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link Evaluator}
 */
class EvaluatorTest {

    private Evaluator evaluator;
    private Map<String, String> properties0;

    /**
     * Set up the test environment
     */
    @BeforeEach
    void setUp() {
        properties0 = new HashMap<>();
        properties0.put("formula", "A==B");

        properties0.put("length","2");
        properties0.put("start","10");
        properties0.put("step","1");
        properties0.put("padding-length","2");
        properties0.put("format","{0}");
        properties0.put("input","0");
    }

    /**
     * Test instantiation of {@link Evaluator}
     */
    @Test
    void testInstantiation() {
        ToolPattern pattern = new ToolPattern(properties0);
        evaluator = new Evaluator(pattern);
        assertNotNull(evaluator);
    }

    /**
     * Test {@link Evaluator#applyFormula(String)}
     */
    @Test
    void testApplyFormula() {
        ToolPattern pattern = new ToolPattern(properties0);
        evaluator = new Evaluator(pattern);
        String values = "11"; // A equals to B
        assertEquals(values, evaluator.applyFormula(values));

        values = "12"; // A does not equal to B
        assertNull(evaluator.applyFormula(values));
    }

    /**
     * Test {@link Evaluator#generate(String)}
     */
    @Test
    void testGenerate() {
        ToolPattern pattern = new ToolPattern(properties0);
        evaluator = new Evaluator(pattern);
        String values = "11"; // A equals to B
        assertEquals(values, evaluator.generate(values));

        values = "12"; // A does not equal to B
        assertNull(evaluator.generate(values));
    }

    /**
     * Test {@link Evaluator#applyFormula(String)} with complex formula
     */
    @Test
    void testApplyFormulaWithComplexFormula() {
        properties0.put("formula", "(A+B)==(C+D)");
        ToolPattern pattern = new ToolPattern(properties0);
        evaluator = new Evaluator(pattern);
        String values = "1223"; // (1+2) does not equal to (2+3)
        assertNull(evaluator.applyFormula(values));
    }
}
