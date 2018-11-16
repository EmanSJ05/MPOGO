package com.emansj.mpogo.model;

import android.content.Context;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.Tools;

import java.util.ArrayList;
import java.util.List;

public class UserRating {

    public int groupId;
    public String groupName;

    public int id;
    public String name;
    public String subTitle = null;
    public String caption = null;

    public Integer totalUser;
    public Integer totalAktifitas;
    public Integer totalPoin;

    public String imageUrl = null;


    public UserRating() {

    }


    //kewenangan
    public static List<UserRating> getRatingProvinsi(Context ctx) {
        List<UserRating> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.ur_per_provinsi);

        for (int i = 0; i < dtArray.length; i++) {

            UserRating obj = new UserRating();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");

            obj.id = Integer.parseInt(dt[0]);
            obj.name = dt[1];
            obj.totalUser = Integer.parseInt(dt[2]);
            obj.totalAktifitas = Integer.parseInt(dt[3]);
            obj.totalPoin = Integer.parseInt(dt[4]);

            items.add(obj);
        }
        return items;
    }

    //kegiatan
    public static List<UserRating> getRatingKabupaten(Context ctx) {
        List<UserRating> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.ur_per_kabupaten);

        for (int i = 0; i < dtArray.length; i++) {

            UserRating obj = new UserRating();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");

            obj.groupId = Integer.parseInt(dt[0]);
            obj.groupName = dt[1];
            obj.id = Integer.parseInt(dt[2]);
            obj.name = dt[3];
            obj.totalUser = Integer.parseInt(dt[4]);
            obj.totalAktifitas = Integer.parseInt(dt[5]);
            obj.totalPoin = Integer.parseInt(dt[6]);

            items.add(obj);
        }
        return items;
    }

    //kegiatan & provinsi
    public static List<UserRating> getRatingAllUser(Context ctx) {
        List<UserRating> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.ur_all_user);

        for (int i = 0; i < dtArray.length; i++) {

            UserRating obj = new UserRating();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");

            obj.groupId = Integer.parseInt(dt[0]); //ProvId
            obj.groupName = dt[1]; //ProvName
            obj.id = Integer.parseInt(dt[2]); //UserId
            obj.name = dt[3]; //NickName
            obj.subTitle = dt[4]; //FullName
            obj.caption = dt[5]; //KabName
            obj.totalAktifitas = Integer.parseInt(dt[6]); //TotalAktifitas
            obj.totalPoin = Integer.parseInt(dt[7]); //TotalPoin

            obj.imageUrl = dt[8];
            if (!obj.imageUrl.equals("-")) {
//                obj.imageDrawableResource = Tools.getDrawableResource(ctx, obj.imageUrl);
//                obj.imageDrawable = Tools.getImageDrawable(ctx, obj.imageUrl);
            }

            items.add(obj);
        }
        return items;
    }

}

