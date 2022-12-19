package com.asas.cloud.Model;

import java.util.Date;

public class AudioModel {
    String audioPath,  Video_Name, Id;
    Date Data;

    public AudioModel(String audioPath, String video_Name, String id, Date data) {
        this.audioPath = audioPath;
        Video_Name = video_Name;
        Id = id;
        Data = data;
    }

    public AudioModel() {}

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getVideo_Name() {
        return Video_Name;
    }

    public void setVideo_Name(String video_Name) {
        Video_Name = video_Name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }
}
