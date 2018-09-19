package com.emansj.mpogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.model.RealisasiKeuangan;

import java.util.ArrayList;
import java.util.List;

public class AdapterRkKegiatan extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RealisasiKeuangan> items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view, RealisasiKeuangan obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.m_OnItemClickListener = mItemClickListener;
    }

    public AdapterRkKegiatan(Context context, List<RealisasiKeuangan> items){
        this.items = items;
        m_Ctx = context;
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

        public OriginalViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyParent);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvPagu = v.findViewById(R.id.tvPagu);
            tvSmartValue = v.findViewById(R.id.tvSmartValue);
            tvSmartPercent = v.findViewById(R.id.tvSmartPercent);
            tvMpoValue = v.findViewById(R.id.tvMpoValue);
            tvMpoPercent = v.findViewById(R.id.tvMpoPercent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan, parent,false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            RealisasiKeuangan i = items.get(position);

            view.tvTitle.setText(i.title);
            view.tvPagu.setText(String.format("%,.2f", i.pagu));
            view.tvSmartValue.setText(String.format("%,.2f", i.smartValue));
            view.tvSmartPercent.setText(String.format("%,.2f", i.smartPercent) + "%");
            view.tvMpoValue.setText(String.format("%,.2f", i.mpoValue));
            view.tvMpoPercent.setText(String.format("%,.2f", i.mpoPercent) + "%");

            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

