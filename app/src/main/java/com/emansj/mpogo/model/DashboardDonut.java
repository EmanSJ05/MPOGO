package com.emansj.mpogo.model;

import android.content.Context;
import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.Tools;
import java.util.Date;

public class DashboardDonut {

    public Double pagu;
    public Date smartLastUpdate;
    public Double smartRealisasi;
    public Double smartPercent;
    public Double smartSisa;
    public Date mpoLastUpdate;
    public Double mpoRealisasi;
    public Double mpoPercent;
    public Double mpoSisa;

    public DashboardDonut(){
    }

    public void getDashboard(Context ctx){
        String dtArray[] = ctx.getResources().getStringArray(R.array.dash_pie_smart);

        for (int i = 0; i < dtArray.length; i++){
            String str = dtArray[i].toString();
            String[] dt = str.split("\\|");

            this.pagu = Double.parseDouble(dt[0]);

            this.smartLastUpdate = Tools.convertDateSTD(dt[1].toString(), "yyyy-MM-dd");
            this.smartRealisasi = Double.parseDouble(dt[2]);
            this.smartPercent = Double.parseDouble(dt[3]);
            this.smartSisa = Double.parseDouble(dt[4]);

            this.mpoLastUpdate = Tools.convertDateSTD(dt[5].toString(), "yyyy-MM-dd");
            this.mpoRealisasi = Double.parseDouble(dt[6]);
            this.mpoPercent = Double.parseDouble(dt[7]);
            this.mpoSisa = Double.parseDouble(dt[8]);
        }
    }

}
