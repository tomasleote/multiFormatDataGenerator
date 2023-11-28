package com.datprof.generators.controller.formatters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import org.junit.jupiter.api.Test;

/**
 * The class contains tests for the class <code>{@link Formatter}</code>.
 */
class FormatterTest {

    @Mock
    Map<Integer, String> mockSubFormatsMap;
    private Formatter formatter;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        formatter = new Formatter("{2}-{1}:{0}aa", mockSubFormatsMap);
    }

    /**
     * Test for method constructor.
     */
    @Test
    void testConstructor() {
        HashMap<Integer, String> integerStringMap = new HashMap<>(1);
        Formatter actualFormatter = new Formatter("Template Format", integerStringMap);

        Map<Integer, String> subFormatsMap = actualFormatter.getSubFormatsMap();
        assertSame(integerStringMap, subFormatsMap);
        assertTrue(subFormatsMap.isEmpty());
        assertEquals("Template Format", actualFormatter.getTemplateFormat());
        assertSame(subFormatsMap, integerStringMap);
    }

    /**
     * Test that checks that correct format is returned.
     */
    @Test
    void testFormat() {
        List<String> values = Arrays.asList("1", "2", "3");
        when(mockSubFormatsMap.containsKey(anyInt())).thenReturn(false);

        String result = formatter.format(values);
        assertNotNull(result);
        assertEquals("3-2:1aa", result);
    }

    /**
     * Test that checks that correct format is returned.
     */
    @Test
    void testFormatValuesList() {
        List<String> values = Arrays.asList("1", "2", "3");
        when(mockSubFormatsMap.containsKey(anyInt())).thenReturn(false);

        List<String> result = formatter.formatValuesList(values);
        assertNotNull(result);
        assertEquals(values.size(), result.size());
    }

    /**
     * Test that checks that correct format is returned.
     */
    @Test
    void testFormatWithValueAndFormat() {
        String number = "123";
        String format = "{0}{1}{2}";
        when(mockSubFormatsMap.containsKey(anyInt())).thenReturn(true);
        when(mockSubFormatsMap.get(anyInt())).thenReturn(format);

        String result = formatter.format(number, format);
        assertNotNull(result);
        assertEquals("123", result);
    }

    /**
     * Test that checks that correct format is returned.
     */
    @Test
    void testFormat2() {
        Formatter formatter = new Formatter("Template Format", new HashMap<>(1));
        assertEquals("Template Format", formatter.format(new ArrayList<>()));
    }

    /**
     * Test that method isDigit is acting accordingly.
     */
    @Test
    void testIsDigit() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Formatter formatter = new Formatter("", null);
        Method isDigitMethod = Formatter.class.getDeclaredMethod("isDigit", char.class);
        isDigitMethod.setAccessible(true);

        boolean result = (boolean) isDigitMethod.invoke(formatter, '9');
        assertTrue(result);

        result = (boolean) isDigitMethod.invoke(formatter, 'A');
        assertFalse(result);
    }

    /**
     * Test that method isGeneratorValue is acting accordingly.
     */
    @Test
    void testIsGeneratorValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Formatter formatter = new Formatter("", null);
        Method isGeneratorValueMethod = Formatter.class.getDeclaredMethod("isGeneratorValue", char.class);
        isGeneratorValueMethod.setAccessible(true);

        boolean result = (boolean) isGeneratorValueMethod.invoke(formatter, '{');
        assertTrue(result);

        result = (boolean) isGeneratorValueMethod.invoke(formatter, '[');
        assertFalse(result);
    }
}

