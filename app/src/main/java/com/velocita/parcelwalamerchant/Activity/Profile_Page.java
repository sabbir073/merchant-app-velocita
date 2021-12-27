package com.velocita.parcelwalamerchant.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.velocita.parcelwalamerchant.Adaptar.Profile.ProfileAdaptar;
import com.velocita.parcelwalamerchant.R;
import com.velocita.parcelwalamerchant.model.Profile.ProfileContainer;
import com.velocita.parcelwalamerchant.network.Api;
import com.velocita.parcelwalamerchant.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Page extends AppCompatActivity {

    Api api;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    Button button;
    TextView changepass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        progressDialog = new ProgressDialog(Profile_Page.this);
        progressDialog.setMessage("Please wait.....");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        recyclerView = findViewById(R.id.profile_delivery_charge);
        button=findViewById(R.id.update_profile);
        changepass=findViewById(R.id.changePassword);


        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_Page.this,ChangePasswordActivity.class);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile_Page.this,UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
      dataInitialize();



        api.getprofile().enqueue(new Callback<ProfileContainer>() {
            @Override
            public void onResponse(Call<ProfileContainer> call, Response<ProfileContainer> response) {

            }

            @Override
            public void onFailure(Call<ProfileContainer> call, Throwable t) {

            }
        });


    }

    private void dataInitialize() {
        progressDialog.show();
        api.getprofile().enqueue(new Callback<ProfileContainer>() {
            @Override
            public void onResponse(Call<ProfileContainer> call, Response<ProfileContainer> response) {
                progressDialog.dismiss();

                try {
                    if (response.isSuccessful() && response.body() != null) {

                        TextView textView = findViewById(R.id.company_name);
                        TextView merchant_name = findViewById(R.id.merchant_name);
                        TextView Profile_email = findViewById(R.id.profile_email);
                        TextView Profile_phone = findViewById(R.id.profile_phn);
                        TextView Profile_address = findViewById(R.id.profile_address);
                        TextView Cod_charge=findViewById(R.id.cod_percent);
                        ImageView Profile_pic = findViewById(R.id.profile_pic);
                        TextView merchantid=findViewById(R.id.merchant_id_profile);
//                        TextView addres=findViewById(R.id.Address);
                        TextView fb=findViewById(R.id.fburl);
                        TextView bankname=findViewById(R.id.bankname);
                        TextView bankaccno=findViewById(R.id.bankaccountnumber);
                        TextView bankaccname=findViewById(R.id.bankaccountname);
                        TextView bakash=findViewById(R.id.bkashnumber);
                        TextView ngad=findViewById(R.id.nagadnumber);
                        TextView rocket=findViewById(R.id.rocketnumber);
                        TextView nidnumber=findViewById(R.id.nidnumber);


//                        TextView ProfileHead = findViewById(R.id.profile_email_head);



                        Glide.with(Profile_Page.this).load(response.body().
                                getProfilelist().getImage()).into(Profile_pic);


                        textView.setText(response.body().getProfilelist().getCompany_name());
                        merchant_name.setText(response.body().getProfilelist().getName());
                        Profile_email.setText(response.body().getProfilelist().getEmail());
//                        ProfileHead.setText(response.body().getProfilelist().getEmail());
                        Profile_phone.setText(response.body().getProfilelist().getContact_number());
                        Profile_address.setText(response.body().getProfilelist().getAddress());
                        merchantid.setText(response.body().getProfilelist().getM_id());
                        Cod_charge.setText(response.body().getCod_charge_percent());
//                        addres.setText(response.body().getProfilelist().getAddress());
                        fb.setText(response.body().getProfilelist().getFb_url());
                        bankname.setText(response.body().getProfilelist().getBank_name());
                        bankaccno.setText(response.body().getProfilelist().getBank_account_no());
                        bankaccname.setText(response.body().getProfilelist().getBank_account_name());
                        bakash.setText(response.body().getProfilelist().getBkash_number());
                        ngad.setText(response.body().getProfilelist().getNagad_number());
                        rocket.setText(response.body().getProfilelist().getRocket_name());
                        nidnumber.setText(response.body().getProfilelist().getNid_no());


                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                                LinearLayoutManager.VERTICAL, false));
                        ProfileAdaptar adaptar = new ProfileAdaptar(response.body().getDeliverycahrge());
                        recyclerView.setAdapter(adaptar);

                        Log.e("profileError", response.toString());
                    }
                } catch (Exception e) {
                    Toast.makeText(Profile_Page.this, "Something Wrong...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileContainer> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        dataInitialize();

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}