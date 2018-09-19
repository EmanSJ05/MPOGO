package com.emansj.mpogo.model;

import android.content.Context;
import android.widget.Toast;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.Tools;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RealisasiKeuangan {

    public String tag;
    public String title;
    public String subTitle;
    public Double pagu;
    public Double smartValue;
    public Double smartPercent;
    public Double mpoValue;
    public Double mpoPercent;


    public RealisasiKeuangan() {

    }


    //kewenangan
    public static List<RealisasiKeuangan> getKewenangan(Context ctx) {
        List<RealisasiKeuangan> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rk_kewenangan);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuangan obj = new RealisasiKeuangan();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            items.add(obj);
        }
        return items;
    }

    //kegiatan
    public static List<RealisasiKeuangan> getKegiatan(Context ctx) {
        List<RealisasiKeuangan> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rk_kegiatan);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuangan obj = new RealisasiKeuangan();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            items.add(obj);
        }
        return items;
    }

    //kegiatan & provinsi
    public static List<RealisasiKeuangan> getKegiatanProvinsi(Context ctx) {
        List<RealisasiKeuangan> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rk_kegiatan_n_provinsi);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuangan obj = new RealisasiKeuangan();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.tag = dt[0];
            obj.title = dt[1];
            obj.pagu = Double.parseDouble(dt[2]);
            obj.smartValue = Double.parseDouble(dt[3]);
            obj.smartPercent = Double.parseDouble(dt[4]);
            obj.mpoValue = Double.parseDouble(dt[5]);
            obj.mpoPercent = Double.parseDouble(dt[6]);
            items.add(obj);
        }
        return items;
    }

    //kegiatan & output
    public static List<RealisasiKeuangan> getKegiatanOutput(Context ctx) {
        List<RealisasiKeuangan> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rk_kegiatan_output);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuangan obj = new RealisasiKeuangan();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            items.add(obj);
        }
        return items;
    }

    //satker
    public static List<RealisasiKeuangan> getSatker(Context ctx) {
        List<RealisasiKeuangan> items = new ArrayList<>();
        String title = "";

        try {
            String dtArray[] = ctx.getResources().getStringArray(R.array.rk_satker);
            for (int i = 0; i < dtArray.length; i++) {
                RealisasiKeuangan obj = new RealisasiKeuangan();
                String str = dtArray[i].toString();
                String[] dt = str.split("\\|");
                obj.title = Tools.initCaps(dt[0]);
                title = dt[0];
                obj.pagu = Double.parseDouble(dt[1]);
                obj.smartValue = Double.parseDouble(dt[2]);
                obj.smartPercent = Double.parseDouble(dt[3]);
                obj.mpoValue = Double.parseDouble(dt[4]);
                obj.mpoPercent = Double.parseDouble(dt[5]);
                items.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(ctx, "Error on : " + title, Toast.LENGTH_LONG).show();
        }
        return items;
    }

    //output
    public static List<RealisasiKeuangan> getOutput(Context ctx) {
        List<RealisasiKeuangan> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rk_output);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuangan obj = new RealisasiKeuangan();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            items.add(obj);
        }
        return items;
    }

}

