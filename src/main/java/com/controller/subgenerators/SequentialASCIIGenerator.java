package com.controller.subgenerators;

import com.model.patterns.SequentialASCIIPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that extends generator and defines a specific type of generator that generates a value based on a list of values
 */
public class SequentialASCIIGenerator extends SubGenerator implements ISubGenerator {
    private final int length;
    private final List<Integer> maxIndexesInt;
    private final List<Integer> minIndexesInt;
    private final List<String> list ;

    public SequentialASCIIGenerator(SequentialASCIIPattern pattern){
        super(pattern);
        this.list = pattern.getList();
        if(list.isEmpty()){
            throw new IllegalArgumentException("Pattern for ASCIISequential generator contains an empty list");
        }
        this.length = pattern.getLength();

        if(this.length==0){
            throw new IllegalArgumentException("Pattern for ASCIISequential generator contains an empty length field");
        }
        this.maxIndexesInt=this.getMaxIndexesIntList();
        this.minIndexesInt=this.getMinIndexesIntList();
    }

    /**
     * Converst list of values to list of indexes
     * @param valueList list of values
     * @return returns the list of values as list
     */
    private List<Integer> stringListToIntIndexesList(List<String> valueList){
        List<Integer> intIndexes = new ArrayList<>();
        for(int i = 0 ; i < valueList.size() ; i++){
            String element = valueList.get(i);
            int index = list.indexOf(element);
            if(index == -1){
                throw new IllegalArgumentException("An error occured in converting the string list to indexes list");
            }
            intIndexes.add(index);
        }
        return intIndexes;
    }

    /**
     * Converts list of integers to list of strings
     * @param intIndexes list of integers
     * @return returns the list of integers as list of strings
     */
    private List<String> intIndexesToStringList(List<Integer> intIndexes){
        List<String> values = new ArrayList<>();
        for(int i = 0 ; i <intIndexes.size() ; i++){
            int index = intIndexes.get(i);
            String element = list.get(index);
            values.add(element);
        }

        return values;
    }

    /**
     * Returns a list of maximum indexes
     * @return returns a list of maximum indexes
     */
    private List<Integer> getMaxIndexesIntList() {
        List<Integer> intIndexes = new ArrayList<>();
        int maxValue = ((this.list.size()-1) > 9 ? 9 : this.list.size()-1 );
        for(int i = 0; i< this.length ; i++) {
            intIndexes.add(maxValue);
        }
           return intIndexes;
    }

    /**
     * Returns a list of minimum indexes
     * @return returns a list of minimum indexes
     */
    private List<Integer> getMinIndexesIntList() {
        List<Integer> intIndexes = new ArrayList<>();
        int minValue = 0;
        for(int i = 0; i< this.length ; i++) {
            intIndexes.add(minValue);
        }
        return intIndexes;
    }

    /**
     * Converts a string to a list of strings
     * @param inputString string that will be converted to a list of strings
     * @return returns the string as a list of strings
     */
    public List<String> stringToList(String inputString) {
        List<String> charList = new ArrayList<>();
        for (Character c : inputString.toCharArray()) {
            String letter = "" +c;
            charList.add(letter);
        }
        return charList;
    }

    /**
     * Increments the list of indexes
     * @param intIndexesList list of indexes that will be incremented
     * @return returns the incremented list of indexes
     */
    private List<Integer> incrementList( List<Integer> intIndexesList){
        Boolean condition = true;
        int index = intIndexesList.size()-1 ;
        while(Boolean.TRUE.equals(condition)){
            int element = intIndexesList.get(index);
            if(element < this.list.size()-1){
                element++;
                intIndexesList.set(index, element);
                condition = false;
            }else{
                element = 0;
                intIndexesList.set(index, element);
                index --;
            }
        }
        return intIndexesList;
    }

    /**
     * Returns the next value in the sequence
     * @param pastValue the value that will be used to generate the next value
     * @return returns the next value in the sequence
     */
    private String getNext(String pastValue) {
        if(pastValue == null){
            throw new IllegalArgumentException("Input value passed to SequentialASCIIGenerator is null");
        }
        List<String> valueList = this.stringToList(pastValue);
        List<Integer> intIndexesList = this.stringListToIntIndexesList(valueList);

        if(intIndexesList.equals(this.maxIndexesInt)){
            intIndexesList = this.minIndexesInt;
        }else{
            intIndexesList = incrementList(intIndexesList);
        }

        valueList = this.intIndexesToStringList(intIndexesList);
        StringBuilder sb = new StringBuilder();
        for (String c : valueList) {
            sb.append(c);
        }
       return sb.toString();

    }

    /**
     * Generates a new value based on the past value
     * @param pastValue value that will be used to generate the new value
     * @return returns the new value
     */
    public String generate(String pastValue) {
        return this.getNext(pastValue);
    }
}

