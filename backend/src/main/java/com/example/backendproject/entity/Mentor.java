package com.example.backendproject.entity;

public class Mentor {
    private String name;
    private String surname;
    private String degree;
    private String mentorType;

    public Mentor(){

    }

    public Mentor(String mentorType,String name, String surname, String degree) {
        this.name = name;
        this.surname = surname;
        this.degree = degree;
        this.mentorType = mentorType;
    }

    public String getMentorType() {
        return mentorType;
    }

    public void setMentorType(String mentorType) {
        this.mentorType = mentorType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    @Override
    public String toString() {
        return "Mentor{" +
                "mentorType='" + "mentorType" + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", degree='" + degree + '\'' +
                '}';
    }
}
