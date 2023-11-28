package com.datprof.generators.controller.subgenerators.tools;

import com.datprof.generators.controller.subgenerators.SubGenerator;
import com.datprof.generators.controller.subgenerators.ISubGenerator;
import com.datprof.generators.model.patterns.ToolPattern;

/**
 * Generator that given an evaluation formula and a value checks if the value conforms to the value and returns the value if
 * true and null if not
 */
public class Evaluator extends SubGenerator implements ISubGenerator {
    private final Tool tool;

    /**
     * @param pattern the requirements for this generator
     */
    public Evaluator(ToolPattern pattern) {
        super(pattern);
        this.tool = new Tool(pattern);
    }

    /**
     * Evaluates for a given formula and values, it assumes the formula has an equality
     * on which it can evaluate either true or false.
     * @param values
     * @return TRUE or FALSE
     */
    public String applyFormula(String values){
        double result = this.tool.applyFormula(values);
        int resultInt = (int) result;
        if(resultInt ==1)
            return values;
        return null;
    }
    /**
     * @param values parameter that is processed using the evaluation formula
     * @return the string of the values if evaluation is true, null otherwise
     */
    public String generate(String values) {
       return applyFormula(values);
    }
}
