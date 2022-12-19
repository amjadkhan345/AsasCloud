package com.asas.cloud.Model;

import java.util.Date;

public class ContactModel {

    String contact_Number,  contact_Name, contact_Id;
    Date Data;

    public ContactModel(String contact_Number, String contact_Name, String contact_Id, Date data) {
        this.contact_Number = contact_Number;
        this.contact_Name = contact_Name;
        this.contact_Id = contact_Id;
        Data = data;
    }

    public ContactModel() {
    }


    public String getContact_Number() {
        return contact_Number;
    }

    public void setContact_Number(String contact_Number) {
        this.contact_Number = contact_Number;
    }

    public String getContact_Name() {
        return contact_Name;
    }

    public void setContact_Name(String contact_Name) {
        this.contact_Name = contact_Name;
    }

    public String getContact_Id() {
        return contact_Id;
    }

    public void setContact_Id(String contact_Id) {
        this.contact_Id = contact_Id;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }
}
