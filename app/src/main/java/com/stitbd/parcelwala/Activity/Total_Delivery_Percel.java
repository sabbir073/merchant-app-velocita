package com.stitbd.parcelwala.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.stitbd.parcelwala.Adaptar.CanclePercel.CancleParcelAdapter;
import com.stitbd.parcelwala.R;
import com.stitbd.parcelwala.model.DashBoardClick.DashboardClickContainer;
import com.stitbd.parcelwala.network.Api;
import com.stitbd.parcelwala.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Total_Delivery_Percel extends AppCompatActivity {

    RecyclerView recyclerView;
    Api api;
    ProgressDialog progressDialog;
    ConstraintLayout nodatafound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_delivery_percel);

        recyclerView = findViewById(R.id.total_delivery_percel);
        nodatafound = findViewById(R.id.nodatafound);

        progressDialog = new ProgressDialog(Total_Delivery_Percel.this);
        progressDialog.setMessage("please Wait");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Total delivery Parcel");

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);

        api.getfilter("delivery").enqueue(new Callback<DashboardClickContainer>() {
            @Override
            public void onResponse(Call<DashboardClickContainer> call, Response<DashboardClickContainer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false));

                    CancleParcelAdapter adapter = new CancleParcelAdapter(response.body().getDashclick()
                            , getApplicationContext());
                    recyclerView.setAdapter(adapter);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    nodatafound.setVisibility(View.INVISIBLE);
                    // Log.e("eorrrr", response.toString());
                } else {
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    nodatafound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<DashboardClickContainer> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Total_Delivery_Percel.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
//        moveTaskToBack(true);
        return true;
    }
}