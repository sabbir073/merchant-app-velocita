package com.stitbd.parcelwala.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.stitbd.parcelwala.Adaptar.AddpercelWeightpackage.Weightpackagespinner;
import com.stitbd.parcelwala.Adaptar.AreaSpinnerAdapter;
import com.stitbd.parcelwala.Adaptar.DelivaryOptionSpinner;
import com.stitbd.parcelwala.Adaptar.DistrictSpinnerAdaptar;
import com.stitbd.parcelwala.Adaptar.UpozilaSpinnerAdaptar;
import com.stitbd.parcelwala.R;
import com.stitbd.parcelwala.databinding.ActivityAddParcelBinding;
import com.stitbd.parcelwala.model.Area;
import com.stitbd.parcelwala.model.AreaContainer;
import com.stitbd.parcelwala.model.DeliveryOption;
import com.stitbd.parcelwala.model.District;
import com.stitbd.parcelwala.model.DistrictsContainer;
import com.stitbd.parcelwala.model.GetChargeDelivery.ChargeDeliveryAddPercel;
import com.stitbd.parcelwala.model.GetChargeDelivery.WeightPackageRate;
import com.stitbd.parcelwala.model.Upozila;
import com.stitbd.parcelwala.model.UpozilasContainer;
import com.stitbd.parcelwala.network.Api;
import com.stitbd.parcelwala.network.RetrofitClient;
import com.stitbd.parcelwala.util.Constant;
import com.stitbd.parcelwala.util.MySharedPreference;
import com.google.gson.JsonObject;
import com.skydoves.expandablelayout.ExpandableAnimation;
import com.skydoves.expandablelayout.ExpandableLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Add_Parcel extends AppCompatActivity {
    ActivityAddParcelBinding binding;
    Boolean isExpended = false;
    EditText Customer_Name, Phone_Number, Email_Address, Merchant_order_no,
            Collection_Amount, Enter_Product_Description, exCodPercent, exCodCharge,
            exWeightPackage, exWeightCharge, exDeliveryCharge, exTotalCharge;
    Spinner District, Upazila, Area, Package, Payment_Method, PickupAddress;
    Button Submit;
    Toolbar toolbar;
    Api api;


    Double weight_price;
    Double last_weight_price;
    String weight_input;

    Double perkg;


    ProgressDialog progressDialog;
    List<District> districts;
    List<Upozila> upozilaList;
    List<Area> areas;
    List<WeightPackageRate> weightPackageList;
    Double cod_percent = 0.0;
    Double codCharge = 0.0;
    Double weightCharge = 0.0;
    Double deliveryCharge = 0.0;
    Double totalCharge = 0.0;


    List<DeliveryOption> deliveryOptions = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddParcelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().setTitle("Add Parcel");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(Add_Parcel.this);
        progressDialog.setMessage("Please Wait......");
        progressDialog.setCancelable(false);

        api = RetrofitClient.get(getApplicationContext()).create(Api.class);

        Customer_Name = findViewById(R.id.customer_name);
        Phone_Number = findViewById(R.id.phone_num);
        Merchant_order_no = findViewById(R.id.merchant_order_no);
        Collection_Amount = findViewById(R.id.collection_amount_percel_page);
        Enter_Product_Description = findViewById(R.id.product_description_percel_page);
        District = findViewById(R.id.district_name_parcel_page);
        Upazila = findViewById(R.id.upazila_name_parcel_page);
        Area = findViewById(R.id.area_name_parcel_page);
        Package = findViewById(R.id.package_parcel_page);
        Payment_Method = findViewById(R.id.payment_method_parcel_page);
        Submit = findViewById(R.id.submit_add_percel_page);
        PickupAddress = findViewById(R.id.pickup_address);


        binding.weight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                /*if (!TextUtils.isEmpty(s.toString())) {
                    double weightcharge = (Double.valueOf(s.toString()) * weight_price);

                    exWeightCharge.setText(String.valueOf(Math.ceil(weightcharge)));
                    exTotalCharge.setText(String.valueOf(weightcharge
                            + Double.valueOf(exDeliveryCharge.getText().toString()) +
                            Double.valueOf(codCharge)));
                }*/

                if (!TextUtils.isEmpty(s.toString()) && weight_price != null) {
                    double weightcharge = (Double.valueOf(s.toString()) * perkg)+weight_price;
                    Log.d("tesst",String.valueOf(weightcharge));
                    exWeightCharge.setText(String.valueOf(Math.round(weightcharge)));
                    exTotalCharge.setText(String.valueOf(weightcharge
                            + Double.valueOf(exDeliveryCharge.getText().toString()) +
                            Double.valueOf(codCharge)));
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Collection_Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    if (District.getSelectedItemPosition() > 0) {

                        if (Package.getSelectedItemPosition() > 0) {
                            if (!TextUtils.isEmpty(binding.weight.getText().toString())) {
                                WeightPackageRate weightPackage = weightPackageList.get(Package.getSelectedItemPosition());
                                codCharge = Double.valueOf(String.valueOf(s)) * cod_percent / 100;
                                exCodCharge.setText(String.valueOf(codCharge));


                                double weightvalu = Double.valueOf(binding.weight.getText().toString());
                                exTotalCharge.setText(String.valueOf(Double.valueOf((perkg * weightvalu)+weightPackage.getRate())
                                        + Double.valueOf(exDeliveryCharge.getText().toString()) +
                                        Double.valueOf(codCharge)));
                            } else {
                                WeightPackageRate weightPackage = weightPackageList.get(Package.getSelectedItemPosition());
                                codCharge = Double.valueOf(String.valueOf(s)) * cod_percent / 100;
                                exCodCharge.setText(String.valueOf(codCharge));

                                exTotalCharge.setText(String.valueOf(Double.valueOf(weightPackage.getRate())
                                        + Double.valueOf(exDeliveryCharge.getText().toString()) +
                                        Double.valueOf(codCharge)));
                            }

                        } else {

                            codCharge = Double.valueOf(String.valueOf(s)) * cod_percent / 100;
                            exCodCharge.setText(String.valueOf(codCharge));
                            exTotalCharge.setText("");

                        }
                    } else {
                        Toast.makeText(Add_Parcel.this, "Select District First", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    if (Package.getSelectedItemPosition() > 0) {
                        if (!TextUtils.isEmpty(binding.weight.getText().toString())) {
                            exCodCharge.setText("");
                            WeightPackageRate weightPackage = weightPackageList.get(binding.packageParcelPage.
                                    getSelectedItemPosition());
                            exWeightPackage.setText(weightPackage.getName());


                            double weight_input = Double.valueOf(binding.weight.getText().toString());
                            weight_price = weightPackage.getRate();
                            perkg = Double.valueOf(weightPackage.getPerKg());

                           double totalperkg = (weight_input * perkg) + weight_price;


                            //  last_weight_price = (Double.valueOf(weight_input) * Double.valueOf( weight_price));


                            // exWeightCharge.setText(String.valueOf(last_weight_price));
//                        exWeightCharge.setText(String.valueOf(weightPackage.getRate()));


                            exTotalCharge.setText(String.valueOf(Double.valueOf(totalperkg)
                                    + Double.valueOf(exDeliveryCharge.getText().toString())));
                        } else {
                            exCodCharge.setText("");
                            WeightPackageRate weightPackage = weightPackageList.get(binding.packageParcelPage.
                                    getSelectedItemPosition());
                            exWeightPackage.setText(weightPackage.getName());


                            //double weight_input = Double.valueOf(binding.weight.getText().toString());
                            double weight_input = Double.valueOf(binding.weight.getText().toString());
                            weight_price = weightPackage.getRate();
                           // perkg = Double.valueOf(weightPackage.getPerKg());



                            //  last_weight_price = (Double.valueOf(weight_input) * Double.valueOf( weight_price));


                            // exWeightCharge.setText(String.valueOf(last_weight_price));
//                        exWeightCharge.setText(String.valueOf(weightPackage.getRate()));


                            exTotalCharge.setText(String.valueOf(Double.valueOf(weight_price)
                                    + Double.valueOf(exDeliveryCharge.getText().toString())));
                        }

                    } else {
                        exCodCharge.setText("");
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

///////Expandable Layout method

        ExpandableLayout expandableLayout
                = findViewById(R.id.expandable);
        expandableLayout.setExpandableAnimation(ExpandableAnimation.ACCELERATE);
        expandableLayout.secondLayout.findViewById(R.id.cod_percent);
        exCodPercent = expandableLayout.secondLayout.findViewById(R.id.cod_percent);
        exCodCharge = expandableLayout.secondLayout.findViewById(R.id.cod_charge);
        exWeightPackage = expandableLayout.secondLayout.findViewById(R.id.weight_package);
        exWeightCharge = expandableLayout.secondLayout.findViewById(R.id.weight_charge);
        exDeliveryCharge = expandableLayout.secondLayout.findViewById(R.id.delivery_charge);
        exTotalCharge = expandableLayout.secondLayout.findViewById(R.id.total_charge);


        expandableLayout.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpended) {
                    expandableLayout.collapse();
                    isExpended = false;
                } else {
                    expandableLayout.expand();
                    isExpended = true;
                }

            }
        });
        datainitilize();

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataValidation();
            }
        });


        //==========Spinner Listener======================
        binding.districtNameParcelPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.e("tesst",String.valueOf(binding.districtSp.getSelectedItemId()));
                if (binding.districtNameParcelPage.getSelectedItemId() != 0) {
                    getcahrge(districts.get(i).getId());
                    getUpozila(districts.get(i).getId());

//                    WeightPackage weightPackage = weightPackageList.get(i);
                    exWeightPackage.setText("");
                    exWeightCharge.setText(" ");
                    Collection_Amount.setText("");
                    binding.weight.setText("");
//                    exDeliveryCharge.setText(String.valueOf(weightPackage.getRate()));
//                    exTotalCharge.setText(String.valueOf(weightPackage.getRate() * 2) + "৳");

                } else {
                    exDeliveryCharge.setText("");
                    exCodPercent.setText("");
                    exTotalCharge.setText("");
                    exCodCharge.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.upazilaNameParcelPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Log.e("tesst", upozilaList.get(i).getName());
                if (binding.upazilaNameParcelPage.getSelectedItemId() != 0) {
                    getAreas(upozilaList.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.packageParcelPage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                binding.weight.setText("");
                if (binding.packageParcelPage.getSelectedItemPosition() > 0) {

                    binding.weightUnit.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(exCodCharge.getText().toString())) {
                        WeightPackageRate weightPackage = weightPackageList.get(i);

//                    WeightPackageRate weight = weightpackages.get(i);

                        exWeightPackage.setText(weightPackage.getName());

                        ///////
                        perkg=Double.valueOf(weightPackage.getPerKg());
                        Log.d("tesst",weightPackage.getPerKg());
                        // String weight_input = binding.weight.getText().toString();
                        weight_price = weightPackage.getRate();



                        // last_weight_price = (Double.valueOf(weight_input) *  weight_price);

                        //  exWeightCharge.setText(String.valueOf(last_weight_price));
                        exWeightCharge.setText(String.format("%1$,.2f", weightPackage.getRate()));


//                    exWeightCharge.setText(String.valueOf(we.getRate()));
//                    exWeightCharge.setText(String.valueOf(weight.getRate()));
//                    exDeliveryCharge.setText(String.valueOf(weightPackage.getRate()));


                        exTotalCharge.setText(String.valueOf(Double.valueOf(weight_price)
                                + Double.valueOf(exDeliveryCharge.getText().toString()) +
                                Double.valueOf(exCodCharge.getText().toString())));


                    } else {
                        WeightPackageRate weightPackage = weightPackageList.get(i);
                        exWeightPackage.setText(weightPackage.getName());
                        weight_price = weightPackage.getRate();
                        perkg=Double.valueOf(weightPackage.getPerKg());
                        Log.d("tesst",weightPackage.getPerKg());
                        exWeightCharge.setText(String.format("%1$,.2f", weightPackage.getRate()));


                        exTotalCharge.setText(String.valueOf(Double.valueOf(weightPackage.getRate())
                                + Double.valueOf(exDeliveryCharge.getText().toString())));
                        //Toast.makeText(Add_Parcel.this, "Please Enter Collection amount first", Toast.LENGTH_SHORT).show();


                    }
                } else {
                    binding.weightUnit.setVisibility(View.GONE);
                    exWeightPackage.setText("");
                    exWeightCharge.setText("");
                    exTotalCharge.setText("");
                }
                {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void datainitilize() {
        getDistrict();
        //  getWeightPackages();
        setDeliveryOption();
        getAddress();
    }


    //========setup Spinner==========================

    private void getDistrict() {
        progressDialog.show();
        api.getDistricts().enqueue(new Callback<DistrictsContainer>() {

            @Override
            public void onResponse(Call<DistrictsContainer> call, Response<DistrictsContainer> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {

                    districts = response.body().getDistricts();
                    districts.add(0, new District(0, "District"));

                    DistrictSpinnerAdaptar customeAdapterForSpinner = new DistrictSpinnerAdaptar(districts,
                            getApplicationContext());
                    binding.districtNameParcelPage.setAdapter(customeAdapterForSpinner);

                }
            }

            @Override
            public void onFailure(Call<DistrictsContainer> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getUpozila(Integer id) {
        progressDialog.show();
        api.getUpazilas(id).enqueue(new Callback<UpozilasContainer>() {
            @Override
            public void onResponse(Call<UpozilasContainer> call, Response<UpozilasContainer> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {

                    upozilaList = response.body().getUpozilaList();
                    upozilaList.add(0, new Upozila(0, "Upazila"));
                    UpozilaSpinnerAdaptar customeAdapterForSpinner = new UpozilaSpinnerAdaptar(upozilaList, getApplicationContext());
                    binding.upazilaNameParcelPage.setAdapter(customeAdapterForSpinner);

                }
            }

            @Override
            public void onFailure(Call<UpozilasContainer> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void getAreas(Integer id) {

        progressDialog.show();
        api.getAreas(id).enqueue(new Callback<AreaContainer>() {
            @Override
            public void onResponse(Call<AreaContainer> call, Response<AreaContainer> response) {

                progressDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {

                    areas = response.body().getArealist();
                    areas.add(0, new Area(0, "Area"));
                    AreaSpinnerAdapter areaSpinnerAdapter = new AreaSpinnerAdapter(areas, getApplicationContext());
                    binding.areaNameParcelPage.setAdapter(areaSpinnerAdapter);
                    areas = response.body().getArealist();
                }

            }

            @Override
            public void onFailure(Call<AreaContainer> call, Throwable t) {

                progressDialog.dismiss();
            }
        });

    }

    private void setDeliveryOption() {
        deliveryOptions.add(new DeliveryOption(0, "Payment Method"));
        deliveryOptions.add(new DeliveryOption(1, "Cash On Delivery"));
        deliveryOptions.add(new DeliveryOption(2, "Bkash"));
        DelivaryOptionSpinner delivaryOptionSpinner = new DelivaryOptionSpinner(deliveryOptions, getApplicationContext());
        binding.paymentMethodParcelPage.setAdapter(delivaryOptionSpinner);
    }


    //=============weight percel=============
//    private WeightPackageRate getWeightPackages() {
//
//
//        api.getWeightPackages().enqueue(new Callback<WeightPackageContainer>() {
//            @Override
//            public void onResponse(Call<WeightPackageContainer> call, Response<WeightPackageContainer> response) {
//                progressDialog.dismiss();
//                if (response.isSuccessful() && response.body() != null) {
//
//                    weightPackageList = response.body().getWeightPackageList();
//                    weightPackageList.add(0, new WeightPackage(0, "WeightPackage"));
//
//                    WeightPackageSpinnerAdapter customeAdapterForSpinner = new WeightPackageSpinnerAdapter(weightPackageList, getApplicationContext());
//                    binding.packageParcelPage.setAdapter(customeAdapterForSpinner);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<WeightPackageContainer> call, Throwable t) {
//
//            }
//        });
//
//        return null;
//    }

    //================dataValidation======================
    private void dataValidation() {
//        Log.e("toos", "dfjkdj");
        if (!TextUtils.isEmpty(binding.customerName.getText().toString())) {
            if (!TextUtils.isEmpty(binding.phoneNum.getText().toString())) {

                if (!TextUtils.isEmpty(binding.customerAddress.getText().toString())) {

                    if (binding.districtNameParcelPage.getSelectedItemPosition() > 0) {
                        if (binding.upazilaNameParcelPage.getSelectedItemPosition() > 0) {
//                            if (binding.areaNameParcelPage.getSelectedItemPosition() > 0) {
                            if (!TextUtils.isEmpty(binding.merchantOrderNo.getText().toString())) {
                                if (!TextUtils.isEmpty(binding.collectionAmountPercelPage.getText().toString())) {
                                    if (binding.packageParcelPage.getSelectedItemPosition() > 0) {
                                        if (binding.paymentMethodParcelPage.getSelectedItemPosition() > 0) {
                                            if (!TextUtils.isEmpty(binding.productDescriptionPercelPage.getText().toString())) {
                                                if (!TextUtils.isEmpty(binding.weight.getText().toString())) {
                                                    addPercel();
                                                } else {
                                                    binding.weight.setError("Please Enter Weight");
                                                    binding.weight.requestFocus();
                                                }

                                            } else {
                                                binding.productDescriptionPercelPage.setError("Please Enter product Description ");
                                                binding.productDescriptionPercelPage.requestFocus();
                                            }

                                        } else {
                                            Toast.makeText(getApplicationContext(), "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Select Weight Package", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    binding.collectionAmountPercelPage.setError("Please Input collectionAmount Parcel Page");
                                    binding.collectionAmountPercelPage.requestFocus();
                                }
                            } else {
                                binding.merchantOrderNo.setError("Please Input a valid Merchant OrderNo");
                                binding.merchantOrderNo.requestFocus();
                            }
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Please Select Area", Toast.LENGTH_SHORT).show();
//                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Please Select Upazila", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please Select a district", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    binding.customerAddress.setError("Please Input  Address");
                    binding.customerAddress.requestFocus();
                }
            } else {
                binding.phoneNum.setError("Please Input a Phone Number");
                binding.phoneNum.requestFocus();
            }

        } else {
            binding.customerName.setError("Please Input a Customer Name");
            binding.customerName.requestFocus();
        }
    }

    private void addPercel() {

        progressDialog.show();
        api = RetrofitClient.get(getApplicationContext()).create(Api.class);
        api.addpercel(binding.merchantOrderNo.getText().toString(),
                exCodPercent.getText().toString(),
                exCodCharge.getText().toString(),
                exDeliveryCharge.getText().toString(),
                exWeightCharge.getText().toString(),
                "3",
                exTotalCharge.getText().toString(),
                String.valueOf(weightPackageList.get(binding.packageParcelPage.getSelectedItemPosition()).getId().toString()),
                String.valueOf(deliveryOptions.get(binding.paymentMethodParcelPage.getSelectedItemPosition()).getId()),
                binding.productDescriptionPercelPage.getText().toString(),
                binding.collectionAmountPercelPage.getText().toString(),
                binding.customerName.getText().toString(),
                binding.phoneNum.getText().toString(),
                binding.customerAddress.getText().toString(),
                String.valueOf(districts.get(binding.districtNameParcelPage.getSelectedItemPosition()).
                        getId().toString()),
                String.valueOf(upozilaList.get(binding.upazilaNameParcelPage.getSelectedItemPosition()).
                        getId().toString()),
                String.valueOf(areas.get(binding.areaNameParcelPage.getSelectedItemPosition()).getId().
                        toString()),
                binding.remarks.getText().toString(), binding.pickupAddressShow.getText().toString(),
                binding.weight.getText().toString(),exWeightCharge.getText().toString()
        ).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    String success = response.body().get("success").getAsString();
                    String parcel_id = response.body().get("parcel_id").getAsString();

                    // Toast.makeText(Add_Parcel.this, "Successfully Added Parcel ", Toast.LENGTH_LONG).show();


//                    Toast toast = Toast.makeText(Add_Parcel.this, "Successfully Added Parcel", Toast.LENGTH_LONG);
//                    toast.getView().setBackgroundColor(Color.parseColor("#67EFC8"));
//                    toast.getView().setBackgroundColor(getResources().getColor(R.color.black));
//                    toast.setGravity(Gravity.CENTER, 0, 50);
//                    TextView text = toast.getView().findViewById(android.R.id.message);
//                    text.setTextColor(getResources().getColor(R.color.red));
//                    toast.show();

//                    AlertDialog.Builder builder=new AlertDialog.Builder(Add_Parcel.this);
//                    builder.setMessage("Add Parcel Successfully").setNegativeButton("ok", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            finish();
//                        }
//                    });
//                    AlertDialog alert = builder.create();
//                    alert.show();

//                    CustomDailoge cdd=new CustomDailoge(Add_Parcel.this);
//
//                    cdd.show();

                    MyAlertDialog myAlertDialog = new MyAlertDialog(Add_Parcel.this);
                    myAlertDialog.showConfirmDialog("Add Parcel Successfully", "ok", "cancel");
                    myAlertDialog.okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });

                    myAlertDialog.show();

                }  else {
                    try {
                        // Log.e("tesstss", response.errorBody().string());
                        try {
                            JSONObject json = new JSONObject(response.errorBody().string().toString());
                            Toast.makeText(Add_Parcel.this, json.getString("message"), Toast.LENGTH_SHORT).show();
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
                progressDialog.dismiss();
                Toast.makeText(Add_Parcel.this, "Something is worng", Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
//        moveTaskToBack(true);
        return true;
    }

    public void getcahrge(int id) {
        progressDialog.show();
        String mid = MySharedPreference.getInstance(getApplicationContext()).
                getString(Constant.MERCENTID, "not found");
        if (mid.equals(new String("not found"))) {
            Toast.makeText(Add_Parcel.this, "merchant id not found", Toast.LENGTH_LONG).show();
        } else {
            api.getcharge(id, mid).enqueue(new Callback<ChargeDeliveryAddPercel>() {
                @Override
                public void onResponse(Call<ChargeDeliveryAddPercel> call, Response<ChargeDeliveryAddPercel> response) {
                    progressDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {

//                        exDeliveryCharge.setText(String.format("%1$,.2f",(response.body().getCharge())));

/////change delivery cahrge converstion
                        exDeliveryCharge.setText(String.valueOf(response.body().getCharge().substring(0, 5)));


                        exCodPercent.setText(String.valueOf(response.body().getCodChargePercent()));
                        cod_percent = Double.valueOf(response.body().getCodChargePercent());
                        weightPackageList = response.body().getWeightlist();
//                        Log.e("error parcel",response.body().getWeightlist().get(0).getName());
                        weightPackageList.add(0, new WeightPackageRate(0, "Delivery Package"));

                        Weightpackagespinner customeAdapterForSpinner = new Weightpackagespinner(weightPackageList,
                                getApplicationContext());
                        binding.packageParcelPage.setAdapter(customeAdapterForSpinner);
//                        exWeightCharge=Double.valueOf(response.body().getCharge());
//                        exWeightCharge.setText(String.valueOf(response.body().));
//                        getWeightPackages();
                    }
                    else {
                        try {
                            // Log.e("tesstss", response.errorBody().string());
                            try {
                                JSONObject json = new JSONObject(response.errorBody().string().toString());
                                Toast.makeText(Add_Parcel.this, json.getString("message"), Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<ChargeDeliveryAddPercel> call, Throwable t) {

                }
            });
        }
    }

    public void getAddress() {
//        String midi = MySharedPreference.getInstance(getApplicationContext()).
//                getString(Constant.BUSINESSADDRESS, "not found");

        ArrayList<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(0, "--Select Address--");
        spinnerArray.add(1, "Others Address");
        spinnerArray.add(2, "Default Address");


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        PickupAddress.setAdapter(spinnerArrayAdapter);

        PickupAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 1) {
                    String midi = MySharedPreference.getInstance(getApplicationContext()).
                            getString(Constant.BUSINESSADDRESS, "not found");
                    if (!midi.equals(new String("not found"))) {
                        binding.pickupAddressShow.setText(midi);
                    }


                } else if (position == 2) {
                    String midi = MySharedPreference.getInstance(getApplicationContext()).
                            getString(Constant.ADDRESS, "not found");
                    if (!midi.equals(new String("not found"))) {
                        binding.pickupAddressShow.setText(midi);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void getweightcharge(int id) {
        progressDialog.show();
        String midi = MySharedPreference.getInstance(getApplicationContext()).
                getString(Constant.MERCENTID, "not found");


    }
}





