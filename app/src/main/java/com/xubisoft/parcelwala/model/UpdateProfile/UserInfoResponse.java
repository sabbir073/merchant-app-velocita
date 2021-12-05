package com.xubisoft.parcelwala.model.UpdateProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("merchant")
    @Expose
    private Merchant merchant;
    @SerializedName("message")
    @Expose
    private String message;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
