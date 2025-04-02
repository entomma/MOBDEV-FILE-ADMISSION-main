package com.example.formadmission;

public class Student {
    private int studentid;
    private String firstName;
    private String address;
    private String zip;
    private String province;
    private String city;
    private String barangay;
    private  String street;
    private String fullName;
    private String email;

    private String phone_number;

    public Student(int studentid, String fullName, String firstName, String address, String city, String zip, String province, String barangay, String street, String email, String phone_number) {
        this.studentid = studentid;
        this.firstName = firstName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.province = province;
        this.barangay = barangay;
        this.street = street;
        this.fullName = fullName;
        this.email = email;
        this.phone_number = phone_number;
    }

    public int getStudentid(){
        return studentid;
    }
    public String getStudentNumber() {
        return String.valueOf(studentid);
    }
    public String getPhone_number(){
        return phone_number;
    }
    public String getEmail(){
        return email;
    }
    public String getfirstName() {
        return firstName;
    }

    public String getAddress() {
        return address;
    }
    public String getZip(){
        return zip;
    }
    public String getFullName(){
        return fullName;
    }
    public String getProvince(){
        return province;
    }
    public String getCity(){
        return city;
    }
    public String getBarangay(){
        return barangay;
    }
    public String getStreet(){
        return street;
    }
}

