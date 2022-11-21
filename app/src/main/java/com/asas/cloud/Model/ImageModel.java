package com.asas.cloud.Model;

import android.provider.ContactsContract;

import java.util.Date;

public class ImageModel {
    String   Video_Name, Id, Image;
    Date Data;
    //byte[] Image;


    public ImageModel() {
    }

    public ImageModel(String id) {
        Id = id;
    }

    public ImageModel(String video_Name, Date data, String image) {
        Video_Name = video_Name;
        Data = data;
        Image = image;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getVideo_Name() {
        return Video_Name;
    }

    public void setVideo_Name(String video_Name) {
        Video_Name = video_Name;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
