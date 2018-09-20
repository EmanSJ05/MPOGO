package com.emansj.mpogo.model;

import android.content.Context;

import com.emansj.mpogo.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DashboardKegut {

    public String categoryName;
    public String realPercent;

    public DashboardKegut() {

    }

    //Dummy data
    public static List<DashboardKegut> getDashboardKegutSmart(Context ctx) {
        List<DashboardKegut> items = new ArrayList<>();

        String categoryName_arr[] = ctx.getResources().getStringArray(R.array.kegut_smart_data);
        String realPercent_arr[] = ctx.getResources().getStringArray(R.array.kegut_smart_realisasi);

        for (int i = 0; i < categoryName_arr.length; i++) {
            DashboardKegut obj = new DashboardKegut();
            obj.categoryName = categoryName_arr[i];
            obj.realPercent = realPercent_arr[i];
            items.add(obj);
        }
        //Collections.shuffle(items);
        return items;
    }

    //Dummy data
    public static List<DashboardKegut> getDashboardKegutMpo(Context ctx) {
        List<DashboardKegut> items = new ArrayList<>();

        String categoryName_arr[] = ctx.getResources().getStringArray(R.array.kegut_mpo_data);
        String realPercent_arr[] = ctx.getResources().getStringArray(R.array.kegut_mpo_realisasi);

        for (int i = 0; i < categoryName_arr.length; i++) {
            DashboardKegut obj = new DashboardKegut();
            obj.categoryName = categoryName_arr[i];
            obj.realPercent = realPercent_arr[i];
            items.add(obj);
        }
        //Collections.shuffle(items);
        return items;
    }
}
