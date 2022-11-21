package com.asas.cloud.Model;

import android.provider.ContactsContract;

import java.util.Date;

public class FileModel {

    String File_Path, File_Name, id;
    //ContactsContract.Data data;
    Date data;

    public FileModel() {
    }

    public FileModel(String id) {
        this.id = id;
    }

    public FileModel(String file_Path, String file_Name, Date data) {
        File_Path = file_Path;
        File_Name = file_Name;
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFile_Path() {
        return File_Path;
    }

    public void setFile_Path(String file_Path) {
        File_Path = file_Path;
    }

    public String getFile_Name() {
        return File_Name;
    }

    public void setFile_Name(String file_Name) {
        File_Name = file_Name;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
