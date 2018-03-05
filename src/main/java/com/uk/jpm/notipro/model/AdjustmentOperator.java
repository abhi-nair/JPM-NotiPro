package com.uk.jpm.notipro.model;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Enum class for storing the Adjustment operators
 */
public enum AdjustmentOperator {
    ADD("add"),
    SUBTRACT("subtract"),
    MULTIPLY("multiply");
    
    //The operator
    private String adjustmentOperator;
    
    AdjustmentOperator(String adjustmentOperator){
        this.adjustmentOperator = adjustmentOperator;
    }
    
    public String getAdjustmentOperator() {
        return adjustmentOperator;
    }
    
    public static AdjustmentOperator getAdjustmentOperator(String adjustmentOperator) {
        return Arrays.stream(values()).filter(val -> StringUtils.equalsIgnoreCase(val.getAdjustmentOperator(), adjustmentOperator)).findFirst().get();
    }
}
