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


public class RatingFragment extends Fragment {

    //Standard vars
    private static final String TAG = RekeFragment.class.getSimpleName();
    private Context m_Ctx;
    private View parent_view;
    private View.OnClickListener m_OnClickListener;
    
    public RatingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.fragment_rating, container, false);
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
        //per-provinsi
        CardView cvProvinsi = parent_view.findViewById(R.id.cvProvinsi);
        cvProvinsi.setOnClickListener(m_OnClickListener);
        View lyProvinsi = parent_view.findViewById(R.id.lyProvinsi);
        lyProvinsi.setOnClickListener(m_OnClickListener);
        TextView tvProvinsi = parent_view.findViewById(R.id.tvProvinsi);
        tvProvinsi.setOnClickListener(m_OnClickListener);
        ImageView imvProvinsi = parent_view.findViewById(R.id.imvProvinsi);
        imvProvinsi.setOnClickListener(m_OnClickListener);

        //per-kabupaten
        CardView cvKabupaten = parent_view.findViewById(R.id.cvKabupaten);
        cvKabupaten.setOnClickListener(m_OnClickListener);
        View lyKabupaten = parent_view.findViewById(R.id.lyKabupaten);
        lyKabupaten.setOnClickListener(m_OnClickListener);
        TextView tvKabupaten = parent_view.findViewById(R.id.tvKabupaten);
        tvKabupaten.setOnClickListener(m_OnClickListener);
        ImageView imvKabupaten = parent_view.findViewById(R.id.imvKabupaten);
        imvKabupaten.setOnClickListener(m_OnClickListener);

        //all user
        CardView cvUser = parent_view.findViewById(R.id.cvUser);
        cvUser.setOnClickListener(m_OnClickListener);
        View lyUser = parent_view.findViewById(R.id.lyUser);
        lyUser.setOnClickListener(m_OnClickListener);
        TextView tvUser = parent_view.findViewById(R.id.tvUser);
        tvUser.setOnClickListener(m_OnClickListener);
        ImageView imvUser = parent_view.findViewById(R.id.imvUser);
        imvUser.setOnClickListener(m_OnClickListener);

        
    }

    private void initComponent() {

    }

    public void gotoActivity(View v)
    {
        String title = "Report Title";

        String tag = v.getTag().toString();
        switch(tag) {
            case "Provinsi":
                title = "Per-Provinsi";
                break;
            case "Kabupaten":
                title = "Per-Kabupaten";
                break;
            case "User":
                title = "All User";
                break;
        }

        Intent i = new Intent(m_Ctx, RatingActivity.class);
        i.putExtra("title", title);
        i.putExtra("report_name", tag);
        startActivity(i);
    }
    
}
