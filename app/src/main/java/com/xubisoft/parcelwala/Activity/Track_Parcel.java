package com.xubisoft.parcelwala.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.xubisoft.parcelwala.Adaptar.PercelLogAdapter;
import com.xubisoft.parcelwala.R;
import com.xubisoft.parcelwala.databinding.ActivityMainBinding;
import com.xubisoft.parcelwala.databinding.ActivityTrackParcelBinding;
import com.xubisoft.parcelwala.model.PercelLog.PercelContainerLog;
import com.xubisoft.parcelwala.model.PercelLog.PercelInformation;
import com.xubisoft.parcelwala.model.PercelLog.PercelLog;
import com.xubisoft.parcelwala.network.Api;
import com.xubisoft.parcelwala.network.RetrofitClient;
import com.xubisoft.parcelwala.util.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Track_Parcel extends AppCompatActivity {
         ActivityTrackParcelBinding binding;
         Api api;
         RecyclerView recyclerView;
         ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Parcel Track");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding = ActivityTrackParcelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(Track_Parcel.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);
        api= RetrofitClient.get(getApplicationContext()).create(Api.class);
          recyclerView=findViewById(R.id.rv_list);
        String invoiceId=getIntent().getStringExtra(Constant.INVOICE_NO);
        if(!invoiceId.isEmpty()){
            getdatafromServer(invoiceId);
            binding.etInvoiceNo.setText(invoiceId);
            binding.etInvoiceNo.setEnabled(false);
            binding.etMerchantOrderNo.setEnabled(false);
        }


    }

    private void getdatafromServer(String invoiceId) {
        progressDialog.show();
        api.getpercellog(invoiceId,null).enqueue(new Callback<PercelContainerLog>() {
            @Override
            public void onResponse(Call<PercelContainerLog> call, Response<PercelContainerLog> response) {
                progressDialog.dismiss();
                if(response.isSuccessful()&&response.body()!=null){
                    PercelInformation percelInformation=response.body().getPercellogInformation();
                   List<PercelLog> percelLog=response.body().getPercelLog();
                    binding.tvCustomerName.setText("Name: "+response.body().getPercellogInformation().getCustomerName());
                    binding.tvCustomerAddress.setText("Address: "+response.body().getPercellogInformation().getCustomerAddress());
                    binding.tvCustomerPhone.setText("Phone: "+response.body().getPercellogInformation().getCustomerContactNumber());
                    binding.tvDate.setText("Date: "+response.body().getPercellogInformation().getDate());

                    binding.tvPackage.setText("Package: "+percelInformation.getWeight_package().getName());
                    binding.tvInvoice.setText("Inv: "+percelInformation.getParcelInvoice());
                    binding.tvOrderNo.setText("Order Id:"+percelInformation.getOrder_id());
                    binding.tvTotalCharge.setText("Total: "+String.valueOf(percelInformation.getTotalCharge()));
                    binding.tvDeliveryCharge.setText("Delivery: "+String.valueOf(percelInformation.getDeliveryCharge()));

                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.VERTICAL, false));
                    PercelLogAdapter adapter = new PercelLogAdapter(response.body().getPercelLog());
                    recyclerView.setAdapter(adapter);



                }
//                Log.e("tesst",response.toString());
            }

            @Override
            public void onFailure(Call<PercelContainerLog> call, Throwable t) {
                progressDialog.dismiss();
//                Log.e("tesst",t.toString());
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}