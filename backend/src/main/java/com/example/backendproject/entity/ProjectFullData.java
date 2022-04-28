package com.example.backendproject.entity;

import java.util.ArrayList;

public class ProjectFullData {
    private String userName;
    private String projectName;
    private String deliveryPeriod;
    private String abstractForPdf;
    private String lessonType;
    private ArrayList<Author> authors;
    private ArrayList<Mentor> mentors;

    public ProjectFullData() {
    }

    public ProjectFullData(String userName, String projectName, String deliveryPeriod,
                           String abstractForPdf, String lessonType, ArrayList<Author> authors,
                           ArrayList<Mentor> mentors) {
        this.userName = userName;
        this.projectName = projectName;
        this.deliveryPeriod = deliveryPeriod;
        this.abstractForPdf = abstractForPdf;
        this.lessonType = lessonType;
        this.authors = authors;
        this.mentors = mentors;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDeliveryPeriod() {
        return deliveryPeriod;
    }

    public void setDeliveryPeriod(String deliveryPeriod) {
        this.deliveryPeriod = deliveryPeriod;
    }

    public String getAbstractForPdf() {
        return abstractForPdf;
    }

    public void setAbstractForPdf(String abstractForPdf) {
        this.abstractForPdf = abstractForPdf;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(ArrayList<Author> authors) {
        this.authors = authors;
    }

    public ArrayList<Mentor> getMentors() {
        return mentors;
    }

    public void setMentors(ArrayList<Mentor> mentors) {
        this.mentors = mentors;
    }
}
