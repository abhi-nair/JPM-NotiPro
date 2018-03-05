package com.uk.jpm.notipro.model;

/**
 * Created by Abhishek Nair (5th March 2018)
 * Each notification received will be recorded as Sale objects within the DB (currently the internal memory)
 * When the sales notification is for an adjustment, then AdjustmentSale -> child class of this class will be saved
 */
public class Sale {
    private long quantity;
    private double price;
    
    public Sale() {
    }
    
    public Sale(long quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }
    
    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}