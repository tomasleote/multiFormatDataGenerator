package com.datprof.generators.model.patterns;

import com.datprof.generators.utils.Parsers;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

/**
 * Class that contains the requirements for a specific generator
 * Contains a map of properties and a formatter
 */
@Setter
@Getter
public class Pattern {
    private Map<String, String> properties;
    private static final String PROP_FORMAT = "format";
    private static final String PROP_LENGTH = "length";
    private String formatString;
    private String lengthString;
    private final int length;
    private String inputString;
    private static final String PROP_INPUT = "input";
    private final int input;



    /**
     * Constructor for the pattern,
     * Checks if each property is null , parses it if necessary and initialises the properties map and the formatter
     * @param properties map from the name of the property to the property
     */
    public Pattern(Map<String, String> properties) {
        this.properties = properties;

        if (properties.containsKey(PROP_FORMAT)) {
            this.formatString = properties.get(PROP_FORMAT);

            if (!properties.containsKey(PROP_LENGTH)) {
                throw new IllegalArgumentException("For formatting a value the length is needed as well.");
            }
        } else {
            this.formatString = null;
        }

        if (properties.containsKey(PROP_LENGTH)) {
            this.lengthString = properties.get(PROP_LENGTH);
            length = Parsers.parseAsInt(PROP_LENGTH, lengthString);
        } else {
            this.lengthString = null;
            this.length = 0;
        }

        if (properties.containsKey(PROP_INPUT)) {
            this.inputString = properties.get(PROP_INPUT);
            input = Parsers.parseAsInt(PROP_INPUT, inputString);
        } else {
           throw new IllegalArgumentException("No input has been specified for this pattern");
        }

    }

}
