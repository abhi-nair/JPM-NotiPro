package com.uk.jpm.notipro.controller;

import com.uk.jpm.notipro.log.NotiProLogUtil;
import com.uk.jpm.notipro.manager.NotificationProcessor;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Entry point for the received notification
 * increments the counter and instructs processor when to log sales report and when to halt the application
 * and log a adjustment history report
 */
public class NotiProController {
    
    //Main processor class which processes the sales notification
    private NotificationProcessor notificationProcessor = new NotificationProcessor();
    //Counter used to identify the number of messages received by the app
    private static int counter = 0;
    
    public String recordSale(String notification) {
        NotiProLogUtil.debug("Entered recordSale()", this.getClass().getName());
    
        NotiProLogUtil.info("Notification received : "+notification, this.getClass().getName());
        //Increments the counter indicating the number of notifications received by the application for processing
        counter+=1;
        NotiProLogUtil.info("Counter incremented, value after incrementing : "+counter, this.getClass().getName());
        
        /**
         * counter%10 == 0 defines the variable to be true after every 10 increments of the counter which indicates that a sales report is to be logged
         * counter == 50 defines passes the variable as true after 50 increments of the counter which indicates that a final adjustment report is to be logged
         */
        NotiProLogUtil.debug("Exiting recordSale()", this.getClass().getName());
        return notificationProcessor.processSaleNotification(notification, counter%10 == 0, counter == 50);
    }
}
