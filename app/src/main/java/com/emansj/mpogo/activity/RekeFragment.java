package com.emansj.mpogo.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emansj.mpogo.R;

public class RekeFragment extends Fragment {

    //Standard vars
    private static final String TAG = RekeFragment.class.getSimpleName();
    private Context m_Ctx;
    private View parent_view;
    private View.OnClickListener m_OnClickListener;


    public RekeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.fragment_rk, container, false);
        initComponent();

        //init menu listener
        m_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(v);
            }
        };
        initListener();

        return parent_view;
    }


    private void initListener() {
        //kewenangan
        CardView cvKewenangan = parent_view.findViewById(R.id.cvKewenangan);
        cvKewenangan.setOnClickListener(m_OnClickListener);
        View lyKewenangan = parent_view.findViewById(R.id.lyKewenangan);
        lyKewenangan.setOnClickListener(m_OnClickListener);
        TextView tvKewenangan = parent_view.findViewById(R.id.tvKewenangan);
        tvKewenangan.setOnClickListener(m_OnClickListener);
        ImageView imvKewenangan = parent_view.findViewById(R.id.imvKewenangan);
        imvKewenangan.setOnClickListener(m_OnClickListener);

        //kegiatan
        CardView cvKegiatan = parent_view.findViewById(R.id.cvKegiatan);
        cvKegiatan.setOnClickListener(m_OnClickListener);
        View lyKegiatan = parent_view.findViewById(R.id.lyKegiatan);
        lyKegiatan.setOnClickListener(m_OnClickListener);
        TextView tvKegiatan = parent_view.findViewById(R.id.tvKegiatan);
        tvKegiatan.setOnClickListener(m_OnClickListener);
        ImageView imvKegiatan = parent_view.findViewById(R.id.imvKegiatan);
        imvKegiatan.setOnClickListener(m_OnClickListener);

        //kegiatan output
        CardView cvKegiatanOutput = parent_view.findViewById(R.id.cvKegiatanOutput);
        cvKegiatanOutput.setOnClickListener(m_OnClickListener);
        View lyKegiatanOutput = parent_view.findViewById(R.id.lyKegiatanOutput);
        lyKegiatanOutput.setOnClickListener(m_OnClickListener);
        TextView tvKegiatanOutput = parent_view.findViewById(R.id.tvKegiatanOutput);
        tvKegiatanOutput.setOnClickListener(m_OnClickListener);
        ImageView imvKegiatanOutput = parent_view.findViewById(R.id.imvKegiatanOutput);
        imvKegiatanOutput.setOnClickListener(m_OnClickListener);

        //kegiatan n provinsi
        CardView cvKegiatanNProvinsi = parent_view.findViewById(R.id.cvKegiatanNProvinsi);
        cvKegiatanNProvinsi.setOnClickListener(m_OnClickListener);
        View lyKegiatanNProvinsi = parent_view.findViewById(R.id.lyKegiatanNProvinsi);
        lyKegiatanNProvinsi.setOnClickListener(m_OnClickListener);
        TextView tvKegiatanNProvinsi = parent_view.findViewById(R.id.tvKegiatanNProvinsi);
        tvKegiatanNProvinsi.setOnClickListener(m_OnClickListener);
        ImageView imvKegiatanNProvinsi = parent_view.findViewById(R.id.imvKegiatanNProvinsi);
        imvKegiatanNProvinsi.setOnClickListener(m_OnClickListener);

        //satker
        CardView cvSatker = parent_view.findViewById(R.id.cvSatker);
        cvSatker.setOnClickListener(m_OnClickListener);
        View lySatker = parent_view.findViewById(R.id.lySatker);
        lySatker.setOnClickListener(m_OnClickListener);
        TextView tvSatker = parent_view.findViewById(R.id.tvSatker);
        tvSatker.setOnClickListener(m_OnClickListener);
        ImageView imvSatker = parent_view.findViewById(R.id.imvSatker);
        imvSatker.setOnClickListener(m_OnClickListener);

        //output
        CardView cvOutput = parent_view.findViewById(R.id.cvOutput);
        cvOutput.setOnClickListener(m_OnClickListener);
        View lyOutput = parent_view.findViewById(R.id.lyOutput);
        lyOutput.setOnClickListener(m_OnClickListener);
        TextView tvOutput = parent_view.findViewById(R.id.tvOutput);
        tvOutput.setOnClickListener(m_OnClickListener);
        ImageView imvOutput = parent_view.findViewById(R.id.imvOutput);
        imvOutput.setOnClickListener(m_OnClickListener);
    }

    private void initComponent() {

    }

    public void gotoActivity(View v)
    {
        String title = "Report Title";

        String tag = v.getTag().toString();
        switch(tag) {
            case "Kewenangan":
                title = "Kewenangan";
                break;
            case "Kegiatan":
                title = "Kegiatan";
                break;
            case "KegiatanOutput":
                title = "Kegiatan Output";
                break;
            case "KegiatanNProvinsi":
                title = "Kegiatan dan Provinsi";
                break;
            case "Satker":
                title = "Satker";
                break;
            case "Output":
                title = "Output";
                break;
        }

        Intent i = new Intent(m_Ctx, RekeActivity.class);
        i.putExtra("title", title);
        i.putExtra("report_name", tag);
        startActivity(i);
    }

}
