package com.datprof.generators.controller.subgenerators.tools;

import com.datprof.generators.model.patterns.ToolPattern;
import lombok.Getter;
import redempt.crunch.CompiledExpression;
import redempt.crunch.Crunch;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract class that extends generator and defines a specific type of generator that uses a formula to generate a value
 */
@Getter
public class Tool  {
    private final Function<String, String> function;
    private final String formulaString;

    /** Constructor for the Tool class that takes a pattern as a parameter
     * @param pattern pattern that contains the formula
     */
    public Tool(ToolPattern pattern) {
        if(pattern == null)
            throw new IllegalArgumentException("No pattern has been passes to this generator");
        this.formulaString = pattern.getFormulaString();
        this.function = getToolFunction();
    }

    /**
     * @param number values that represent the input for the generator
     * @return returns the same values but in a map that can be used later to compiute the result
     */
    public Map<Character, Integer> initializeNumberMap(String number) {
        Map<Character, Integer> numberMap = new HashMap<>();

        for (int i = 0; i < number.length(); i++) {
            char letter = (char) ('A' + i);
            int digit = Character.getNumericValue(number.charAt(i));
            numberMap.put(letter, digit);
        }

        return numberMap;
    }

    /**
     * Given a set of values and a formula, it replaces the digits with numerical values
     * @param formula string containing the formula
     * @return string format of the expression
     */
    public String getExpression(String number, String formula) {
        Map<Character, Integer> numMap = initializeNumberMap(String.valueOf(number));
        StringBuilder sb = new StringBuilder();
        for (char c : formula.toCharArray()) {
            if (Character.isLetter(c)) {
                Integer value = numMap.get(c);
                if (value == null) {
                    return null;
                }
                sb.append(numMap.get(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @return a function from values map to expression by appling a formula
     */
    Function<String, String> getToolFunction() {
        String formula = this.formulaString;
        if (formula.equals("")) {
            throw new IllegalArgumentException("No formula was given");
        }

        return values -> {
            if (values==null) {
                throw new IllegalArgumentException("No values have been passed");
            }
            return String.valueOf(this.applyFormula(values));
        };
    }

    /**
     * Evaluates for a given formula and values, it assumes the formula has an equality
     * on which it can evaluate either true or false.
     * @param values
     * @return TRUE or FALSE
     */
    public int applyFormula(String values) {
        String expression = getExpression(values, this.formulaString);
        CompiledExpression compiledExpression = Crunch.compileExpression(expression);
        double result = compiledExpression.evaluate();
        return (int) result;
    }

}
