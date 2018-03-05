package com.uk.jpm.notipro.model;

import org.apache.commons.lang.StringUtils;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Extends the Sale class -> The adjustmentPrice is stored in price variable of Sale.java and the quantity adjusted
 * is stored in quantity of the Sale.java
 */
public class AdjustmentSale extends Sale {
    private AdjustmentOperator adjustmentOperator;
    private double valueBeforeAdjustment;
    private double valueAfterAdjustment;
    
    
    public AdjustmentSale(long quantity, double price, String adjustmentOperator) {
        super(quantity, price);
        this.adjustmentOperator = AdjustmentOperator.getAdjustmentOperator(StringUtils.trimToEmpty(StringUtils.lowerCase(adjustmentOperator)));
    }
    
    public AdjustmentOperator getAdjustmentOperator() {
        return adjustmentOperator;
    }
    public void setAdjustmentOperator(String adjustmentOperator) {
        this.adjustmentOperator = AdjustmentOperator.valueOf(StringUtils.trimToEmpty(StringUtils.lowerCase(adjustmentOperator)));
    }
    public double getValueBeforeAdjustment() {
        return valueBeforeAdjustment;
    }
    public void setValueBeforeAdjustment(double valueBeforeAdjustment) {
        this.valueBeforeAdjustment = valueBeforeAdjustment;
    }
    public double getValueAfterAdjustment() {
        return valueAfterAdjustment;
    }
    
    public void setValueAfterAdjustment(double valueAfterAdjustment) {
        this.valueAfterAdjustment = valueAfterAdjustment;
    }
    
    /**
     * Wrapper method for better code legibility
     * calls the super.getPrice()
     * @return double
     */
    public double getAdjustmentPrice() {
        return getPrice();
    }
    
    /**
     * Wrapper method for better code legibility
     * calls the super.getQuantity()
     * @return long
     */
    public long getAdjustedtQuantity() {
        return getQuantity();
    }
}