package com.datprof.generators.controller.subgenerators.tools;

import com.datprof.generators.controller.subgenerators.SubGenerator;
import com.datprof.generators.controller.subgenerators.ISubGenerator;
import com.datprof.generators.model.patterns.ToolPattern;

/**
 * a generator that extends class tool and that given a formula and a value , applies the formula to the value and return the result
 */
public class Calculator extends SubGenerator implements ISubGenerator {
    private final Tool tool;
    public Calculator(ToolPattern pattern) {
        super(pattern);
        this.tool = new Tool(pattern);
    }

    /**
     * @param values values that have to be fed into the formula
     * @return a string representing the final result
     */
    public String applyFormula(String values){
        double result = this.tool.applyFormula(values);
        int resultInt = (int) result;
        return String.valueOf(resultInt);
    }

    /**
     * @param values parameter that is processed used the formula
     * @return the final result after the calculation
     */
    public String generate(String values) {
        return applyFormula(values);
    }
}