package com.datprof.generators.controller.subgenerators;

import com.datprof.generators.model.patterns.IPattern;
import lombok.Getter;

/**
 * Abstract class that is inherited by all subGenerators and that contains common functionality
 */
@Getter
public abstract class SubGenerator {
    protected final IPattern pattern;

    /**
     * @param pattern Object that contains values that help to initialise a generator and also help when generating
     */
    protected SubGenerator(IPattern pattern) {
        this.pattern = pattern;
    }


}
