package com.uk.jpm.notipro.manager;

import com.uk.jpm.notipro.model.Product;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Class used to log the reports onto the console in specific readable format
 */
public class SalesReportLogger {
    
    private long totalItems;
    private double totalSalesValue;
    
    /**
     * Report that logs the sales report after every 10th notification is recorded and processed
     * @throws InterruptedException
     */
    public void logSalesReport(Map<String, Product> salesStorage) {
        System.out.println(StringUtils.center(" Sales Report ", 44, '*'));
        System.out.println("|Product           |Quantity   |Value(£)   |");
        System.out.println(StringUtils.center(StringUtils.EMPTY, 44, '-'));
        salesStorage.forEach((key, value) -> printLineItem(key, value));
        System.out.println(StringUtils.center(StringUtils.EMPTY, 44, '-'));
        System.out.println(String.format("|%-18s|%11d|%11.2f|","Total Sales", totalItems, totalSalesValue));
        System.out.println(StringUtils.center(StringUtils.EMPTY, 44, '-'));
        System.out.println(StringUtils.center(" End ", 44, '*'));
        System.out.println("\n");
        //Add 1 second pause -> to emulate the reception of every 10 messages in real-time
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Adjustment report that prints every iteration of adjustment that has occurred on the recorded sales
     * @param salesStorage
     */
    public void logAdjustmentReport(Map<String, Product> salesStorage) {
    
        System.out.println("NotiPro - Sales Notification Processor is pausing......\n\n");
    
        System.out.println(StringUtils.center(" Adjustment Report ", 100, '*'));
        salesStorage.forEach((key, value) -> printAdjustment(key, value));
        System.out.println(StringUtils.center(" End ", 100, '*'));
        System.out.println("\n");
        //Add 3 second pause -> to emulate the reception of every 10 messages in real-time
    }
    
    /**
     * Formats the product details into a line that needs to be printed on the table for the sales report
     * @param productType
     * @param product
     */
    public void printLineItem(String productType, Product product) {
        String lineItem = String.format("|%-18s|%11d|%11.2f|", productType, product.getTotalQuantity(), product.getTotalPrice());
        totalItems+=product.getTotalQuantity();
        totalSalesValue+=product.getTotalPrice();
        System.out.println(lineItem);
    }
    
    /**
     * Formats the adjustment details of each product type as line items on the Adjustment report
     * @param productType
     * @param product
     */
    public void printAdjustment(String productType, Product product) {
        if(product.getAdjustmentSales() != null && product.getAdjustmentSales().size() != 0) {
            System.out.println();//newLine
            System.out.println(StringUtils.center(productType, 100));
            System.out.println();//newLine
            product.getAdjustmentSales().stream()
                    .map(adjustmentSale -> String.format("Performed %s £%.2f to %d sales of %s and price adjusted from £%.2f to £%.2f",
                            adjustmentSale.getAdjustmentOperator().getAdjustmentOperator(),
                            adjustmentSale.getAdjustmentPrice(),
                            adjustmentSale.getAdjustedtQuantity(),
                            productType,
                            adjustmentSale.getValueBeforeAdjustment(),
                            adjustmentSale.getValueAfterAdjustment()))
                    .collect(Collectors.toList())
                    .forEach(System.out::println);
            System.out.println(StringUtils.center(StringUtils.EMPTY, 100, '-'));
        }
    }
}
