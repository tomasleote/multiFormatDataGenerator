package com.controller;

import com.controller.generators.MainGenerator;
import com.model.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for {@link InputProcessor}.
 */
class InputProcessorTest {

    private InputProcessor inputProcessor;

    /**
     * Sets up the test fixture.
     */
    @BeforeEach
    void setUp() {
        inputProcessor = new InputProcessor("{0}__{1}__{2}__{3}");
    }

    /**
     * Tests {@link InputProcessor#addGeneratorAndPattern(String, Map)} with a valid input.
     */
    @Test
    void addGeneratorAndPatternSequentialNumber() {
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("start","100");
        properties.put("step","1");
        properties.put("padding-length","3");
        properties.put("format","{1}{2}-{3}");
        properties.put("input","0");

        inputProcessor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties);

        assertEquals(1, inputProcessor.getISubGenerators().size());
        assertEquals(1, inputProcessor.getSubPatternsMap().size());
    }

    /**
     * Tests {@link InputProcessor#addGeneratorAndPattern(String, Map)} with a ascii generator.
     */
    @Test
    void addGeneratorAndPatternSequentialASCII() {
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("list","a,b,c");
        properties.put("start","aaa");
        properties.put("padding-length","3");
        properties.put("input","3");
        properties.put("format","{1}--{2}--{3}");

        inputProcessor.addGeneratorAndPattern("SEQUENTIALASCIIGENERATOR", properties);

        assertEquals(1, inputProcessor.getISubGenerators().size());
        assertEquals(1, inputProcessor.getSubPatternsMap().size());
    }

    /**
     * Tests {@link InputProcessor#addGeneratorAndPattern(String, Map)} with a invalid number generator.
     */
    @Test
    void addGeneratorAndPatternInvalidType() {
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("start","100");
        properties.put("step","1");
        properties.put("padding-length","3");
        properties.put("format","{1}{2}-{3}");
        properties.put("input","0");
        assertThrows(IllegalArgumentException.class, () -> inputProcessor.addGeneratorAndPattern("INVALIDGENERATORTYPE", properties));
    }

    /**
     * Tests {@link InputProcessor#addGeneratorAndPattern(String, Map)} to check if it properly initialized.
     */
    @Test
    void initTemplate() {
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("start","100");
        properties.put("step","1");
        properties.put("padding-length","3");
        properties.put("format","{1}{2}-{3}");
        properties.put("input","0");
        inputProcessor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties);

        Template template = inputProcessor.initTemplate();

        assertNotNull(template);
    }

    /**
     * Tests {@link InputProcessor#initMainGenerator()} to check if it properly initialized.
     */
    @Test
    void initMainGenerator() {
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("start","100");
        properties.put("step","1");
        properties.put("padding-length","3");
        properties.put("format","{1}{2}-{3}");
        properties.put("input","0");
        inputProcessor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties);

        Template template = inputProcessor.initTemplate();

        MainGenerator mainGenerator = inputProcessor.initMainGenerator();

        assertNotNull(mainGenerator);
    }

    /**
     * Tests {@link InputProcessor#addGeneratorAndPattern(String, Map)} for exception.
     */
    @Test
    void addGeneratorAndPatternEmptyProperties() {
        Map<String, String> properties = new HashMap<>();
        assertThrows(IllegalArgumentException.class, () -> inputProcessor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties));
    }

    /**
     * Test initTemplate for exception.
     */
    @Test
    void initTemplateNoPatterns() {
        InputProcessor emptyInputProcessor = new InputProcessor("{0}__{1}__{2}__{3}");
        assertThrows(IllegalArgumentException.class, () -> emptyInputProcessor.initTemplate());
    }

    /**
     * Test initTemplate for exception when no format is given.
     */
    @Test
    void initTemplateNoTemplateFormat() {
        InputProcessor noFormatInputProcessor = new InputProcessor("");
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("start","100");
        properties.put("step","1");
        properties.put("padding-length","3");
        properties.put("format","{1}{2}-{3}");
        properties.put("input","0");
        noFormatInputProcessor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties);
        assertThrows(IllegalArgumentException.class, () -> noFormatInputProcessor.initTemplate());
    }

    /**
     * Test initMainGenerator for exception.
     */
    @Test
    void initMainGeneratorNoGenerators() {
        InputProcessor emptyInputProcessor = new InputProcessor("{0}__{1}__{2}__{3}");
        assertThrows(IllegalArgumentException.class, () -> emptyInputProcessor.initMainGenerator());
    }

    /**
     * Test initMainGenerator for exception when no format is given.
     */
    @Test
    void initMainGeneratorNoTemplate() {
        Map<String, String> properties = new HashMap<>();
        properties.put("length","3");
        properties.put("start","100");
        properties.put("step","1");
        properties.put("padding-length","3");
        properties.put("format","{1}{2}-{3}");
        properties.put("input","0");
        inputProcessor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties);
        assertThrows(IllegalArgumentException.class, () -> inputProcessor.initMainGenerator());
    }

}
