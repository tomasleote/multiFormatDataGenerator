package com.controller.generators;

import com.controller.formatters.Formatter;
import com.controller.subgenerators.ISubGenerator;
import com.model.Template;
import com.model.patterns.IPattern;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The class contains tests for the class <code>{@link MainGenerator}</code>.
 */
class MainGeneratorTest {
    @Mock
    Template mockTemplate;

    @Mock
    ISubGenerator mockISubGenerator;

    @Mock
    Formatter mockFormatter;

    @Mock
    IPattern mockIPattern;

    @Mock
    List<String> mockList;

    private MainGenerator mainGenerator;
    private Map<Integer, ISubGenerator> iSubGenerators;
    private Map<Integer, IPattern> patterns;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        iSubGenerators = new HashMap<>();
        iSubGenerators.put(0, mockISubGenerator);
        patterns = new HashMap<>();
        patterns.put(0, mockIPattern);

        // Setting up the mock behaviours
        when(mockIPattern.getInput()).thenReturn(0);
        when(mockIPattern.getStartString()).thenReturn("AAA");
        when(mockIPattern.getLength()).thenReturn(3);
        when(mockISubGenerator.generate(anyString())).thenReturn("AAA");
        when(mockTemplate.getPatternsMap()).thenReturn(patterns);
        when(mockTemplate.getFormatter()).thenReturn(mockFormatter); // Return mock formatter
        when(mockFormatter.format(any())).thenReturn("AAA"); // Set behaviour for mock formatter

        mainGenerator = new MainGenerator(mockTemplate, iSubGenerators);
    }

    /**
     * Tear down.
     */
    @AfterEach
    void tearDown() {
        // Clear invocations after each test case
        clearInvocations(mockTemplate, mockISubGenerator, mockFormatter, mockIPattern, mockList);
    }

    /**
     * Test for method constructor.
     */
    @Test
    void testConstructor() {
        Template template = new Template(new HashMap<>(1), "Template Format");

        MainGenerator actualMainGenerator = new MainGenerator(template, new HashMap<>(1));

        assertTrue(actualMainGenerator.getISubGenerators().isEmpty());
        assertTrue(actualMainGenerator.getValues().isEmpty());
        assertSame(template, actualMainGenerator.getTemplate());
    }

    /**
     * Test for method {@link MainGenerator#generate()}.
     */
    @Test
    void testGenerate() {
        Stream<String> result = mainGenerator.generate();
        assertNotNull(result);
        assertEquals("AAA", result.findFirst().get());
    }

    /**
     * Test that format is correctly called.
     */
    @Test
    void testFormat() {
        // Mock behavior for the list
        when(mockList.size()).thenReturn(1);
        when(mockList.get(0)).thenReturn("AAA");

        String result = mainGenerator.format(mockList);
        assertEquals("AAA", result);
    }

    /**
     * Test initialization of values map.
     */
    @Test
    void testInitValuesMap() {
        // Because initValuesMap() is private, we can't directly test it.
        // However, we can verify its effect by checking that the values map has been initialized.
        Map<Integer, String> values = mainGenerator.getValues();
        assertEquals(1, values.size());
        assertTrue(values.containsKey(0));
        assertEquals("AAA", values.get(0));
    }

    /**
     * Test for method {@link MainGenerator#generateNextValue()} and that correct exception is thrown.
     */
    @Test
    void testGenerateNextValue_Exception() {
        // Setting up the mock behaviour for generating null value
        when(mockISubGenerator.generate(anyString())).thenReturn(null);

        // Create a new MainGenerator because we've modified the mock behavior
        mainGenerator = new MainGenerator(mockTemplate, iSubGenerators);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mainGenerator.generate().findFirst(); // This should throw an exception
        });

        String expectedMessage = "The generator is unable to generate this template.Please recheck your input";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

}

