package com.emansj.mpogo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.provider.Contacts;
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

public class DialogRekeDetail extends DialogFragment {

    //Standard vars
    private static final String TAG = RekesikFragment.class.getSimpleName();
    private Context m_Ctx;
    private View parent_view;

    //Custom vars
    private RealisasiKeuangan m_Data;


    public DialogRekeDetail() {
        // Required empty public constructor
    }

    public void setData(RealisasiKeuangan obj) {
        this.m_Data = obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.dialog_realisasi_keuangan, container, false);
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

        ((TextView) parent_view.findViewById(R.id.tvPaguLong)).setText(String.format("%,.2f", m_Data.pagu));
        ((TextView) parent_view.findViewById(R.id.tvSmartLong)).setText(String.format("%,.2f", m_Data.smartValue));
        ((TextView) parent_view.findViewById(R.id.tvMpoLong)).setText(String.format("%,.2f", m_Data.mpoValue));
    }
}
