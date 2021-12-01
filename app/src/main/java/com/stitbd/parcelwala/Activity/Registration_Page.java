package com.stitbd.parcelwala.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.stitbd.parcelwala.Adaptar.AreaSpinnerAdapter;
import com.stitbd.parcelwala.Adaptar.DistrictSpinnerAdaptar;
import com.stitbd.parcelwala.Adaptar.UpozilaSpinnerAdaptar;
import com.stitbd.parcelwala.databinding.ActivityMainBinding;
import com.stitbd.parcelwala.databinding.ActivityRegistrationPageBinding;
import com.stitbd.parcelwala.model.Area;
import com.stitbd.parcelwala.model.AreaContainer;
import com.stitbd.parcelwala.model.District;
import com.stitbd.parcelwala.model.DistrictsContainer;
import com.stitbd.parcelwala.model.RegisterResponse;
import com.stitbd.parcelwala.model.Upozila;
import com.stitbd.parcelwala.model.UpozilasContainer;
import com.stitbd.parcelwala.network.Api;
import com.stitbd.parcelwala.network.RetrofitClient;
import com.stitbd.parcelwala.util.Constant;
import com.stitbd.parcelwala.util.MySharedPreference;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
        api.registration(binding.companyName.getText().toString(), binding.merchantName.getText().toString(),
                binding.mailAddress.getText().toString(), binding.enterPassword.getText().toString(),
                binding.confirpassword.getText().toString(), binding.phnNumber.getText().toString(),binding.address.getText().toString(),
                binding.fburl.getText().toString()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("yes", response.toString());
                    Intent intent = new Intent(getApplicationContext(), OTP_Page.class);
                    startActivity(intent);
                    finish();

                } else {
                    try {
                        Log.e("unstop", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(Registration_Page.this, "Registration not Successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }


    //================dataValidation======================
    private void dataValidation() {
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