package com.emansj.mpogo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.RealisasiKeuanganFisik;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

public class AdapterRekesik extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RealisasiKeuanganFisik> m_Items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view, RealisasiKeuanganFisik obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.m_OnItemClickListener = mItemClickListener;
    }

    public AdapterRekesik(Context context, List<RealisasiKeuanganFisik> items){
        this.m_Items = items;
        this.m_Ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public TextView tvTitle;
        public TextView tvSubTitle;
        public TextView tvPagu;
        public TextView tvSmartValue;
        public TextView tvSmartPercent;
        public TextView tvMpoValue;
        public TextView tvMpoPercent;
        public TextView tvFisikTarget;
        public TextView tvFisikValue;
        public TextView tvFisikPercent;

        public OriginalViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvPagu = v.findViewById(R.id.tvPagu);
            tvSmartValue = v.findViewById(R.id.tvSmartValue);
            tvSmartPercent = v.findViewById(R.id.tvSmartPercent);
            tvMpoValue = v.findViewById(R.id.tvMpoValue);
            tvMpoPercent = v.findViewById(R.id.tvMpoPercent);
            tvFisikTarget = v.findViewById(R.id.tvFisikTarget);
            tvFisikValue = v.findViewById(R.id.tvFisikValue);
            tvFisikPercent = v.findViewById(R.id.tvFisikPercent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_fisik, parent,false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            RealisasiKeuanganFisik i = m_Items.get(position);

            view.tvTitle.setText(i.title);
            view.tvPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.pagu)));
            view.tvSmartValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.smartValue)));
            view.tvSmartPercent.setText(String.format("%,.2f", i.smartPercent) + "%");
            view.tvMpoValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.mpoValue)));
            view.tvMpoPercent.setText(String.format("%,.2f", i.mpoPercent) + "%");
            view.tvFisikTarget.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.fisikTarget)));
            view.tvFisikValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.fisikValue)));
            view.tvFisikPercent.setText(String.format("%,.2f", i.fisikPercent) + "%");

            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, m_Items.get(position), position);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return m_Items.size();
    }
}

