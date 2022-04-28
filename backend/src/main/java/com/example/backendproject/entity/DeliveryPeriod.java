package com.example.backendproject.entity;

public class DeliveryPeriod {
    private String deliveryPeriod;
    private int deliveryYear;
    public DeliveryPeriod(){

    }
    public DeliveryPeriod(String deliveryPeriod, int deliveryYear) {
        this.deliveryPeriod = deliveryPeriod;
        this.deliveryYear = deliveryYear;
    }
    public DeliveryPeriod(String deliveryPeriod){
        this.deliveryPeriod=deliveryPeriod;
    }

    public String getDeliveryPeriod() {
        return deliveryPeriod;
    }

    public void setDeliveryPeriod(String deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    public int getDeliveryYear() {
        return deliveryYear;
    }

    public void setDeliveryYear(int deliveryYear) {
        this.deliveryYear = deliveryYear;
    }
}
