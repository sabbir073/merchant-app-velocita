package com.stitbd.parcelwala.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stitbd.parcelwala.model.Profile.Profile;

public class ProfileContainerOfOTP {

    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("merchant")
    @Expose
    private Profile merchant;




    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Profile getMerchant() {
        return merchant;
    }

    public void setMerchant(Profile merchant) {
        this.merchant = merchant;
    }







}
