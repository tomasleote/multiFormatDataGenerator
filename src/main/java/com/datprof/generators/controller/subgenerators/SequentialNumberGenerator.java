package com.datprof.generators.controller.subgenerators;

import com.datprof.generators.model.patterns.SequentialPattern;
import com.datprof.generators.utils.Parsers;
import java.util.function.Function;

/**
 * MainGenerator that generates a sequence of incremental numbers.
 */
public class SequentialNumberGenerator extends SubGenerator implements ISubGenerator {
    private final Function<Long, String> paddingFunction;
    private final long step;
    private final long length;

    /**
     * Constructer that initialises the step and the length of the generated values
     * @param pattern object that gives all the properties needed by the generator to be initialised
     */
    public SequentialNumberGenerator(SequentialPattern pattern){
       super(pattern);
       this.step = pattern.getStep();
       this.length = pattern.getLength();
        paddingFunction = getPaddingFunction(pattern.getPadding());
    }

    /**
     * We do not want to format the padding on every iteration, nor do we want to check if padding is 0 for every iteration.
     */
    private static Function<Long, String> getPaddingFunction(final long padding) {
        String leftPadding = String.format("%%0%dd", padding);

        if (padding == 0) {
            return l -> Long.toString(l);
        }
        return l -> String.format(leftPadding, l);
    }

    /**
     * @param pastValue input value that is used to generate the next one in the sequence
     * @return the next value
     */
    public String generate(String pastValue) {
        Long value = Parsers.parseAsLong("NAME",pastValue);
        Long nextValue = value + this.step;
        if(nextValue < Math.pow(10,this.length)){
           return paddingFunction.apply(nextValue);
        }
        return null;
    }
}
