package com.xubisoft.parcelwala.Adaptar.AddpercelWeightpackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xubisoft.parcelwala.Adaptar.listener.Clicklistener;
import com.xubisoft.parcelwala.R;
import com.xubisoft.parcelwala.model.GetChargeDelivery.WeightPackageRate;

import java.util.ArrayList;
import java.util.List;

public class Weightpackagespinner extends BaseAdapter {

    List<WeightPackageRate> weightPackages=new ArrayList<>();
    LayoutInflater inflter;
    Clicklistener clicklistener;

    public Weightpackagespinner(List<WeightPackageRate> weightPackages, Context applicationContext) {
        this.weightPackages = weightPackages;
        inflter = (LayoutInflater.from(applicationContext));
        this.clicklistener=clicklistener;
    }

    @Override
    public int getCount() {
        return weightPackages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return weightPackages.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custome_spinner, null);
        TextView name=view.findViewById(R.id.textView);
        name.setText(weightPackages.get(i).getName());

        return view;
    }
}