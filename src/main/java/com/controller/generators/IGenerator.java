package com.controller.generators;

import java.util.stream.Stream;

/**
 * Interface for generators.
 */
public interface IGenerator {
    /**
     * Create a stream that will generate the values.
     * @return A stream of generated values.
     */
    Stream<String> generate();
}
