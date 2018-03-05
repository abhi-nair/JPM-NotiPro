package com.uk.jpm.notipro.manager;

import com.uk.jpm.notipro.log.NotiProLogUtil;
import com.uk.jpm.notipro.model.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Processes each sales notification and records the same.
 * Based on the instruction from the controller logs the sales or adjustment history report
 */
public class NotificationProcessor {
    
    private static Map<String, Product> productsMap = new HashMap<>();
    private static final String SUCCESS = "SUCCESS";
    
    public String processSaleNotification(String notification, boolean logSalesReport, boolean logSalesAdjustmentReport) {
        NotiProLogUtil.debug("Entered processSaleNotification()", this.getClass().getName());
        
        //Record and Process each and every sales notification
        NotificationParser notificationParser = new NotificationParser(notification);
        if(notificationParser.isValidNotification()){
            if(productsMap.containsKey(notificationParser.getProductType())) {
                productsMap.get(notificationParser.getProductType()).recordAndProcessSale(notificationParser.getSalesRecord());
            } else {
                productsMap.put(notificationParser.getProductType(), new Product(notificationParser.getProductType(), notificationParser.getSalesRecord()));
            }
        }
        
        //Check if 10 notifications have been recorded and processed -> if so log a report
        if(logSalesReport) new SalesReportLogger().logSalesReport(productsMap);
        
        //Check if 50 messages have been received --> Assumption end of day or max number of notifications that can be processed has been received and application stops.
        if(logSalesAdjustmentReport) {
            new SalesReportLogger().logAdjustmentReport(productsMap);
            NotiProLogUtil.info("Application stopping..!)", this.getClass().getName());
            System.exit(404); //--> Application stopped = 404 - Not available anymore
        }
        
        //Dummy response to the client accessing our service that processes the sale notification
        NotiProLogUtil.debug("Exiting processSaleNotification()", this.getClass().getName());
        return SUCCESS;
    }
}
