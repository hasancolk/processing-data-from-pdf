package com.example.backendproject.entity;

public class Project {

    private int id;
    private int userId;
    private String name;
    private String deliveryPeriod;
    private String summary;
    private String lessonName;
    private String pdfAddress;

    public Project(int id, int userId, String name, String deliveryPeriod, String summary, String lessonName, String pdfAddress) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.deliveryPeriod = deliveryPeriod;
        this.summary = summary;
        this.lessonName = lessonName;
        this.pdfAddress = pdfAddress;
    }

    public Project(int userId, String name, String deliveryPeriod, String summary, String lessonName, String pdfAddress) {
        this.userId = userId;
        this.name = name;
        this.deliveryPeriod = deliveryPeriod;
        this.summary = summary;
        this.lessonName = lessonName;
        this.pdfAddress = pdfAddress;
    }

    public Project() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeliveryPeriod() {
        return deliveryPeriod;
    }

    public void setDeliveryPeriod(String deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getPdfAddress() {
        return pdfAddress;
    }

    public void setPdfAddress(String pdfAddress) {
        this.pdfAddress = pdfAddress;
    }
}
