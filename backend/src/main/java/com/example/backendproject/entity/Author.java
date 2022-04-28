package com.example.backendproject.entity;

public class Author {
    private int id;
    private String name;
    private String surname;
    private int studentNo;
    private int typeEdu;

    public Author(){

    }
    public Author(String name, String surname, int studentNo) {
        this.name = name;
        this.surname = surname;
        this.studentNo = studentNo;
        this.typeEdu=defineTypeEdu(studentNo);
    }

    public Author(int id, String name, String surname, int studentNo) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.studentNo = studentNo;
        this.typeEdu=defineTypeEdu(studentNo);
    }

    public Author(int id, String name, String surname, int studentNo, int typeEdu) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.studentNo = studentNo;
        this.typeEdu=typeEdu;
    }

    private int defineTypeEdu(int studentNo){
        int placeMillion=studentNo/1000000;
        placeMillion*=1000;
        int placeThousands=studentNo/1000;
        int tmp=placeThousands-placeMillion;
        if(tmp==201){
            return 1;
        }else{
            return 2;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", studentNo=" + studentNo +
                ", typeEdu='" + typeEdu + '\'' +
                '}';
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

    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public int getTypeEdu() {
        return typeEdu;
    }

    public void setTypeEdu(int typeEdu) {
        this.typeEdu = typeEdu;
    }
}
