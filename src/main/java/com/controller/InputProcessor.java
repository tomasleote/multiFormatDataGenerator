package com.controller;

import com.controller.generators.MainGenerator;
import com.controller.subgenerators.ISubGenerator;
import com.controller.subgenerators.SequentialASCIIGenerator;
import com.controller.subgenerators.SequentialNumberGenerator;
import com.controller.subgenerators.tools.Calculator;
import com.controller.subgenerators.tools.Evaluator;
import com.model.Template;

import com.model.patterns.IPattern;
import com.model.patterns.SequentialASCIIPattern;
import com.model.patterns.SequentialPattern;
import com.model.patterns.ToolPattern;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that takes the input from the user and initialises the patterns and generators
 */
@Getter
public class InputProcessor {
    private final String templateFormatString;
    private final Map<Integer, IPattern> subPatternsMap;
    private final Map<Integer, ISubGenerator> iSubGenerators;
    private MainGenerator mainGenerator;
    private Template template;

    /**
     * Constructor for the InputProcessor class
     * @param templateFormatString the format for the whole result
     * initialises the maps of patterns and generators and the format
     */
    public InputProcessor(String templateFormatString) {
        this.templateFormatString = templateFormatString;
        this.subPatternsMap = new HashMap<>();
        this.iSubGenerators =new HashMap<>();

    }

    /**
     * This method takes the input from the user and initialises the patterns and generators
     * @param genType variable that specifies for a specific generator what type of generator it should be
     * @param properties properties of a specific generator
     *  initialises the a generator and the pattern that is mapped to it and ads them to the existing lists
     */
    public void addGeneratorAndPattern(String genType, Map<String, String> properties ) {
        int index = this.iSubGenerators.size();
        if(properties.isEmpty()){
            throw new IllegalArgumentException("no property list has been passes for this generator");
        }
        switch (genType) {
            case "SEQUENTIALNUMBERGENERATOR":
                SequentialPattern p = new SequentialPattern(properties);
                SequentialNumberGenerator g = new SequentialNumberGenerator(p);
                this.iSubGenerators.put(index, g);
                this.subPatternsMap.put(index, p);
                break;
            case "SEQUENTIALASCIIGENERATOR":
                SequentialASCIIPattern p1 = new SequentialASCIIPattern(properties);
                SequentialASCIIGenerator g1 = new SequentialASCIIGenerator(p1);
                this.iSubGenerators.put(index, g1);
                this.subPatternsMap.put(index, p1);
                break;
            case "EVALUATION":
                ToolPattern p2 = new ToolPattern(properties);
                Evaluator g2 = new Evaluator(p2);
                this.iSubGenerators.put(index, g2);
                this.subPatternsMap.put(index, p2);
                break;
            case "CALCULATION":
                ToolPattern p3 = new ToolPattern(properties);
                Calculator g3 = new Calculator(p3);
                this.iSubGenerators.put(index, g3);
                this.subPatternsMap.put(index, p3);
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: input doesn't match any type of generator and pattern");
        }
    }

    /**
     * Using the map of patterns and the template format string it initialises the template
     * @return the instance of the template
     */
    public Template initTemplate(){
        if(this.subPatternsMap.isEmpty()){
            throw new IllegalArgumentException("Unable to initialise template since no patterns have been passed.");
        }

        if(this.templateFormatString.isEmpty()){
            throw new IllegalArgumentException("Unable to initialise template since no template format has been passed.");

        }
        this.template = new Template(this.subPatternsMap,this.templateFormatString);
        return this.template;
    }

    /**
     * Using the map of generators and the template format string it initialises the template
     * @return the instance of the template
     */
    public MainGenerator initMainGenerator() {
        if (this.iSubGenerators.isEmpty()) {
            throw new IllegalArgumentException("Unable to initialise main generator since no subgenerators have been passed.");
        }

        if (this.template == null) {
            throw new IllegalArgumentException("Unable to initialise main generator since no template has been initialised.");
        }
        this.mainGenerator = new MainGenerator(this.template, this.iSubGenerators);
        return this.mainGenerator;
    }

}
