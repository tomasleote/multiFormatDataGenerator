package com.datprof.generators.controller.subgenerators;

/**
 * Interface that defines the methods that a subgenerator should implement
 */
public interface ISubGenerator {

    /**
     * Generates a value based on the past value
     * @param pastValue value that will be used to generate the new value
     * @return returns the generated value
     */
    String generate(String pastValue);
}
