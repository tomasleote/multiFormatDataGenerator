package com.datprof.generators.model.patterns;

import java.util.Map;

/**
 * Interface that inforces some behaviour on all the patterns, useful for using the properties of all the patterns when generating and initialising variables in main generator and template
 */
public interface IPattern {
    /**
     * @return the map of properties of this pattern
     */
    Map<String, String> getProperties();

    /**
     * @return the input value associated with this generator
     */
    int getInput();

    /**
     * @return the format string of this pattern
     */
    String getFormat();

    /**
     * @return true if the pattern contains a format and false otherwise
     */
    Boolean hasFormat();

    /**
     * @return the starting string
     */
    String getStartString();

    /**
     * @return returns the length value
     */
    int getLength();
}
