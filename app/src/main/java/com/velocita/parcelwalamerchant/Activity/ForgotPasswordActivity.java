package com.velocita.parcelwalamerchant.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.velocita.parcelwalamerchant.R;
import com.velocita.parcelwalamerchant.model.ForgotPassword.ForgetPasswordContainer;
import com.velocita.parcelwalamerchant.network.Api;
import com.velocita.parcelwalamerchant.network.RetrofitClient;
import com.velocita.parcelwalamerchant.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

//    private ActivityMainBinding binding;

    EditText Phn;
    Button sendOtp;
    Api api;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendOtp = findViewById(R.id.send_otp);
        Phn = findViewById(R.id.phn_no);

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);

        progressDialog = new ProgressDialog(ForgotPasswordActivity.this);
        progressDialog.setMessage("Please wait.....");
        progressDialog.setCancelable(false);

        sendOtp.setOnClickListener(v -> {
            progressDialog.show();
            if (!TextUtils.isEmpty(Phn.getText().toString()) && Phn.getText().length() == 11) {
                Log.d("isMethodCalling", "method calling");
                api = RetrofitClient.get(getApplicationContext()).create(Api.class);
                Call<ForgetPasswordContainer> call = api.forgetPassword(Phn.getText().toString());

                call.enqueue(new Callback<ForgetPasswordContainer>() {
                    @Override
                    public void onResponse(Call<ForgetPasswordContainer> call, Response<ForgetPasswordContainer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            progressDialog.dismiss();
                            Log.d("forgotresponse", response.body().getSuccess() + "");
                            Intent intent = new Intent(ForgotPasswordActivity.this, OTP_Page.class);
                            intent.putExtra(Constant.PHONE, Phn.getText().toString());
                            startActivity(intent);
                            finish();
                        }
                        else {
                            try {
                                // Log.e("tesstss", response.errorBody().string());
                                try {
                                    JSONObject json = new JSONObject(response.errorBody().string().toString());
                                    Toast.makeText(ForgotPasswordActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<ForgetPasswordContainer> call, Throwable t) {
                        Log.d("errorResponse", "response is: " + t.getMessage());
                       // Toast.makeText(ForgotPasswordActivity.this, "something wrong......", Toast.LENGTH_SHORT).show();
                    }

                });
            } else {
                Phn.setError("please enter your valid phone no");
                Phn.requestFocus();
            }
        });

    }
}