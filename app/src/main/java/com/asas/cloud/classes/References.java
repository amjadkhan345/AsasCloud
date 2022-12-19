package com.asas.cloud.classes;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class References {
    //FirebaseDatabase database;

    //DatabaseReference reference;
    public static final FirebaseAuth AUTH=FirebaseAuth.getInstance();
    public static final FirebaseUser USER=AUTH.getCurrentUser();
    public static final  FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference reference = database.getReference();
    public static final String UserId=USER.getUid();
    public static final FirebaseStorage storage= FirebaseStorage.getInstance();
    public static final StorageReference STORAGE_REFERENCE=storage.getReference();
    public static final StorageReference PROFILE_REFERENCE=STORAGE_REFERENCE.child("profile");


    public static  final String Image = "Image";
    public static final DatabaseReference User_Reference = reference.child("User");


    public static final DatabaseReference Video_Reference = reference.child("Video");
    public static final DatabaseReference AUDIO_Reference = reference.child("Audio");
    public static final DatabaseReference CONTACT_Reference = reference.child("Contacts");
    public static final DatabaseReference Image_Reference = reference.child("Image");
    public static final DatabaseReference File_Reference = reference.child("File");
    public static final DatabaseReference ADS_Reference = reference.child("ADS");
}
