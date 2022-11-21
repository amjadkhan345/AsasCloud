package com.asas.cloud.classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.asas.cloud.Model.ApiControler;
import com.asas.cloud.Model.Response;

import retrofit2.Call;
import retrofit2.Callback;

public class CallApi {

    static String res;


    public static String sendmail(String code, Context context, String useremail){
        //String res = //Uttilties.createRandomCode(6);

        String senderEmail = "asaskhan039@gmail.com";
        //String message =  code ;
        String subject = "Verification Code";
        //String ReseverEmail = "ak4806030@gmail.com";
        ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setTitle("Sending Email");

        progressBar.setCancelable(false);

        progressBar.show();


        Call<Response> call= ApiControler.getInstance()
                .getapi()
                .sendmail(useremail, subject, code, senderEmail);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response response1 = response.body();
                if (response1.getMessage().equals("sent")){
                    //Intent intent=new Intent(LoginWithPasswordActivity.this, Otp_loginActivity.class);
                    //intent.putExtra("otp", code);
                    //intent.putExtra("type","1");
                    //startActivity(intent);
                    //finish();
                    res= "sent";//response1.getMessage();
                    progressBar.dismiss();

                }else {
                    progressBar.dismiss();
                    res="filed";//response1.getMessage();

                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                    //progressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                progressBar.dismiss();
                Toast.makeText(context, "error" + code, Toast.LENGTH_SHORT).show();
                res="error";

            }
        });
        return res;
    }
}
