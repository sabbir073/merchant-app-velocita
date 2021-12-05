package com.xubisoft.parcelwala.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.xubisoft.parcelwala.R;
import com.xubisoft.parcelwala.databinding.ActivityMainBinding;
import com.xubisoft.parcelwala.databinding.ActivityRegistrationPageBinding;
import com.xubisoft.parcelwala.network.Api;
import com.xubisoft.parcelwala.network.RetrofitClient;
import com.xubisoft.parcelwala.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Registration_Page extends AppCompatActivity {
    ActivityRegistrationPageBinding binding;
    Api api;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressDialog = new ProgressDialog(Registration_Page.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);


        api = RetrofitClient.difBaseUrle().create(Api.class);


//===========Buttons===========================
        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataValidation();


//                Datainitailization();

            }
        });


        binding.backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void Datainitailization() {

        progressDialog.show();
        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
        api.registration(binding.companyName.getText().toString(), binding.merchantName.getText().toString(),
                binding.mailAddress.getText().toString(), binding.enterPassword.getText().toString(),
                binding.confirpassword.getText().toString(), binding.phnNumber.getText().toString(), binding.address.getText().toString(),
                binding.fburl.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    progressDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(), OtpRegistrationConfirm.class);
                    intent.putExtra(Constant.PHONE, binding.phnNumber.getText().toString());
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Otp Send Successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    try {
                        // Log.e("tesstss", response.errorBody().string());
                        try {
                            JSONObject json = new JSONObject(response.errorBody().string().toString());

                            new StyleableToast

                                    .Builder(getApplicationContext())
                                    .text(json.getString("error"))
                                    .textColor(Color.WHITE)
                                    .cornerRadius(90)
                                    .iconStart(R.drawable.ic_arrow_down)
                                    .backgroundColor(Color.RED)
                                    .show();




                            /*JSONObject json = new JSONObject(response.errorBody().string().toString());
                            Toast.makeText(Registration_Page.this, json.getString("error"), Toast.LENGTH_SHORT).show();
                       */
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // String a=response.errorBody().string().toString();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    //================dataValidation======================
    private void dataValidation() {
        // Datainitailization();
        if (!TextUtils.isEmpty(binding.companyName.getText().toString())) {
            if (!TextUtils.isEmpty(binding.merchantName.getText().toString())) {
                if (!TextUtils.isEmpty(binding.phnNumber.getText().toString())) {
                    if (!TextUtils.isEmpty(binding.mailAddress.getText().toString())) {
                        if (!TextUtils.isEmpty(binding.enterPassword.getText().toString())) {
                            if (!TextUtils.isEmpty(binding.confirpassword.getText().toString())) {
                                Log.e("yes", "fgfggdg");
                                Datainitailization();
                            } else {
                                binding.enterPassword.setError("Please Enter Confirm Password");
                                binding.enterPassword.requestFocus();
                            }
                        } else {
                            binding.enterPassword.setError("Please Enter Password");
                            binding.enterPassword.requestFocus();
                        }

                    } else {
                        binding.mailAddress.setError("Please Enter Email");
                        binding.mailAddress.requestFocus();
                    }

                } else {
                    binding.phnNumber.setError("Please Enter Phone Number");
                    binding.phnNumber.requestFocus();
                }

            } else {
                binding.merchantName.setError("Please Enter  Name");
                binding.merchantName.requestFocus();
            }

        } else {
            binding.companyName.setError("Please Enter Company Name");
            binding.companyName.requestFocus();
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}