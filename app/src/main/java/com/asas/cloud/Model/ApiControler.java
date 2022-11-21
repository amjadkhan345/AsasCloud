package com.asas.cloud.Model;

import com.asas.cloud.classes.ApiRest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiControler {


    private static final String URL = "https://asasbook.com/sendmail/";

    private static ApiControler apiControler;

    private static Retrofit retrofit;

    ApiControler(){
        retrofit=new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized ApiControler getInstance(){
        if (apiControler==null)
            apiControler= new ApiControler();
        return apiControler;
    }

    public ApiRest getapi(){
        return retrofit.create(ApiRest.class);
    }
}
