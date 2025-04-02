package com.example.formadmission;

public class Student {
    private int studentid;
    private String firstName;
    private String city;

    public Student(int studentid, String firstName, String city) {
        this.studentid = studentid;
        this.firstName = firstName;
        this.city = city;
    }
    public int getStudentid(){
        return studentid;
    }
    public String getStudentNumber() {
        return String.valueOf(studentid);
    }

    public String getfirstName() {
        return firstName;
    }

    public String getCity() {
        return city;
    }
}

