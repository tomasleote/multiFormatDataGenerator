package com.datprof.generators.controller.formatters;

import lombok.Getter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.System.exit;


/**
 * Formats a given value with a given specific format
 */
public class Formatter {
    private static final Logger LOGGER = Logger.getLogger(Formatter.class.getName());
    @Getter
    private final String templateFormat;
    @Getter
    private Map<Integer,String> subFormatsMap;
    public Formatter(String templateFormat, Map<Integer,String> subFormats) {
        this.subFormatsMap = subFormats;
        this.templateFormat = templateFormat;
    }

    /**
     * This method maps through a list of numbers and changes it to follow the desired pattern
     * @param values
     * @return
     */
    public String format(List<String> values) {
        List<String> formattedValues = formatValuesList(values);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < templateFormat.length(); i++) {
            char c = templateFormat.charAt(i);
            if(Boolean.TRUE.equals(isGeneratorValue(c))){
                i++;
                Character generatorIndex = templateFormat.charAt(i);
                if(Boolean.FALSE.equals(isDigit(generatorIndex))){
                    throw new IllegalArgumentException("The format string is not valid");
                }
                int generatorIndexInt = (int) generatorIndex - 48;
                if(formattedValues.get(generatorIndexInt)==null){
                    return null;
                }
                i++;
                result.append(formattedValues.get(generatorIndexInt));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }


    /**
     * This method maps through a list of numbers and changes it to follow the desired pattern
     * @param values
     * @return
     */
    public List<String> formatValuesList(List<String> values ){
        List<String> formattedValues = new ArrayList<>();
        for(int i = 0; i < values.size(); i++){
            String value = values.get(i);

            //has format
            if(this.subFormatsMap.containsKey(i)){
                String format = this.subFormatsMap.get(i);
                String formattedValue = this.format(value, format);
                formattedValues.add(formattedValue);
            }else{
                formattedValues.add(value);
            }

        }
        return formattedValues;
    }

    /**
     * Same method as above but this one only take one value and applies the format string to it
     * @param number
     * @return
     */
    public String format(String number, String format) {

        StringBuilder result = new StringBuilder();
        int numberIndex = 0;
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if(Boolean.TRUE.equals(isGeneratorValue(c))){
                i++;
                result.append(number.charAt(numberIndex));
                i++;
                numberIndex++;
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    /**
     * Checks if the given char is a digit
     * @param c char to check
     * @return true if it is a digit, false otherwise
     */
    private static Boolean isDigit(char c){
        return Character.isDigit(c);
    }

    /**
     * Checks if the given char is a generator value
     * @param c char to check
     * @return true if it is a generator value, false otherwise
     */
    private static Boolean isGeneratorValue(char c){
        return c == '{';
    }
    
}

