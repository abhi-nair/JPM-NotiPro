package com.uk.jpm.notipro.model;

import com.uk.jpm.notipro.log.NotiProLogUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Product model class that stored the details of the Sale items
 * productType - type of product is like apples, oranges, etc... Assumption is that only plural forms will be received in the notification
 * singular and plural will be treated as separate product if received since there is no fixed product type mapping
 * totalQuantity - stores the total quantity of sale item recorded for the product
 * totalPrice - Stores the total sale price for the product
 * sales - list of sales recorded for the product
 */
public class Product {
   
    private String productType;
    private long totalQuantity;
    private double totalPrice;
    private List<Sale> sales;
    private List<AdjustmentSale> adjustmentSales;
    
    public Product(String productType, Sale sale) {
        this.productType = productType;
        recordAndProcessSale(sale);
    }
    
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public long getTotalQuantity() {
        return totalQuantity;
    }
    public void setTotalQuantity(long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    public List<Sale> getSales() {
        return sales;
    }
    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
    public List<AdjustmentSale> getAdjustmentSales() {
        return adjustmentSales;
    }
    public void setAdjustmentSales(List<AdjustmentSale> adjustmentSales) {
        this.adjustmentSales = adjustmentSales;
    }
    
    /**
     * Records a sale of the same product type and processes
     * @param sale
     */
    public void recordAndProcessSale(Sale sale) {
        NotiProLogUtil.debug("Entered recordAndProcessSale()", this.getClass().getName());
        
        //Adjustment Sale
        if (sale instanceof AdjustmentSale) {
            AdjustmentSale adjustmentSale = (AdjustmentSale) sale;
            adjustmentSale.setQuantity(getTotalQuantity());
            adjustmentSale.setValueBeforeAdjustment(getTotalPrice());
            
            /*
             * Switch and process based on the operator
             * For each sale recorded for the product -> (sale.price (operartor) adjustedPrice) * sale.quantity = new total
             */
            switch (adjustmentSale.getAdjustmentOperator()) {
                case ADD:
                    setTotalPrice(sales.stream().mapToDouble(s -> performAdd(s.getPrice(), adjustmentSale.getAdjustmentPrice(), s.getQuantity())).sum());
                    break;
                case SUBTRACT:
                    setTotalPrice(sales.stream().mapToDouble(s -> performSubtract(s.getPrice(), adjustmentSale.getAdjustmentPrice(), s.getQuantity())).sum());
                    break;
                case MULTIPLY:
                    setTotalPrice(sales.stream().mapToDouble(s -> performMultiply(s.getPrice(), adjustmentSale.getAdjustmentPrice(), s.getQuantity())).sum());
                    break;
                default:
                    break;
            }
            
            adjustmentSale.setValueAfterAdjustment(getTotalPrice());
            if(adjustmentSales == null) adjustmentSales = new ArrayList<>();
            adjustmentSales.add(adjustmentSale);
        }
        //Non-Adjustment Sale
        else {
            if(sales == null) sales = new ArrayList<>();
            sales.add(sale);
            totalQuantity += sale.getQuantity();
            totalPrice += (sale.getQuantity() * sale.getPrice());
        }
    
        NotiProLogUtil.debug("Exiting recordAndProcessSale()", this.getClass().getName());
    }
    
    /**
     * Performs the Add adjustment
     * @param salePrice
     * @param adjustmentPrice
     * @param quantity
     * @return double
     */
    private double performAdd(double salePrice, double adjustmentPrice, long quantity) {
        NotiProLogUtil.debug("Entered performAdd()", this.getClass().getName());
        BigDecimal bdSalePrice = new BigDecimal(salePrice+StringUtils.EMPTY);
        BigDecimal bdAdjustmentPrice = new BigDecimal(adjustmentPrice+StringUtils.EMPTY);
        BigDecimal bdQuantity = new BigDecimal(quantity+StringUtils.EMPTY);
    
        NotiProLogUtil.debug("Exiting performAdd()", this.getClass().getName());
        return (((bdSalePrice.add(bdAdjustmentPrice)).multiply(bdQuantity)).setScale(2, RoundingMode.HALF_UP)).doubleValue();
    }
    
    /**
     * Performs the Subtract adjustment
     * @param salePrice
     * @param adjustmentPrice
     * @param quantity
     * @return double
     */
    private double performSubtract(double salePrice, double adjustmentPrice, long quantity) {
        NotiProLogUtil.debug("Entered performSubtract()", this.getClass().getName());
        BigDecimal bdSalePrice = new BigDecimal(salePrice+StringUtils.EMPTY);
        BigDecimal bdAdjustmentPrice = new BigDecimal(adjustmentPrice+StringUtils.EMPTY);
        BigDecimal bdQuantity = new BigDecimal(quantity+StringUtils.EMPTY);
        
        NotiProLogUtil.debug("Exiting performSubtract()", this.getClass().getName());
        return (((bdSalePrice.subtract(bdAdjustmentPrice)).multiply(bdQuantity)).setScale(2, RoundingMode.HALF_UP)).doubleValue();
    }
    
    /**
     * Performs the Multiply adjustment
     * @param salePrice
     * @param adjustmentPrice
     * @param quantity
     * @return double
     */
    private double performMultiply(double salePrice, double adjustmentPrice, long quantity) {
        NotiProLogUtil.debug("Entered performMultiply()", this.getClass().getName());
        BigDecimal bdSalePrice = new BigDecimal(salePrice+StringUtils.EMPTY);
        BigDecimal bdAdjustmentPrice = new BigDecimal(adjustmentPrice+StringUtils.EMPTY);
        BigDecimal bdQuantity = new BigDecimal(quantity+StringUtils.EMPTY);
        
        NotiProLogUtil.debug("Exiting performMultiply()", this.getClass().getName());
        return (((bdSalePrice.multiply(bdAdjustmentPrice)).multiply(bdQuantity)).setScale(2, RoundingMode.HALF_UP)).doubleValue();
    }
}