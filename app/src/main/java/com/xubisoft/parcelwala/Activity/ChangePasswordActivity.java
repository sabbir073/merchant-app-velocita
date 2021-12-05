package com.xubisoft.parcelwala.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.xubisoft.parcelwala.R;
import com.xubisoft.parcelwala.databinding.ActivityChangePasswordBinding;
import com.xubisoft.parcelwala.model.UpdateProfile.UpdateProContainer;
import com.xubisoft.parcelwala.network.Api;
import com.xubisoft.parcelwala.network.RetrofitClient;
import com.xubisoft.parcelwala.util.MySharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {


    Api api;
    ProgressDialog progressDialog;
    EditText Cpass, Npass, COpass;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Password");
        progressDialog = new ProgressDialog(ChangePasswordActivity.this);
        progressDialog.setMessage("Please wait.....");

        Cpass = findViewById(R.id.currentPassword);
        Npass = findViewById(R.id.newPassword);
        COpass = findViewById(R.id.confirmPassword);
        save = findViewById(R.id.UpdatePassword);

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                startActivity(intent);
                datainitialize();
            }
        });

    }

    public void datainitialize() {
        progressDialog.show();
        if (!TextUtils.isEmpty(Cpass.getText().toString())) {
            if (!TextUtils.isEmpty(Npass.getText().toString())) {
                if (!TextUtils.isEmpty(COpass.getText().toString())) {
                    api.getupdatepassword(Cpass.getText().toString(), Npass.getText().toString()).enqueue(new Callback<UpdateProContainer>() {
                        @Override
                        public void onResponse(Call<UpdateProContainer> call, Response<UpdateProContainer> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                progressDialog.dismiss();
                                MySharedPreference.editor(getApplicationContext()).clear().commit();
                                Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(ChangePasswordActivity.this, "Update Successfully Password", Toast.LENGTH_SHORT).show();
                            } else {
                                try {
                                    // Log.e("tesstss", response.errorBody().string());
                                    try {
                                        JSONObject json = new JSONObject(response.errorBody().string().toString());
                                        Toast.makeText(ChangePasswordActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
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
                        public void onFailure(Call<UpdateProContainer> call, Throwable t) {

                        }
                    });

                } else {
                    COpass.setError("Please Enter Confirm Password");
                    COpass.requestFocus();
                }
            } else {
                Npass.setError("Please Enter Your New Password");
                Npass.requestFocus();

            }
        } else {
            Cpass.setError("Enter Your Current Password");
            Cpass.requestFocus();


        }

    }

}