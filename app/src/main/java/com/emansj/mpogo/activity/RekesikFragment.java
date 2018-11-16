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


public class RekesikFragment extends Fragment {

    //Standard vars
    private static final String TAG = RekesikFragment.class.getSimpleName();
    private Context m_Ctx;
    private View parent_view;
    private View.OnClickListener m_OnClickListener;

    public RekesikFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.fragment_rkf, container, false);

        initComponent();

        return parent_view;
    }

    private void initComponent() {
        //set one action for all
        m_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoActivity(v);
            }
        };

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

        //kegiatan output
        CardView cvOutput = parent_view.findViewById(R.id.cvOutput);
        cvOutput.setOnClickListener(m_OnClickListener);
        View lyOutput = parent_view.findViewById(R.id.lyOutput);
        lyOutput.setOnClickListener(m_OnClickListener);
        TextView tvOutput = parent_view.findViewById(R.id.tvOutput);
        tvOutput.setOnClickListener(m_OnClickListener);
        ImageView imvOutput = parent_view.findViewById(R.id.imvOutput);
        imvOutput.setOnClickListener(m_OnClickListener);

        //output n provinsi smart
        CardView cvOutputNProvinsiSmart = parent_view.findViewById(R.id.cvOutputNProvinsiSmart);
        cvOutputNProvinsiSmart.setOnClickListener(m_OnClickListener);
        View lyOutputNProvinsiSmart = parent_view.findViewById(R.id.lyOutputNProvinsiSmart);
        lyOutputNProvinsiSmart.setOnClickListener(m_OnClickListener);
        TextView tvOutputNProvinsiSmart = parent_view.findViewById(R.id.tvOutputNProvinsiSmart);
        tvOutputNProvinsiSmart.setOnClickListener(m_OnClickListener);
        ImageView imvOutputNProvinsiSmart = parent_view.findViewById(R.id.imvOutputNProvinsiSmart);
        imvOutputNProvinsiSmart.setOnClickListener(m_OnClickListener);

        //output n provinsi mpo
        CardView cvOutputNProvinsiMpo = parent_view.findViewById(R.id.cvOutputNProvinsiMpo);
        cvOutputNProvinsiMpo.setOnClickListener(m_OnClickListener);
        View lyOutputNProvinsiMpo = parent_view.findViewById(R.id.lyOutputNProvinsiMpo);
        lyOutputNProvinsiMpo.setOnClickListener(m_OnClickListener);
        TextView tvOutputNProvinsiMpo = parent_view.findViewById(R.id.tvOutputNProvinsiMpo);
        tvOutputNProvinsiMpo.setOnClickListener(m_OnClickListener);
        ImageView imvOutputNProvinsiMpo = parent_view.findViewById(R.id.imvOutputNProvinsiMpo);
        imvOutputNProvinsiMpo.setOnClickListener(m_OnClickListener);
    }

    public void gotoActivity(View v)
    {
        String title = "Report Title";

        String tag = v.getTag().toString();
        switch(tag) {
            case "Kegiatan":
                title = "Kegiatan";
                break;
            case "KegiatanOutput":
                title = "Kegiatan Output";
                break;
            case "Output":
                title = "Output";
                break;
            case "OutputNProvinsiSmart":
                title = "Output dan Provinsi";
                break;
            case "OutputNProvinsiMpo":
                title = "Output dan Provinsi";
                break;
        }

        Intent i = new Intent(m_Ctx, RekesikActivity.class);
        i.putExtra("title", title);
        i.putExtra("report_name", tag);
        startActivity(i);
    }
}
