package com.datprof.generators.model.patterns;

import lombok.Getter;
import java.util.Map;

/**
 * This class represents a pattern that is used to generate a tool
 */
@Getter
public class ToolPattern implements IPattern{
    private final Pattern pattern;
    private static final String PROP_FORMULA = "formula";
    private String formulaString;

    /**
     * Constructor for the ToolPattern class that takes a map of properties as input and creates a pattern
     * @param properties
     */
    public ToolPattern(Map<String, String> properties) {
        this.pattern = new Pattern(properties);

        if (properties.containsKey(PROP_FORMULA)) {
            this.formulaString = properties.get(PROP_FORMULA);
        } else {
            this.formulaString = null;
        }

    }

    /**
     * @return the map of properties of this pattern
     */
    public Map<String, String> getProperties() {
        return this.pattern.getProperties();
    }

    /**
     * @return the input value associated with this generator
     */
    public int getInput() {
        return this.pattern.getInput();
    }

    /**
     * @return the format string associated with this generator
     */
    public String getFormat() {
        if(Boolean.TRUE.equals(this.hasFormat()))
            return this.pattern.getFormatString();

        return null;
    }


    /**
     * @return true if the pattern contains a format and false otherwise
     */
    public Boolean hasFormat() {
        return this.pattern.getFormatString() != null;
    }

    /**
     * @return the starting string
     */
    @Override
    public String getStartString() {
        throw new IllegalArgumentException("This pattern does not have a starting string");
    }

    /**
     * @return returns the length value
     */
    @Override
    public int getLength() {
        return this.pattern.getLength();
    }
}
