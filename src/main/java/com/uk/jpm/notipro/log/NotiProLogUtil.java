package com.uk.jpm.notipro.log;

import java.util.logging.*;

/**
 * Created by Abhishek Nair (5th March 2018)
 * A Log Util for logging in the application
 */
public class NotiProLogUtil {
    
    private static Logger logger;
    
    static {
        //Configure the logging.properties for your logger
        int size = 1000000; // 1 Mb
        try {
            logger = Logger.getLogger(NotiProLogUtil.class.getName());
            
            Handler handler = new FileHandler("NotiPro.log", size, 1); //will be created in the classpath
            handler.setFormatter(new SimpleFormatter());
            
            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
            logger.setUseParentHandlers(false);//Disable console Logging
        } catch (Exception e) {
            System.out.println("Exception while loading Logger config");
            e.printStackTrace();
        }
    }
    
    public static void debug(String logMessage, String className) {
        logger.log(Level.FINE, className+" - "+logMessage);
    }
    
    public static void info(String logMessage, String className) {
        logger.log(Level.INFO, className+" - "+logMessage);
    }
    
    public static void error(String logMessage, String className) {
        logger.log(Level.SEVERE, className+" - "+logMessage);
    }
}
