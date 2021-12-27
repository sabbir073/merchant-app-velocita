package com.velocita.parcelwalamerchant.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.velocita.parcelwalamerchant.R;
import com.velocita.parcelwalamerchant.model.ProfileContainerOfOTP;
import com.velocita.parcelwalamerchant.network.Api;
import com.velocita.parcelwalamerchant.network.RetrofitClient;
import com.velocita.parcelwalamerchant.util.Constant;
import com.velocita.parcelwalamerchant.util.MySharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpRegistrationConfirm extends AppCompatActivity {

    Button submit_otp;
    Api api;
    EditText otp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_registration_confirm);

        submit_otp = findViewById(R.id.submit_otp_page);
        otp = findViewById(R.id.otp_number_input);
        progressDialog = new ProgressDialog(OtpRegistrationConfirm.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);

        api = RetrofitClient.noInterceptor().create(Api.class);
        String phn = getIntent().getStringExtra(Constant.PHONE);

        submit_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                if (!TextUtils.isEmpty(otp.getText().toString())) {

                    api.otpCheck(phn, otp.getText().toString()).enqueue(new Callback<ProfileContainerOfOTP>() {
                        @Override
                        public void onResponse(Call<ProfileContainerOfOTP> call, Response<ProfileContainerOfOTP> response) {
                            progressDialog.dismiss();

                            if (response.isSuccessful() && response.body() != null) {
                                MySharedPreference.getInstance(OtpRegistrationConfirm.this).edit()
                                        .putString(Constant.TOKEN, response.body().getToken()).putString(Constant.NAME, response.body().getMerchant().getName()).putString(Constant.PHONE, response.body().getMerchant().getContact_number())
                                        .putString(Constant.MERCENTID, response.body().getMerchant().getM_id()).putString(Constant.BUSINESSADDRESS, response.body().getMerchant().getBusiness_address() != null ? response.body().getMerchant().getBusiness_address().toString() : " ").
                                        putString(Constant.ADDRESS, response.body().getMerchant().getAddress()).apply();
                                Toast.makeText(OtpRegistrationConfirm.this, "otp match", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(OtpRegistrationConfirm.this, Dashboard.class);
                                startActivity(intent);

                            } else {
                                try {
                                    // Log.e("tesstss", response.errorBody().string());
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject json = new JSONObject(response.errorBody().string().toString());
                                        Toast.makeText(OtpRegistrationConfirm.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // String a=response.errorBody().string().toString();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ProfileContainerOfOTP> call, Throwable t) {
                            progressDialog.dismiss();
                            Log.d("eroorbody", t.toString());
                            // Toast.makeText(OtpRegistrationConfirm.this,"Something wrong.....",Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    otp.setError("please enter otp code");
                    otp.requestFocus();
                }


            }
        });
    }
}