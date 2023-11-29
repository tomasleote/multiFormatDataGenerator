package com.controller.subgenerators;

import com.model.patterns.SequentialASCIIPattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * The class contains tests for the class {@link SequentialASCIIGenerator}.
 */
class SequentialASCIIGeneratorTest {
    @Mock
    SequentialASCIIPattern pattern;

    SequentialASCIIGenerator generator;

    /**
     * Sets up.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(pattern.getPadding()).thenReturn(2L);
        when(pattern.getList()).thenReturn(Arrays.asList("a", "b", "c", "d"));
        when(pattern.getLength()).thenReturn(3);
        generator = new SequentialASCIIGenerator(pattern);
    }

    /**
     * Test for method constructor.
     */
    @Test
    void constructorTest() {
        verify(pattern, times(1)).getList();
        verify(pattern, times(1)).getLength();
    }

    /**
     * Test that correct indexes are returned.
     */
    @Test
    void stringListToIntIndexesListTest() throws Exception {
        Method method = SequentialASCIIGenerator.class.getDeclaredMethod("stringListToIntIndexesList", List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Integer> indexes = (List<Integer>) method.invoke(generator, Arrays.asList("a", "b", "c"));

        assertEquals(Arrays.asList(0, 1, 2), indexes);
    }

    /**
     * Test that correct strings are returned.
     * @throws Exception
     */
    @Test
    void intIndexesToStringListTest() throws Exception {
        Method method = SequentialASCIIGenerator.class.getDeclaredMethod("intIndexesToStringList", List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<String> strings = (List<String>) method.invoke(generator, Arrays.asList(0, 1, 2));

        assertEquals(Arrays.asList("a", "b", "c"), strings);
    }

    /**
     * Test that correct indexes are returned.
     */
    @Test
    void getMaxIndexesIntListTest() throws Exception {
        Method method = SequentialASCIIGenerator.class.getDeclaredMethod("getMaxIndexesIntList");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Integer> maxIndexes = (List<Integer>) method.invoke(generator);

        assertEquals(Arrays.asList(3, 3, 3), maxIndexes);
    }

    /**
     * Test that correct indexes are returned.
     * @throws Exception
     */
    @Test
    void getMinIndexesIntListTest() throws Exception {
        Method method = SequentialASCIIGenerator.class.getDeclaredMethod("getMinIndexesIntList");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Integer> minIndexes = (List<Integer>) method.invoke(generator);

        assertEquals(Arrays.asList(0, 0, 0), minIndexes);
    }

    /**
     * Test that string is properly converted to list.
     * @throws Exception
     */
    @Test
    void stringToListTest() {
        List<String> charList = generator.stringToList("abc");
        assertEquals(Arrays.asList("a", "b", "c"), charList);
    }

    /**
     * Test that list is properly incremented.
     */
    @Test
    void incrementListTest() throws Exception {
        Method method = SequentialASCIIGenerator.class.getDeclaredMethod("incrementList", List.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        List<Integer> incremented = (List<Integer>) method.invoke(generator, Arrays.asList(0, 0, 0));

        assertEquals(Arrays.asList(0, 0, 1), incremented);
    }

    /**
     * Test that it returns correct next string.
     */
    @Test
    void getNextTest() throws Exception {
        Method method = SequentialASCIIGenerator.class.getDeclaredMethod("getNext", String.class);
        method.setAccessible(true);

        String next = (String) method.invoke(generator, "abc");

        assertEquals("abd", next);
    }

    /**
     * Test that it generates correct string.
     */
    @Test
    void generateTest() {
        String generated = generator.generate("abc");
        assertEquals("abd", generated);
    }
}
