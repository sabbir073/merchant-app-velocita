package com.velocita.parcelwalamerchant.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.velocita.parcelwalamerchant.R;
import com.velocita.parcelwalamerchant.model.DashBoardHelperModel;
import com.velocita.parcelwalamerchant.model.Profile.ProfileContainer;
import com.velocita.parcelwalamerchant.network.Api;
import com.velocita.parcelwalamerchant.network.RetrofitClient;
import com.velocita.parcelwalamerchant.model.DashBordDataContainer;
import com.velocita.parcelwalamerchant.util.Constant;
import com.velocita.parcelwalamerchant.util.MyAlertDialog;
import com.velocita.parcelwalamerchant.util.MySharedPreference;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Dashboard extends AppCompatActivity {
    Api api;
    NavigationView nav;
    RecyclerView recyclerView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    TextView username, phone, total_parcel, total_cancle_parcel, waiting_pickup_parcel, total_waitng_delivery_parcel, total_delivery_parcel, total_delivery_complete_parcel, total_return_parcel,
            total_return_complete_parcel, total_pending_collect_amount, total_collect_amounts;
    ProgressDialog progressDialog;
    Button profile_view;
    ImageView image;
    SwipeRefreshLayout swip;

    LinearLayout total_parcels, total_cancle_parcels, waiting_pickup_parcels, total_waitng_delivery_parcels,
            total_delivery_parcels, total_delivery_complete_parcels, total_return_parcels,
            total_return_complete_parcels, total_pending_collect_amounts, total_collect_amountss;
    //////click

    List<DashBoardHelperModel> dashBoardData = new ArrayList<>();
    ///click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dashboard);

        progressDialog = new ProgressDialog(Dashboard.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.dash_board_list);
        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
        username = findViewById(R.id.username_tv);
        phone = findViewById(R.id.userPhone_tv);
        nav = (NavigationView) findViewById(R.id.navbar);
        drawerLayout = findViewById(R.id.drawerlayout);
        profile_view = findViewById(R.id.view_profile);
        swip=findViewById(R.id.swip);


       swip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
           @Override
           public void onRefresh() {
               dashboarddata();
               swip.setRefreshing(false);
           }
       });

        image = findViewById(R.id.image_dashboard);

        total_collect_amounts = findViewById(R.id.total_collect_amounts);
        total_pending_collect_amount = findViewById(R.id.total_pending_collect_amount);
        total_return_complete_parcel = findViewById(R.id.total_return_complete_parcel);
        total_return_parcel = findViewById(R.id.total_return_parcel);
        total_delivery_complete_parcel = findViewById(R.id.total_delivery_complete_parcel);
        total_delivery_parcel = findViewById(R.id.total_delivery_parcel);
        total_waitng_delivery_parcel = findViewById(R.id.total_waitng_delivery_parcel);
        waiting_pickup_parcel = findViewById(R.id.waiting_pickup_parcel);
        total_cancle_parcel = findViewById(R.id.total_cancle_parcel);
        total_parcel = findViewById(R.id.total_parcel);


        total_collect_amountss = findViewById(R.id.total_collect_amountss);
        total_pending_collect_amounts = findViewById(R.id.total_pending_collect_amounts);
        total_return_complete_parcels = findViewById(R.id.total_return_complete_parcels);
        total_return_parcels = findViewById(R.id.total_return_parcels);
        total_delivery_complete_parcels = findViewById(R.id.total_delivery_complete_parcels);
        total_delivery_parcels = findViewById(R.id.total_delivery_parcels);
        total_waitng_delivery_parcels = findViewById(R.id.total_waitng_delivery_parcels);

        waiting_pickup_parcels = findViewById(R.id.waiting_pickup_parcels);
        total_cancle_parcels = findViewById(R.id.total_cancle_parcels);
        total_parcels = findViewById(R.id.total_parcels);
        //dashboarddata();
        total_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Dashboard_Click_Total_Parcel.class));
            }
        });

        total_cancle_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Total_Cancle_Percel.class));
            }
        });

        waiting_pickup_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Total_WaitingPercel.class));
            }
        });

        total_waitng_delivery_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Waiting_Delivery_Percel.class));
            }
        });

        total_delivery_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Total_Delivery_Percel.class));
            }
        });

        total_delivery_complete_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Total_Delivery_Complete_Percel.class));
            }
        });

        total_return_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Total_Return_Percel.class));
            }
        });

        total_return_complete_parcels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Total_Return_Complete_percel.class));
            }
        });


        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setTitle("Dashboard");
        //getSupportActionBar().setTitle("Dashboard");


        profile_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Profile_Page.class);
                startActivity(intent);
            }
        });


        api.getprofile().enqueue(new Callback<ProfileContainer>() {
            @Override
            public void onResponse(Call<ProfileContainer> call, Response<ProfileContainer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ImageView image = findViewById(R.id.image_dashboard);
                    Glide.with(Dashboard.this).load(response.body().
                            getProfilelist().getImage()).into(image);
                }

            }

            @Override
            public void onFailure(Call<ProfileContainer> call, Throwable t) {

            }
        });


//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        username.setText(MySharedPreference.getInstance(getApplicationContext()).getString(Constant.NAME, "not found"));
        phone.setText(MySharedPreference.getInstance(getApplicationContext()).getString(Constant.PHONE, "not found"));

        progressDialog.show();


        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_percel:
                        Intent intent = new Intent(getApplicationContext(), Add_Parcel.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.percel_list:
                        Intent intentone = new Intent(getApplicationContext(), Parcel_list_show.class);
                        startActivity(intentone);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.order_tracking:
                        Intent intenttwo = new Intent(getApplicationContext(), Order_Tracking.class);
                        startActivity(intenttwo);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.coverage_area:
                        Intent intentthre = new Intent(getApplicationContext(), Coverage_Area.class);
                        startActivity(intentthre);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
//                    case R.id.service_charge:
//                        Intent intentfour = new Intent(getApplicationContext(), Service_Charge.class);
//                        startActivity(intentfour);
//                        drawerLayout.closeDrawer(GravityCompat.START);
//                        break;
                    case R.id.payment_list:
                        Intent intentfive = new Intent(getApplicationContext(), Payment_List.class);
                        startActivity(intentfive);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.delivery_list:
                        Intent intentsix = new Intent(getApplicationContext(), Delivery_Parcel_list.class);
                        startActivity(intentsix);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.pickup_parcel:
                        Intent pintent = new Intent(getApplicationContext(), PickupParcelListActivity.class);
                        startActivity(pintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.return_parcel:
                        Intent rintent = new Intent(getApplicationContext(), ReturnParcelActivity.class);
                        startActivity(rintent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.log_out:
                        MySharedPreference.editor(getApplicationContext()).clear().commit();
                        startActivity(new Intent(Dashboard.this, MainActivity.class));
                        finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        //logout();

                        break;
                }

                return true;
            }
        });
    }

    private void logout() {
        MyAlertDialog alertDialog = new MyAlertDialog(getApplicationContext());
        alertDialog.showConfirmDialog("Are You Want to Logout?", "Confirm", "Cancel");
        alertDialog.okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  alertDialog.cancel();
                //
                startActivity(new Intent(Dashboard.this, MainActivity.class));
                finish();

            }
        });

        alertDialog.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
      //  setDashBoardData();
        dashboarddata();


    }

    public void dashboarddata() {
        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
        progressDialog.show();
        api.getDashboarddata().enqueue(new Callback<DashBordDataContainer>() {
            @Override
            public void onResponse(Call<DashBordDataContainer> call, Response<DashBordDataContainer> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    total_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalParcel()));
                    total_cancle_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalCancelParcel()));
                    waiting_pickup_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalWaitingPickupParcel()));
                    total_waitng_delivery_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalWaitingDeliveryParcel()));
                    total_delivery_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalDeliveryParcel()));
                    total_delivery_complete_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalDeliveryCompleteParcel()));
                    total_return_parcel.setText(String.valueOf(response.body().getDashBoardData().getTotalReturnParcel()));
                    total_return_complete_parcel.setText(String.format("%1$,.0f",response.body().getDashBoardData().getTotalReturnCompleteParcel()));
                    total_pending_collect_amount.setText(String.format("%1$,.0f",response.body().getDashBoardData().getTotalPendingCollectAmount()));
                    total_collect_amounts.setText(String.format("%1$,.0f",response.body().getDashBoardData().getTotalCollectAmount()));

                    // Log.e("testing",response.body().getMessage());
                }else if (response.code() == 400){
                    MySharedPreference.remove(getApplicationContext());
                    startActivity(new Intent(Dashboard.this, MainActivity.class));
                    finish();

                }
            }

            @Override
            public void onFailure(Call<DashBordDataContainer> call, Throwable t) {
                progressDialog.dismiss();
                MySharedPreference.remove(getApplicationContext());
                startActivity(new Intent(Dashboard.this, MainActivity.class));
            }
        });
    }

//    public void setDashBoardData() {
//        api.getDashboarddata().enqueue(new Callback<DashBordDataContainer>() {
//            @Override
//            public void onResponse(Call<DashBordDataContainer> call, Response<DashBordDataContainer> response) {
//                // Log.e("datt",String.valueOf(response.body().getTotalParcel()));
//                progressDialog.dismiss();
//                if (response.isSuccessful() && response.body() != null) {
//                    Log.d("tesstsss", response.toString());
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(),
//                            2));
//                    Adapter adapter = new Adapter(response.body().getDashBoardData().getlist(), getApplicationContext());
//                    recyclerView.setAdapter(adapter);
//                } else if (response.code() == 400) {
//                    Log.d("tesst", response.toString());
//                    // Toast.makeText(Dashboard.this, "Token is Expired", Toast.LENGTH_SHORT).show();
//                    MySharedPreference.remove(getApplicationContext());
//                    startActivity(new Intent(Dashboard.this, MainActivity.class));
//                } else {
//                    try {
//                        Log.e("dash", response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<DashBordDataContainer> call, Throwable t) {
//                progressDialog.dismiss();
//                MySharedPreference.remove(getApplicationContext());
//                startActivity(new Intent(Dashboard.this, MainActivity.class));
//                Toast.makeText(Dashboard.this, "Something wrong", Toast.LENGTH_SHORT).show();
//                Log.d("tesst", t.toString());
//            }
//        });
//
//    }
//    @Override
//    public void click(int position) {
//        dashBoardData.get(position);
//        Intent intent = new Intent(this,Forgot_Password.class);
//        startActivity(intent);
//    }


}