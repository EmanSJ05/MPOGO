package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterDashboardKegut;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.model.DashboardDonut;
import com.emansj.mpogo.model.DashboardKegut;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;


public class HomeFragment extends Fragment {

    private Context m_Ctx;

    private View parent_view;
    private RecyclerView rvSMART;
    private RecyclerView rvMPO;
    private AdapterDashboardKegut m_Adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponent();

        return parent_view;
    }

    private void initComponent() {
        showPie();
        showKegutSmart();
        showKegutMpo();
    }

    private void showPie() {
        DashboardDonut i = new DashboardDonut();
        i.getDashboard(m_Ctx);
        
        ((TextView) parent_view.findViewById(R.id.tvSmartPagu)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", i.pagu)));
        ((DonutProgress) parent_view.findViewById(R.id.dopSmartPercent)).setProgress(Long.parseLong(String.format("%.0f", i.smartPercent)));
        ((TextView) parent_view.findViewById(R.id.tvSmartRealisasi)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", i.smartRealisasi)));
        ((TextView) parent_view.findViewById(R.id.tvSmartSisa)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", i.smartSisa)));

        ((TextView) parent_view.findViewById(R.id.tvMpoPagu)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", i.pagu)));
        ((DonutProgress) parent_view.findViewById(R.id.dopMpoPercent)).setProgress(Long.parseLong(String.format("%.0f", i.mpoPercent)));
        ((TextView) parent_view.findViewById(R.id.tvMpoRealisasi)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", i.smartRealisasi)));
        ((TextView) parent_view.findViewById(R.id.tvMpoSisa)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", i.smartSisa)));

        ((TextView) parent_view.findViewById(R.id.tvPieSmartUpdate)).setText(Tools.convertDateDTS(i.smartLastUpdate, "d MMM yyyy"));
        ((TextView) parent_view.findViewById(R.id.tvPieMpoUpdate)).setText(Tools.convertDateDTS(i.mpoLastUpdate, "d MMM yyyy"));
        ((TextView) parent_view.findViewById(R.id.tvSmartUpdate)).setText(Tools.convertDateDTS(i.smartLastUpdate, "d MMM yyyy"));
        ((TextView) parent_view.findViewById(R.id.tvMpoUpdate)).setText(Tools.convertDateDTS(i.mpoLastUpdate, "d MMM yyyy"));
    }

    private void showKegutSmart() {
        rvSMART = parent_view.findViewById(R.id.rvKegutSMART);
        rvSMART.setLayoutManager(new LinearLayoutManager(m_Ctx));
        //rvSMART.setHasFixedSize(true);
        rvSMART.setNestedScrollingEnabled(false);

        List<DashboardKegut> items = DashboardKegut.getDashboardKegutSmart(m_Ctx);

        m_Adapter = new AdapterDashboardKegut(m_Ctx, items);
        rvSMART.setAdapter(m_Adapter);

        m_Adapter.setOnItemClickListener(new AdapterDashboardKegut.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DashboardKegut obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.categoryName + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void showKegutMpo() {
        rvMPO = parent_view.findViewById(R.id.rvKegutMPO);
        rvMPO.setLayoutManager(new LinearLayoutManager(m_Ctx));
        //rvMPO.setHasFixedSize(true);
        rvMPO.setNestedScrollingEnabled(false);

        List<DashboardKegut> items = DashboardKegut.getDashboardKegutMpo(m_Ctx);

        m_Adapter = new AdapterDashboardKegut(m_Ctx, items);
        rvMPO.setAdapter(m_Adapter);

        m_Adapter.setOnItemClickListener(new AdapterDashboardKegut.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DashboardKegut obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.categoryName + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}

