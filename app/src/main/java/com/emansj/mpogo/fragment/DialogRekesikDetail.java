package com.emansj.mpogo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.activity.RekesikFragment;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.RealisasiKeuanganFisik;

public class DialogRekesikDetail extends DialogFragment {

    //Standard vars
    private static final String TAG = RekesikFragment.class.getSimpleName();
    private Context m_Ctx;
    private View parent_view;

    //Custom vars
    private RealisasiKeuanganFisik m_Data;


    public DialogRekesikDetail() {
        // Required empty public constructor
    }

    public void setData(RealisasiKeuanganFisik obj) {
        this.m_Data = obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.dialog_realisasi_keuangan_fisik, container, false);
        initListener();
        initComponent();

        return parent_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initListener() {

        //Frame clicked
        ((ViewGroup) parent_view.findViewById(R.id.lyFrame)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        //Button clicked
        ((FloatingActionButton) parent_view.findViewById(R.id.fabClose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initComponent() {
        ((TextView) parent_view.findViewById(R.id.tvTitle)).setText(m_Data.title);

        ((TextView) parent_view.findViewById(R.id.tvPagu)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", m_Data.pagu)));
        ((TextView) parent_view.findViewById(R.id.tvSmartValue)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", m_Data.smartValue)));
        ((TextView) parent_view.findViewById(R.id.tvSmartPercent)).setText(String.format("%,.2f", m_Data.smartPercent) + "%");
        ((TextView) parent_view.findViewById(R.id.tvMpoValue)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", m_Data.mpoValue)));
        ((TextView) parent_view.findViewById(R.id.tvMpoPercent)).setText(String.format("%,.2f", m_Data.mpoPercent) + "%");

        ((TextView) parent_view.findViewById(R.id.tvFisikTarget)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", m_Data.fisikTarget)));
        ((TextView) parent_view.findViewById(R.id.tvFisikValue)).setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", m_Data.fisikValue)));
        ((TextView) parent_view.findViewById(R.id.tvFisikValuePercent)).setText(String.format("%,.2f", m_Data.fisikPercent) + "%");

        //Long
        ((TextView) parent_view.findViewById(R.id.tvPaguLong)).setText(String.format("%,.2f", m_Data.pagu));
        ((TextView) parent_view.findViewById(R.id.tvSmartLong)).setText(String.format("%,.2f", m_Data.smartValue));
        ((TextView) parent_view.findViewById(R.id.tvMpoLong)).setText(String.format("%,.2f", m_Data.mpoValue));
        ((TextView) parent_view.findViewById(R.id.tvFisikTargetLong)).setText(String.format("%,.2f", m_Data.fisikTarget));
        ((TextView) parent_view.findViewById(R.id.tvFisikValueLong)).setText(String.format("%,.2f", m_Data.fisikValue));
    }
}
