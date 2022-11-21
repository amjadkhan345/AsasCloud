package com.asas.cloud.Model;

import android.provider.ContactsContract;

import java.util.Date;

public class VideoModel {
    String Video,  Video_Name, Id;
    Date Data;
    String Thambnel;

    public VideoModel() {
    }

    public VideoModel(String id) {
        Id = id;
    }

    public VideoModel(String video, String video_Name, Date data, String thambnel) {
        Video = video;
        Video_Name = video_Name;
        Data = data;
        Thambnel = thambnel;

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
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

    public String getThambnel() {
        return Thambnel;
    }

    public void setThambnel(String thambnel) {
        Thambnel = thambnel;
    }
}
