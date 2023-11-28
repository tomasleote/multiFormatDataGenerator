package com.datprof.generators.model;

import com.datprof.generators.controller.formatters.Formatter;
import com.datprof.generators.model.patterns.IPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Test class for the Template class
 */
class TemplateTest {
    private Map<Integer, IPattern> patternsMap;
    private Formatter formatter;
    private Template template;

    /**
     * Set up the properties for the pattern
     */
    @BeforeEach
    void setUp() {
        patternsMap = new HashMap<>();
        IPattern pattern1 = Mockito.mock(IPattern.class);
        IPattern pattern2 = Mockito.mock(IPattern.class);
        when(pattern1.getFormat()).thenReturn("{1}{2}-{3}");
        when(pattern2.getFormat()).thenReturn("{3}{4}-{5}");
        patternsMap.put(1, pattern1);
        patternsMap.put(2, pattern2);

        formatter = Mockito.mock(Formatter.class);
        when(formatter.format(any())).thenReturn("formattedString");

        template = new Template(patternsMap, "{1}-{2}");
    }

    /**
     * Test the constructor of the Template class
     */
    @Test
    void testConstructor() {
        assertNotNull(template);
    }

    /**
     * Test the constructor of the Template class with an invalid pattern
     */
    @Test
    void testConstructorNoPattern() {
        assertThrows(NullPointerException.class, () -> new Template(null, "{1}-{2}"));
    }

    /**
     * Test getting the pattern map
     */
    @Test
    void testGetPatternsMap() {
        assertEquals(patternsMap, template.getPatternsMap());
    }

    /** This test ensures that the initFormatter method was called without exception,
     *  which indicates that the getTemplateFormat and getSubFormatsMap methods were executed successfully.
     */
    @Test
    void testInitFormatter() {
        assertDoesNotThrow(() -> new Template(patternsMap, "{1}-{2}"));
    }
}
