package com.datprof.generators.model.patterns;

import com.datprof.generators.utils.Parsers;
import lombok.Getter;
import java.util.Map;

/**
 * This class represents a sequential pattern.
 */
@Getter
public class SequentialPattern implements IPattern{
    private final Pattern pattern;
    private static final String PROP_START = "start";
    private static final String PROP_START_STRING = "startString";
    private static final String PROP_STEP = "step";
    private static final String PROP_PADDING_LENGTH = "padding-length";
    private static final String PROP_LENGTH = "length";

    private String startString;
    private String stepString;
    private String lengthString;
    private String paddingString;
    private final long start;
    private final long step;
    private final int length;
    private final long padding;

    /**
     * Constructor for a sequential pattern.
     * @param properties Map of properties that define the behaviour of this generator.
     */
    public SequentialPattern(Map<String, String> properties) {
        this.pattern= new Pattern(properties);

        if (properties.containsKey(PROP_START)) {
            this.startString = properties.get(PROP_START);
            start = Parsers.parseAsLong(PROP_START, startString);
        }else{
        throw new IllegalArgumentException("No starting value has been specified for this pattern");
        }

        if (properties.containsKey(PROP_STEP)) {
            this.stepString = properties.get(PROP_STEP);
            step = Parsers.parseAsLong(PROP_STEP, stepString);
        } else {
            throw new IllegalArgumentException("No step value has been specified for this pattern");
        }

        if (properties.containsKey(PROP_LENGTH)) {
            this.lengthString = properties.get(PROP_LENGTH);
            length = Parsers.parseAsInt(PROP_LENGTH, lengthString);
        } else {
            throw new IllegalArgumentException("No length has been specified for this pattern");

        }

        if (properties.containsKey(PROP_PADDING_LENGTH)) {
            this.paddingString = properties.get(PROP_PADDING_LENGTH);
            padding = Parsers.parseAsLong(PROP_PADDING_LENGTH, paddingString);
        } else {
            this.paddingString = null;
            this.padding = 0;
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
     * @return the format string of this pattern
     */
    public String getFormat() {
        if(Boolean.TRUE.equals(this.hasFormat()))
            return this.pattern.getFormatString();

        return null;
    }

    /**
     * @return true if the patterns contains a format string, otherwise false
     */
    public Boolean hasFormat() {
        return this.pattern.getFormatString() != null;
    }


    /**
     * @return the starting string
     */
    public String getStartString() {
        return this.startString;
    }
}
