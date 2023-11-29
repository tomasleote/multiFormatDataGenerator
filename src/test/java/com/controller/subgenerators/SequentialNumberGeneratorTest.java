package com.controller.subgenerators;

import com.model.patterns.SequentialPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SequentialNumberGenerator}.
 */
class SequentialNumberGeneratorTest {
    @Mock
    SequentialPattern pattern;

    @InjectMocks
    SequentialNumberGenerator generator;

    /**
     * Sets up the test environment.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Map<String, String> properties0 = new HashMap<>();
        properties0.put("step", "2");
        properties0.put("length", "5");
        properties0.put("padding-length", "3");


        properties0.put("length","2");
        properties0.put("start","10");
        properties0.put("step","1");
        properties0.put("padding-length","2");
        properties0.put("format","{0}");
        properties0.put("input","0");

        when(pattern.getStep()).thenReturn(2L);
        when(pattern.getLength()).thenReturn(5);
        when(pattern.getPadding()).thenReturn(3L);
        when(pattern.getInput()).thenReturn(0);

        generator = new SequentialNumberGenerator(pattern);
    }

    /**
     * Tests the constructor.
     */
    @Test
    void testConstructor() {
        assertSame(pattern, generator.getPattern());
    }

    /**
     * Tests the generate method.
     */
    @Test
    void generateTest() {
        assertEquals("005", generator.generate("3"));
        assertEquals("007", generator.generate("5"));
        assertEquals("209", generator.generate("207"));
        assertNull(generator.generate("99999"));
    }

    /**
     * Tests the padding function.
     * @throws Exception
     */
    @Test
    void getPaddingFunctionTest() throws Exception {
        MockitoAnnotations.openMocks(this);

        Method getPaddingFunctionMethod = SequentialNumberGenerator.class.getDeclaredMethod("getPaddingFunction", long.class);
        getPaddingFunctionMethod.setAccessible(true);

        Function<Long, String> func;

        when(pattern.getPadding()).thenReturn(0L);
        func = (Function<Long, String>) getPaddingFunctionMethod.invoke(generator, 0L);
        assertEquals("123", func.apply(123L));
        assertEquals("0", func.apply(0L));

        when(pattern.getPadding()).thenReturn(2L);
        func = (Function<Long, String>) getPaddingFunctionMethod.invoke(generator, 2L);
        assertEquals("01", func.apply(1L));
        assertEquals("99", func.apply(99L));

        when(pattern.getPadding()).thenReturn(3L);
        func = (Function<Long, String>) getPaddingFunctionMethod.invoke(generator, 3L);
        assertEquals("001", func.apply(1L));
        assertEquals("099", func.apply(99L));
    }

}