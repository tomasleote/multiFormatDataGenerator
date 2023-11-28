package com.datprof.generators.controller.generators;

import com.datprof.generators.controller.InputProcessor;
import com.datprof.generators.model.Template;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

/**
 * The class contains tests for integration of the project. It test various combinations of generators and patterns.
 */
class GeneratorIntegrationTest {
    private String templateStringFormat;
    private InputProcessor processor;

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() {
        templateStringFormat = "{0}__{1}__{2}__{3}";
        processor = new InputProcessor(templateStringFormat);
    }

    /**
     * Test that checks that correct format is returned for random example. It uses all types of generators and returns
     * values such as "20-8__10AAA__208__a--a--b"
     */
    @Test
    void testFullGeneratorProcess() {
        Map<String, String > properties0 = new HashMap<>();
        properties0.put("length","3");
        properties0.put("start","100");
        properties0.put("step","1");
        properties0.put("padding-length","3");
        properties0.put("format","{1}{2}-{3}");
        properties0.put("input","0");

        String genType0 = "SEQUENTIALNUMBERGENERATOR";

        Map<String, String > properties1 = new HashMap<>();
        properties1.put("formula","A+B+C");
        properties1.put("input","0");
        properties1.put("format","{1}{2}AAA");
        properties1.put("length","2");

        String genType1 = "CALCULATION";

        Map<String, String > properties2 = new HashMap<>();
        properties2.put("formula","A%2==0");
        properties2.put("length","3");
        properties2.put("input","0");

        String genType2 = "EVALUATION";

        Map<String, String > properties3 = new HashMap<>();
        properties3.put("length","3");
        properties3.put("list","a,b,c");
        properties3.put("start","aaa");
        properties3.put("padding-length","3");
        properties3.put("input","3");
        properties3.put("format","{1}--{2}--{3}");

        String genType3 ="SEQUENTIALASCIIGENERATOR";

        processor.addGeneratorAndPattern(genType0, properties0);
        processor.addGeneratorAndPattern(genType1, properties1);
        processor.addGeneratorAndPattern(genType2, properties2);
        processor.addGeneratorAndPattern(genType3, properties3);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        Stream<String> stream = mainGenerator.generate();
        List<String> results = stream.filter(Objects::nonNull).limit(100).collect(Collectors.toList());

        assertEquals(100, results.size());

        // Expected results
        List<String> expectedResults = Arrays.asList(
                "20-8__10AAA__208__a--a--b",
                "20-9__11AAA__209__a--a--c",
                "21-7__10AAA__217__a--b--a",
                "21-8__11AAA__218__a--b--b"
                //... Add more expected results here if needed
        );

        // Comparing the output to the expected results
        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i), results.get(i));
        }
    }

    /**
     * Test the BSN number example. It generates 5 BSN numbers and checks that the last digit is correct and these are
     * correct bsn numbers.
     */
    @Test
    void testBSNNumber() {
        String templateStringFormat = "{0}";

        InputProcessor processor = new InputProcessor(templateStringFormat);

        // BSN generator sequential number generator
        Map<String, String> properties0 = new HashMap<>();
        properties0.put("length", "9");
        properties0.put("start", "100000000");
        properties0.put("step", "1");
        properties0.put("padding-length", "0");
        properties0.put("input", "0");

        processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties0);

        // Evaluator to check the last digit
        Map<String, String> properties1 = new HashMap<>();
        String formula = "(9*A + 8*B + 7*C + 6*D + 5*E + 4*F + 3*G + 2*H -I) %11 == 0";
        properties1.put("formula", formula);
        properties1.put("input", "0");

        processor.addGeneratorAndPattern("EVALUATION", properties1);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        List<String> expectedResults = Arrays.asList(
                "100000009",
                "100000010",
                "100000022",
                "100000034",
                "100000046"
        );

        Stream<String> stream = mainGenerator.generate();
        List<String> results = stream.filter(Objects::nonNull).limit(5).collect(Collectors.toList());
        for (int i = 0; i < expectedResults.size(); i++) {
            System.out.println(results.get(i));   //For demo purposes
            assertEquals(expectedResults.get(i), results.get(i));
        }
    }

    /**
     * Test the NLD license plate example. It generates 5 license plates and checks that these are correct license plates.
     */
    @Test
    void testNLLicensePlate() {
        String templateStringFormat = "{0}-{1}-{2}";

        InputProcessor processor = new InputProcessor(templateStringFormat);

        // ASCII generator for single character
        Map<String, String> asciiPropertiesSingle = new HashMap<>();
        asciiPropertiesSingle.put("length", "1");
        asciiPropertiesSingle.put("start", "A");
        asciiPropertiesSingle.put("step", "1");
        asciiPropertiesSingle.put("padding-length", "0");
        asciiPropertiesSingle.put("input", "0");
        asciiPropertiesSingle.put("list", "A, B, C, D, E, F");
        processor.addGeneratorAndPattern("SEQUENTIALASCIIGENERATOR", asciiPropertiesSingle);

        // Number generator for two-digit number
        Map<String, String> numberProperties = new HashMap<>();
        numberProperties.put("length", "2");
        numberProperties.put("start", "10");
        numberProperties.put("step", "1");
        numberProperties.put("padding-length", "0");
        numberProperties.put("input", "1");
        processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", numberProperties);

        // ASCII generator for three characters
        Map<String, String> asciiPropertiesTriple = new HashMap<>();
        asciiPropertiesTriple.put("length", "3");
        asciiPropertiesTriple.put("start", "AAA");
        asciiPropertiesTriple.put("step", "1");
        asciiPropertiesTriple.put("padding-length", "0");
        asciiPropertiesTriple.put("input", "2");
        asciiPropertiesTriple.put("list", "A, B, C, D, E, F");
        processor.addGeneratorAndPattern("SEQUENTIALASCIIGENERATOR", asciiPropertiesTriple);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        List<String> expectedResults = Arrays.asList(
                "B-11-AAB",
                "C-12-AAC",
                "D-13-AAD",
                "E-14-AAE",
                "F-15-AAF"
        );

        Stream<String> stream = mainGenerator.generate();
        List<String> results = stream.filter(Objects::nonNull).limit(5).collect(Collectors.toList());
        for (int i = 0; i < expectedResults.size(); i++) {
            System.out.println(results.get(i));  //For demo purposes
            assertEquals(expectedResults.get(i), results.get(i));
        }
    }

    /**
     * Test the Luhn number example. It generates 5 Luhn numbers and checks that these are correct Luhn numbers.
     */
    @Test
    void testLuhnNumber() {
        String templateStringFormat = "{0}{1}";

        InputProcessor processor = new InputProcessor(templateStringFormat);

        // Number generator for payload
        Map<String, String> numberProperties = new HashMap<>();
        numberProperties.put("length", "10"); // adjust this according to the length of payload you need
        numberProperties.put("start", "1000000000");
        numberProperties.put("step", "1");
        numberProperties.put("padding-length", "0");
        numberProperties.put("input", "0");
        processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", numberProperties);

        // Evaluator for Luhn check digit
        Map<String, String> luhnCheckDigitProperties = new HashMap<>();
        String formula = "10 - ((A*2 % 9 + C*2 % 9 + E*2 % 9 + G*2 % 9 + I*2 % 9) + B + D + F + H + J) % 10";
        luhnCheckDigitProperties.put("formula", formula);
        luhnCheckDigitProperties.put("input", "0");
        processor.addGeneratorAndPattern("EVALUATION", luhnCheckDigitProperties);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        // Generate 5 Luhn numbers for testing
        Stream<String> stream = mainGenerator.generate();
        List<String> results = stream.filter(Objects::nonNull).limit(5).collect(Collectors.toList());

        List<String> expectedResults = Arrays.asList(
                "10000000071000000007",
                "10000000151000000015",
                "10000000231000000023",
                "10000000311000000031",
                "10000000491000000049"
        );

        // Assert that all generated numbers are valid Luhn numbers
        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i), results.get(i));
        }
    }

    /**
     * Test that the evaluation generator fails when the formula is invalid.
     */
    @Test
    void testGeneratorFailure() {
        String templateStringFormat = "{0}";

        InputProcessor processor = new InputProcessor(templateStringFormat);

        // A generator that always produces numbers
        Map<String, String> properties0 = new HashMap<>();
        properties0.put("length", "10");
        properties0.put("start", "1000000000");
        properties0.put("step", "1");
        properties0.put("padding-length", "0");
        properties0.put("input", "0");
        processor.addGeneratorAndPattern("SEQUENTIALNUMBERGENERATOR", properties0);

        // An evaluator that always fails
        Map<String, String> properties1 = new HashMap<>();
        String formula = "A != A";  // This condition always evaluates to false
        properties1.put("formula", formula);
        properties1.put("input", "0");
        processor.addGeneratorAndPattern("EVALUATION", properties1);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        assertThrows(IllegalArgumentException.class, () -> {
            Stream<String> stream = mainGenerator.generate();
            stream.filter(Objects::nonNull).limit(1000).collect(Collectors.toList());
        });
    }


    @Test
    void testASCIIGeneratorAndEvaluatorPatterns() {
        String templateStringFormat = "{0}";

        InputProcessor processor = new InputProcessor(templateStringFormat);

        // ASCII generator for three characters
        Map<String, String> asciiPropertiesTriple = new HashMap<>();
        asciiPropertiesTriple.put("length", "3");
        asciiPropertiesTriple.put("start", "AAA");
        asciiPropertiesTriple.put("padding-length", "0");
        asciiPropertiesTriple.put("input", "0");
        asciiPropertiesTriple.put("list", "A, B, C, D, E, F");
        processor.addGeneratorAndPattern("SEQUENTIALASCIIGENERATOR", asciiPropertiesTriple);

        // Evaluator for ascii number
        Map<String, String> evaluatorProperties = new HashMap<>();
        String formula = "A == B";
        evaluatorProperties.put("formula", formula);
        evaluatorProperties.put("input", "0");
        processor.addGeneratorAndPattern("EVALUATION", evaluatorProperties);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        // Generate 5 numbers for testing
        Stream<String> stream = mainGenerator.generate();
        List<String> results = stream.filter(Objects::nonNull).limit(5).collect(Collectors.toList());
        for (String str : results) {
            System.out.println(str);
        }
        List<String> expectedResults = Arrays.asList(
                "AAB",
                "AAC",
                "AAD",
                "AAE",
                "AAF"
        );

        // Assert that all generated numbers are valid Luhn numbers
        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i), results.get(i));
        }
    }


    @Test
    void testASCIIGeneratorAndCalculatorPatterns() {
        String templateStringFormat = "{1}";

        InputProcessor processor = new InputProcessor(templateStringFormat);

        // ASCII generator for three characters
        Map<String, String> asciiPropertiesTriple = new HashMap<>();
        asciiPropertiesTriple.put("length", "3");
        asciiPropertiesTriple.put("start", "AAA");
        asciiPropertiesTriple.put("padding-length", "0");
        asciiPropertiesTriple.put("input", "0");
        asciiPropertiesTriple.put("list", "A, B, C, D, E, F");
        processor.addGeneratorAndPattern("SEQUENTIALASCIIGENERATOR", asciiPropertiesTriple);

        // Evaluator for ascii number
        Map<String, String> evaluatorProperties = new HashMap<>();
        String formula = "A+B+C";
        evaluatorProperties.put("formula", formula);
        evaluatorProperties.put("input", "0");
        processor.addGeneratorAndPattern("CALCULATION", evaluatorProperties);

        Template template = processor.initTemplate();
        MainGenerator mainGenerator = processor.initMainGenerator();

        // Generate 5 numbers for testing
        Stream<String> stream = mainGenerator.generate();
        List<String> results = stream.filter(Objects::nonNull).limit(5).collect(Collectors.toList());
        for (String str : results) {
            System.out.println(str);
        }
        List<String> expectedResults = Arrays.asList(
                "31",
                "32",
                "33",
                "34",
                "35"
        );

        // Assert that all generated numbers are valid Luhn numbers
        for (int i = 0; i < expectedResults.size(); i++) {
            assertEquals(expectedResults.get(i), results.get(i));
        }
    }

}
