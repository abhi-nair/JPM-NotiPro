package com.uk.jpm.notipro.manager;

import com.uk.jpm.notipro.log.NotiProLogUtil;
import com.uk.jpm.notipro.model.AdjustmentSale;
import com.uk.jpm.notipro.model.Sale;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Abhishek Nair (5th March 2018)
 * The NotificationParser used for parsing string messages to the required application attributes
 * Only 3 pre-defined message types accepted. Else a message logged saying "Invalid notification"
 */
public class NotificationParser {
    
    private String productType;
    private double productPrice;
    private long productQuantity;
    //Parsed product adjustmentOperator (add, subtract, multiply -> fixed set enum or blank means not a Adjustment sale notificaiton)
    private String adjustmentOperator;
    private boolean validNotification;
    
    //Parses the notification and sets the required attributes
    public NotificationParser(String notification) {
        validNotification = parseNotification(notification);
        if(!validNotification) NotiProLogUtil.error("Invalid notification received : "+notification, this.getClass().getName());
    }
    
    /**
     * Parses the message and identifies the message type to set the product details
     * @param notification -> Amounts in notification assumed to be in either £99.99 or £0.99 or 99p format
     * @return false when invalid notification received
     */
    private boolean parseNotification(String notification) {
        NotiProLogUtil.debug("Entered parseNotification()", this.getClass().getName());
        //Blank notification is considered as invalid notification
        if (StringUtils.isBlank(notification)) return false;
    
        //Split the words from the notification
        String[] messageArray = StringUtils.trimToEmpty(notification).split("\\s+");
        String firstWord = StringUtils.lowerCase(messageArray[0]);
    
        if (messageArray.length == 3) {
            if (firstWord.matches("add|subtract|multiply")) {
                NotiProLogUtil.debug("Exiting parseNotification()", this.getClass().getName());
                return parseMessageType3(messageArray);
            } else {
                NotiProLogUtil.debug("Exiting parseNotification()", this.getClass().getName());
                return parseMessageType1(messageArray);
            }
        } else if (messageArray.length == 7 && firstWord.matches("^\\d+")) {
            NotiProLogUtil.debug("Exiting parseNotification()", this.getClass().getName());
            return parseMessageType2(messageArray);
        } else {
            NotiProLogUtil.debug("Exiting parseNotification()", this.getClass().getName());
            return false;
        }
    }
    
    /**
     * Parse Message Type 1
     * Message Type 1 – contains the details of 1 sale E.g apple at 10p
     * @param messageArray
     * @return boolean
     */
    private boolean parseMessageType1(String[] messageArray) {
        NotiProLogUtil.debug("Entered parseMessageType1()", this.getClass().getName());
        productType = parseProductType(StringUtils.trimToEmpty(messageArray[0]));
        productPrice = parsePrice(StringUtils.trimToEmpty(messageArray[2]));
        productQuantity = 1; //Will always be 1 as its a notification denoting single sale of the product
        NotiProLogUtil.debug("Exiting parseMessageType1()", this.getClass().getName());
        return true;
    }
    
    /**
     * Parse Message Type 2
     * Message Type 2 – contains the details of a sale and the number of occurrences of
     * that sale. E.g 20 sales of apples at 10p each.
     * @param messageArray
     * @return boolean
     */
    private boolean parseMessageType2(String[] messageArray) {
        NotiProLogUtil.debug("Entered parseMessageType2()", this.getClass().getName());
        productType = parseProductType(StringUtils.trimToEmpty(messageArray[3]));
        productPrice = parsePrice(StringUtils.trimToEmpty(messageArray[5]));
        productQuantity = Integer.parseInt(StringUtils.trimToEmpty(messageArray[0]));
        NotiProLogUtil.debug("Exiting parseMessageType2()", this.getClass().getName());
        return true;
    }
    
    /**
     * Parse Message Type 3
     * Message Type 3 – contains the details of a sale and an adjustment operation to be
     * applied to all stored sales of this product type. Operations can be add, subtract, or
     * multiply e.g Add 20p apples would instruct your application to add 20p to each sale
     * of apples you have recorded.
     * @param messageArray
     * @return boolean
     */
    private boolean parseMessageType3(String[] messageArray) {
        NotiProLogUtil.debug("Entered parseMessageType3()", this.getClass().getName());
        adjustmentOperator = StringUtils.trimToEmpty(messageArray[0]);
        productType = parseProductType(StringUtils.trimToEmpty(messageArray[2]));
        productQuantity = 0;
        productPrice = parsePrice(StringUtils.trimToEmpty(messageArray[1]));
        NotiProLogUtil.debug("Exiting parseMessageType3()", this.getClass().getName());
        return true;
    }
    
    /**
     * In order to handle singular or product type being sent in the notification for the single sale notification
     * Have handled common cases, an exception needs to be handled when received or a fixed product type needs to be defined.
     * @param productTypeAsReceived
     * @return String
     */
    public String parseProductType(String productTypeAsReceived) {
        NotiProLogUtil.debug("Entered parseProductType()", this.getClass().getName());
        String productType;
        String productTypeWithoutLastChar = StringUtils.substring(productTypeAsReceived,0, productTypeAsReceived.length() - 1);
        
        if (productTypeAsReceived.endsWith("o")) {
            productType = String.format("%soes", productTypeWithoutLastChar);
        } else if (productTypeAsReceived.endsWith("y")) {
            productType = String.format("%sies", productTypeWithoutLastChar);
        } else if (productTypeAsReceived.endsWith("h")) {
            productType = String.format("%shes", productTypeWithoutLastChar);
        } else if (!productTypeAsReceived.endsWith("s")) {
            productType = String.format("%ss", productTypeAsReceived);
        } else {
            productType = String.format("%s", productTypeAsReceived);
        }
    
        NotiProLogUtil.debug("Exiting parseProductType()", this.getClass().getName());
        return StringUtils.trimToEmpty(StringUtils.lowerCase(productType));
    }
    
    /**
     * Parse the price and get only the value
     * £99.99 -> 99.99 or £0.99 -> 0.00 or 99p -> 0.99
     * @param priceAsReceived
     * @return double
     */
    public double parsePrice(String priceAsReceived) {
        NotiProLogUtil.debug("Entered parsePrice()", this.getClass().getName());
        
        double price = Double.parseDouble(priceAsReceived.replaceAll("£|p", ""));
        if (priceAsReceived.contains("p")) {
            price = Double.valueOf(Double.valueOf(price) / Double.valueOf("100"));
        }
    
        NotiProLogUtil.debug("Exiting parsePrice()", this.getClass().getName());
        return price;
    }
    
    /**
     * Obtains the sales record from the Parsed values
     * @return Sale
     */
    public Sale getSalesRecord() {
        NotiProLogUtil.debug("Entered getSalesRecord()", this.getClass().getName());
        
        if(StringUtils.isNotBlank(adjustmentOperator)) {
            NotiProLogUtil.debug("Exiting getSalesRecord()", this.getClass().getName());
            return new AdjustmentSale(this.productQuantity, this.productPrice, this.adjustmentOperator);
        } else {
            NotiProLogUtil.debug("Exiting getSalesRecord()", this.getClass().getName());
            return new Sale(this.productQuantity, this.productPrice);
        }
    }
    
    public String getProductType() {
        return productType;
    }
    public boolean isValidNotification() {
        return validNotification;
    }
}
