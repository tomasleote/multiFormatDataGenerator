package com.model.patterns;

import com.utils.Parsers;
import lombok.Getter;
import java.util.List;
import java.util.Map;

/**
 * Class that contains the requirements for a specific generator
 */
@Getter
public class SequentialASCIIPattern implements IPattern{
    private final Pattern pattern;
    private static final String PROP_START = "start";
    private static final String PROP_LENGTH = "length";
    private static final String PROP_PADDING_LENGTH = "padding-length";
    private static final String PROP_LIST= "list";
    private final List<String> list ;
    private String startString;
    private String lengthString;
    private String paddingString;
    private final int length;
    private final long padding;

    /**
     * Constructor for the pattern,
     * @param properties map from the name of the property to the property
     */
    public SequentialASCIIPattern(Map<String, String> properties) {
        if(properties == null){
            throw new IllegalArgumentException("no properties have been passed to this pattern");
        }
        this.pattern= new Pattern(properties);

        if (properties.containsKey(PROP_START)) {
            this.startString = properties.get(PROP_START);
        }else{
            throw new IllegalArgumentException("No starting value has been specified for this pattern");
        }
        if (properties.containsKey(PROP_LIST)) {
            String listString = properties.get(PROP_LIST);
            this.list = Parsers.asStringList(listString, listString);
            } else {
                throw new IllegalArgumentException("No list of values was passed");
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
     * @return true if the pattern contains a format and false otherwise
     */
    public Boolean hasFormat() {
        return this.pattern.getFormatString() != null;
    }

    /**
     * @return the start string of this pattern
     */
    public String getStartString() {
        return this.startString;
    }



}
