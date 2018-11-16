package com.emansj.mpogo.model;

import android.content.Context;
import com.emansj.mpogo.R;
import java.util.ArrayList;
import java.util.List;

public class Satker {

    public int SatkerId;
    public String KodeSatker;
    public String NamaSatker;
    public String Satker;
    public boolean IsSelected;


    public Satker() {

    }

    public static List<Satker> getSatker(Context ctx) {
        List<Satker> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.master_satker);

        for (int i = 0; i < dtArray.length; i++) {

            Satker obj = new Satker();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");

            obj.SatkerId = Integer.parseInt(dt[1]);
            obj.KodeSatker = dt[2];
            obj.NamaSatker = dt[3];

            items.add(obj);
        }
        return items;
    }

}

