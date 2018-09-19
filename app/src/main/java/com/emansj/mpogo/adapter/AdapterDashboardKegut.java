package com.emansj.mpogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.model.DashboardKegut;

import java.util.ArrayList;
import java.util.List;

public class AdapterDashboardKegut extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DashboardKegut> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener{
        void onItemClick(View view, DashboardKegut obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterDashboardKegut(Context context, List<DashboardKegut> items){
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;
        public TextView realPercent;
        public ProgressBar progress;
        public View lyParent;

        public OriginalViewHolder(View v) {
            super(v);
            categoryName = v.findViewById(R.id.tvTitle);
            realPercent = v.findViewById(R.id.tvRealisasi);
            progress = v.findViewById(R.id.pbProgress);
            lyParent = v.findViewById(R.id.lyParent);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_kegut, parent,false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            DashboardKegut p = items.get(position);
            view.categoryName.setText(p.categoryName);
            view.realPercent.setText(p.realPercent + "%") ;
            int percentValue = (int)Double.parseDouble(p.realPercent);
            view.progress.setProgress(percentValue);

            view.lyParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
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

