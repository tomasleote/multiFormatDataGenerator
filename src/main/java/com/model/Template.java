package com.model;

import com.controller.formatters.Formatter;
import com.model.patterns.IPattern;
import lombok.Getter;
import lombok.Setter;
import java.util.HashMap;
import java.util.Map;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Template class
 */
@Getter
@Setter
public class Template {
    private Formatter formatter;
    private Map<Integer, IPattern> patternsMap;

    /**
     * Constructor for Template class
     * @param patternsMap map of patterns
     * @param templateFormat template format
     */
    public Template(Map<Integer,IPattern> patternsMap ,String templateFormat) {
        this.patternsMap = patternsMap;
        initFormatter(templateFormat);
    }

    /**
     * Method for initializing formatter
     * @param templateFormat template format
     */
    private void initFormatter(String templateFormat){
        Map<Integer,String> subFormatsMap = new HashMap<>();
        for (Map.Entry<Integer, IPattern> entry : this.patternsMap.entrySet()){
            String format = entry.getValue().getFormat();
            int index = entry.getKey();
            if (!isNullOrEmpty(format)){
                subFormatsMap.put(index,format);
            }
        }
        this.formatter = new Formatter(templateFormat, subFormatsMap);
    }

}
