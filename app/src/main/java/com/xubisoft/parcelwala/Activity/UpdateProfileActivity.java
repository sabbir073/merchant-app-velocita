package com.xubisoft.parcelwala.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
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
import com.xubisoft.parcelwala.Adaptar.AreaSpinnerAdapter;
import com.xubisoft.parcelwala.Adaptar.DistrictSpinnerAdaptar;
import com.xubisoft.parcelwala.Adaptar.UpozilaSpinnerAdaptar;
import com.xubisoft.parcelwala.databinding.ActivityAddParcelBinding;
import com.xubisoft.parcelwala.databinding.ActivityUpdateProfileBinding;
import com.xubisoft.parcelwala.model.Area;
import com.xubisoft.parcelwala.model.AreaContainer;
import com.xubisoft.parcelwala.model.District;
import com.xubisoft.parcelwala.model.DistrictsContainer;
import com.xubisoft.parcelwala.model.UpdateProfile.Merchant;
import com.xubisoft.parcelwala.model.UpdateProfile.UserInfoResponse;
import com.xubisoft.parcelwala.model.Upozila;
import com.xubisoft.parcelwala.model.UpozilasContainer;
import com.xubisoft.parcelwala.network.Api;
import com.xubisoft.parcelwala.network.RetrofitClient;
import com.xubisoft.parcelwala.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    Api api;
    ProgressDialog progressDialog;
    ActivityUpdateProfileBinding binding;
    Bitmap bitmap;
    boolean isNid1 = false, isTrade = false, isTin = false, isNid2 = false;
    Uri imageUriNid = null, imageUriNid2 = null, imageUriTrade = null, imageUriTin = null, imageUriNid3 = null;
    File f1nid, f2nid, f3trade, f4tin, f5nid;
    Uri uri;
    List<District> districts;
    List<Upozila> upozilaList;
    List<Area> areas;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Profile");
        progressDialog = new ProgressDialog(UpdateProfileActivity.this);
        progressDialog.setMessage("Please wait.....");
        api = RetrofitClient.get(getApplicationContext()).create(Api.class);

        datainitilize();
        getDistrict();

        binding.districtSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("tesst", String.valueOf(binding.districtSp.getSelectedItemId()));
                if (binding.districtSp.getSelectedItemId() != 0) {
                    getUpozila(districts.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.upozilaSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("tesst", upozilaList.get(i).getName());
                if (binding.upozilaSp.getSelectedItemId() != 0) {
                    getAreas(upozilaList.get(i).getId());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.takeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permison(Constant.PICK_PHOTO_ONE);
            }
        });


        binding.profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataValidation();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);
        }

    }


    public void datainitilize() {
        progressDialog.show();
        api.getUserInfo().enqueue(new Callback<UserInfoResponse>() {
            @Override
            public void onResponse(Call<UserInfoResponse> call, Response<UserInfoResponse> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Merchant merchant = response.body().getMerchant();
                    binding.takeAddress.setText(merchant.getAddress());
                    binding.takeCName.setText(merchant.getCompanyName());
                    binding.takeEmail.setText(merchant.getEmail());
                    binding.takeBaddress.setText(merchant.getBusinessAddress());
                    binding.takeName.setText(merchant.getName());
                    binding.takePhn.setText(merchant.getContactNumber());
                    binding.accountName.setText(merchant.getBankAccountName());
                    binding.accountNumber.setText(merchant.getBankAccountNo());
                    binding.bankName.setText(merchant.getBankName());
                    binding.bkashNumber.setText(merchant.getBkashNumber());
                    binding.nagadNumber.setText(merchant.getNagadNumber());
                    binding.rocketNumber.setText(merchant.getRocketName());
                    binding.nidNumber.setText(String.valueOf(merchant.getNidNo()));
                    binding.facebookLink.setText(String.valueOf(merchant.getFbUrl()));
                } else {
                    Toast.makeText(UpdateProfileActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(UpdateProfileActivity.this, "Something Wrong" + t.toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void dataValidation() {
        if (!TextUtils.isEmpty(binding.takeName.getText().toString())) {
            if (!TextUtils.isEmpty(binding.takeEmail.getText().toString())) {
                if (!TextUtils.isEmpty(binding.takePhn.getText().toString())) {
                    if (!TextUtils.isEmpty(binding.takeCName.getText().toString())) {
                        if (!TextUtils.isEmpty(binding.takeAddress.getText().toString())) {
                            if (!TextUtils.isEmpty(binding.takeBaddress.getText().toString())) {
                                if (binding.districtSp.getSelectedItemPosition() > 0) {
                                    if (binding.upozilaSp.getSelectedItemPosition() > 0) {
                                        Upadate();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Please Select Upazila", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "Please Select a district", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                binding.takeBaddress.setError("Please Inpute your address");
                                binding.takeBaddress.requestFocus();
                            }
                        } else {
                            binding.takeAddress.setError("Please Inpute your address");
                            binding.takeAddress.requestFocus();
                        }
                    } else {
                        binding.takeCName.setError("Please Enter your Name");
                        binding.takeCName.requestFocus();
                    }
                } else {
                    binding.takePhn.setError("Please Enter your Name");
                    binding.takePhn.requestFocus();
                }

            } else {
                binding.takeEmail.setError("Please Enter your Name");
                binding.takeEmail.requestFocus();
            }

        } else {
            binding.takeName.setError("Please Enter your Name");
            binding.takeName.requestFocus();
        }
    }

    private void Upadate() {

        progressDialog.show();
        RequestBody requestBody;

        String nidpicName = "";
        RequestBody requestNid1 = null;
        RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
        if (imageUriNid != null) {
            File nidfile = new File(imageUriNid.getLastPathSegment().toString());
            nidpicName = nidfile.getName();

            requestNid1 =
                    RequestBody.create(MediaType.parse("multipart/form-data"), f1nid);
        }

        requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("company_name", binding.takeCName.getText().toString())
                .addFormDataPart("contact_number", binding.takePhn.getText().toString())
                .addFormDataPart("name", binding.takeName.getText().toString())
                .addFormDataPart("business_address", binding.takeBaddress.getText().toString())
                .addFormDataPart("address", binding.takeAddress.getText().toString())
                .addFormDataPart("district_id", String.valueOf(districts.get(binding.districtSp.getSelectedItemPosition()).getId()))
                .addFormDataPart("upazila_id", String.valueOf(upozilaList.get(binding.upozilaSp.getSelectedItemPosition()).getId()))
                .addFormDataPart("area_id", String.valueOf(areas.get(binding.areaSp.getSelectedItemPosition()).getId()))
                .addFormDataPart("email", binding.takeEmail.getText().toString())
                .addFormDataPart("bank_account_name", binding.accountName.getText().toString())
                .addFormDataPart("bank_name", binding.bankName.getText().toString())
                .addFormDataPart("bank_account_no", binding.accountNumber.getText().toString())
                .addFormDataPart("bkash_number", binding.bkashNumber.getText().toString())
                .addFormDataPart("nagad_number", binding.nagadNumber.getText().toString())
                .addFormDataPart("rocket_name", binding.rocketNumber.getText().toString())
                .addFormDataPart("nid_no", binding.nidNumber.getText().toString())
                .addFormDataPart("fb_url", binding.facebookLink.getText().toString())
                .addFormDataPart("image", nidpicName, requestNid1 != null ? requestNid1 : attachmentEmpty)
                .build();


        api.getproupdate(requestBody).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {

                    Toast.makeText(UpdateProfileActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();

                    finish();
                }  else {
                    try {
                        // Log.e("tesstss", response.errorBody().string());
                        try {
                            JSONObject json = new JSONObject(response.errorBody().string().toString());
                            Toast.makeText(UpdateProfileActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProfileActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    void permison(int requestcode) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 201);

        } else {
            Log.e("REQ", "Inside");
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            /*Intent intent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);*/
            //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            startActivityForResult(intent, requestcode);
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    private void getDistrict() {
        progressDialog.show();

        api.getDistricts().enqueue(new Callback<DistrictsContainer>() {

            @Override
            public void onResponse(Call<DistrictsContainer> call, Response<DistrictsContainer> response) {
                progressDialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {

                    districts = response.body().getDistricts();
                    districts.add(0, new District(0, "District"));

                    DistrictSpinnerAdaptar customeAdapterForSpinner = new DistrictSpinnerAdaptar(districts, getApplicationContext());
                    binding.districtSp.setAdapter(customeAdapterForSpinner);


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
                    binding.upozilaSp.setAdapter(customeAdapterForSpinner);

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
                    binding.areaSp.setAdapter(areaSpinnerAdapter);
                    areas = response.body().getArealist();
                }

            }

            @Override
            public void onFailure(Call<AreaContainer> call, Throwable t) {

                progressDialog.dismiss();
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap b;
        if (resultCode == RESULT_OK && requestCode == Constant.PICK_PHOTO_ONE && data != null) {

            imageUriNid = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUriNid);

                f1nid = new File(getCacheDir(), "Image1");
                bitmap = getResizedBitmap(bitmap, 800);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
                // bitmap.compress(Bitmap.CompressFormat.JPEG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();


                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(f1nid);

                } catch (FileNotFoundException e) {
                    Log.e("REQ", e.toString());
                }
                try {
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    isNid1 = true;
                } catch (IOException e) {
                    Log.e("REQ", e.toString());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bitmap != null) {
                //binding.nidPic.setVisibility(View.VISIBLE);
                binding.profilePic.setImageBitmap(bitmap);
            } else {
                Log.e("REQ", "Bitmap null");
            }

        }

    }
}
