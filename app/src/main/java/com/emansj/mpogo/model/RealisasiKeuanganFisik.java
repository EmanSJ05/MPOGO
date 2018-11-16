package com.emansj.mpogo.model;

import android.content.Context;
import android.widget.Toast;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.Tools;

import java.util.ArrayList;
import java.util.List;

public class RealisasiKeuanganFisik {

    public String tag;
    public String title;
    public String subTitle;
    public Double pagu;
    public Double smartValue;
    public Double smartPercent;
    public Double mpoValue;
    public Double mpoPercent;
    public Double fisikTarget;
    public Double fisikValue;
    public Double fisikPercent;
    public Double fisikProgress;
    public Double fisikProgressTerbobot;
    public boolean expanded = false;


    public RealisasiKeuanganFisik() {

    }


    //kegiatan
    public static List<RealisasiKeuanganFisik> getKegiatan(Context ctx) {
        List<RealisasiKeuanganFisik> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rkf_kegiatan);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuanganFisik obj = new RealisasiKeuanganFisik();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            obj.fisikProgress = Double.parseDouble(dt[6]);
            obj.fisikProgressTerbobot = Double.parseDouble(dt[7]);
            items.add(obj);
        }
        return items;
    }

    //kegiatan output
    public static List<RealisasiKeuanganFisik> getKegiatanOutput(Context ctx) {
        List<RealisasiKeuanganFisik> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rkf_kegiatan_output);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuanganFisik obj = new RealisasiKeuanganFisik();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            obj.fisikTarget = Double.parseDouble(dt[6]);
            obj.fisikValue = Double.parseDouble(dt[7]);
            obj.fisikPercent = Double.parseDouble(dt[8]);
            items.add(obj);
        }
        return items;
    }

    //output
    public static List<RealisasiKeuanganFisik> getOutput(Context ctx) {
        List<RealisasiKeuanganFisik> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rkf_output);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuanganFisik obj = new RealisasiKeuanganFisik();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.title = dt[0];
            obj.pagu = Double.parseDouble(dt[1]);
            obj.smartValue = Double.parseDouble(dt[2]);
            obj.smartPercent = Double.parseDouble(dt[3]);
            obj.mpoValue = Double.parseDouble(dt[4]);
            obj.mpoPercent = Double.parseDouble(dt[5]);
            obj.fisikTarget = Double.parseDouble(dt[6]);
            obj.fisikValue = Double.parseDouble(dt[7]);
            obj.fisikPercent = Double.parseDouble(dt[8]);
            items.add(obj);
        }
        return items;
    }

    //output & provinsi (SMART & MPO)
    public static List<RealisasiKeuanganFisik> getOutputProvinsiSmartMpo(Context ctx) {
        List<RealisasiKeuanganFisik> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.rkf_output_provinsi_SMART_MPO);
        for (int i = 0; i < dtArray.length; i++) {
            RealisasiKeuanganFisik obj = new RealisasiKeuanganFisik();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");
            obj.tag = dt[0];
            obj.title = dt[1];
            obj.pagu = Double.parseDouble(dt[2]);
            obj.smartValue = Double.parseDouble(dt[3]);
            obj.smartPercent = Double.parseDouble(dt[4]);
            obj.mpoValue = Double.parseDouble(dt[5]);
            obj.mpoPercent = Double.parseDouble(dt[6]);
            obj.fisikTarget = Double.parseDouble(dt[7]);
            obj.fisikValue = Double.parseDouble(dt[8]);
            obj.fisikPercent = Double.parseDouble(dt[9]);
            items.add(obj);
        }
        return items;
    }

}

