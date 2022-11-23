package com.asas.cloud.Model;

public class UserModel {
    String Name, Email, Age, Country, PhoneNumber,Profileurl;

    long size, user_storage;

    public UserModel(long size, long user_storage) {
        this.size = size;
        this.user_storage = user_storage;
    }

    public UserModel() {
    }

    public UserModel(String name, String email, String age, String country, String phoneNumber, String profileurl) {
        Name = name;
        Email = email;
        Age = age;
        Country = country;
        PhoneNumber = phoneNumber;
        Profileurl = profileurl;
    }


    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getUser_storage() {
        return user_storage;
    }

    public void setUser_storage(long user_storage) {
        this.user_storage = user_storage;
    }

    public String getProfileurl() {
        return Profileurl;
    }

    public void setProfileurl(String profileurl) {
        Profileurl = profileurl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}

