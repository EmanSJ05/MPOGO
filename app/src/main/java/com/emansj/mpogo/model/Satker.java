package com.emansj.mpogo.model;

import android.content.Context;
import com.emansj.mpogo.R;
import java.util.ArrayList;
import java.util.List;

public class Satker {

    public int idSatker;
    public String kdSatker;
    public String nmSatker;
    public boolean isSelected;


    public Satker() {

    }

    public static List<Satker> getSatker(Context ctx) {
        List<Satker> items = new ArrayList<>();
        String dtArray[] = ctx.getResources().getStringArray(R.array.master_satker);

        for (int i = 0; i < dtArray.length; i++) {

            Satker obj = new Satker();
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");

            obj.idSatker = Integer.parseInt(dt[1]);
            obj.kdSatker = dt[2];
            obj.nmSatker = dt[3];

            items.add(obj);
        }
        return items;
    }

}

