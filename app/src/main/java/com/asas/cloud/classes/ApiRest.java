package com.asas.cloud.classes;

import com.asas.cloud.Model.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRest {


    @FormUrlEncoded
    @POST("mail.php")
    Call<Response> sendmail(
        @Field("to") String To,
        @Field("subject") String subject,
        @Field("message") String message,
        @Field("from") String from
    );
}
