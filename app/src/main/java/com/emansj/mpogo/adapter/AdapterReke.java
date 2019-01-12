package com.emansj.mpogo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.ToolAnimation;
import com.emansj.mpogo.model.RealisasiKeuangan;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class AdapterReke extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_DEFAULT = 0;
    private final int VIEW_HEADER = 1;
    private final int VIEW_DETAIL = 2;

    private List<RealisasiKeuangan> m_Items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view, RealisasiKeuangan obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.m_OnItemClickListener = mItemClickListener;
    }

    public AdapterReke(Context context, List<RealisasiKeuangan> items){
        this.m_Items = items;
        this.m_Ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public View lyExpand;
        public TextView tvTitle;
        public TextView tvTitleInit;
        public TextView tvSubTitle;
        public TextView tvPagu;
        public TextView tvSmartValue;
        public TextView tvSmartPercent;
        public ImageView imvSmartIcon;
        public TextView tvMpoValue;
        public TextView tvMpoPercent;
        public ImageView imvMpoIcon;
        public ImageButton imbExpand;

        public OriginalViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            lyExpand = v.findViewById(R.id.lyExpand);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvTitleInit = v.findViewById(R.id.tvTitleInit);
            tvPagu = v.findViewById(R.id.tvPagu);
            tvSmartValue = v.findViewById(R.id.tvSmartValue);
            tvSmartPercent = v.findViewById(R.id.tvSmartPercent);
            imvSmartIcon = v.findViewById(R.id.imvSmartIcon);
            tvMpoValue = v.findViewById(R.id.tvMpoValue);
            tvMpoPercent = v.findViewById(R.id.tvMpoPercent);
            imvMpoIcon = v.findViewById(R.id.imvMpoIcon);
            imbExpand = v.findViewById(R.id.imbExpand);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (viewType == VIEW_DEFAULT) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan, parent, false);
            vh = new OriginalViewHolder(v);

        }else if (viewType == VIEW_DETAIL) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_prov_detail, parent, false);
            vh = new OriginalViewHolder(v);

        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_prov, parent, false);
            vh = new OriginalViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final RealisasiKeuangan i = m_Items.get(position);

            view.tvTitle.setText(i.title);
            if (i.tag != null) {
                if (i.tag.equals("detail")) {
                    view.tvTitleInit.setText(String.valueOf(i.title.charAt(0)).toUpperCase());
                }
            }
            view.tvPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.pagu)));
            view.tvSmartValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.smartValue)));
            view.tvSmartPercent.setText(String.format("%,.2f", i.smartPercent) + "%");
            view.tvMpoValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.mpoValue)));
            view.tvMpoPercent.setText(String.format("%,.2f", i.mpoPercent) + "%");

            //icon
            double smartPercent = i.smartPercent;
            double mpoPercent = i.mpoPercent;
            int colorMatch = ContextCompat.getColor(m_Ctx, R.color.colorPrimary);
            int colorUp = ContextCompat.getColor(m_Ctx, R.color.grey_80);
            int colorDown = ContextCompat.getColor(m_Ctx, R.color.grey_20);
            if (smartPercent == mpoPercent){
                //smart
                Tools.displayImageOriginal(m_Ctx, view.imvSmartIcon, R.drawable.ic_outline_check);
                ImageViewCompat.setImageTintList(view.imvSmartIcon, ColorStateList.valueOf(colorMatch));
                //mpo
                Tools.displayImageOriginal(m_Ctx, view.imvMpoIcon, R.drawable.ic_outline_check);
                ImageViewCompat.setImageTintList(view.imvMpoIcon, ColorStateList.valueOf(colorMatch));

            }else if (smartPercent > mpoPercent){
                //smart
                Tools.displayImageOriginal(m_Ctx, view.imvSmartIcon, R.drawable.ic_outline_arrow_upward);
                ImageViewCompat.setImageTintList(view.imvSmartIcon, ColorStateList.valueOf(colorUp));
                //mpo
                Tools.displayImageOriginal(m_Ctx, view.imvMpoIcon, R.drawable.ic_outline_arrow_downward);
                ImageViewCompat.setImageTintList(view.imvMpoIcon, ColorStateList.valueOf(colorDown));

            }else if (smartPercent < mpoPercent){
                //smart
                Tools.displayImageOriginal(m_Ctx, view.imvSmartIcon, R.drawable.ic_outline_arrow_downward);
                ImageViewCompat.setImageTintList(view.imvSmartIcon, ColorStateList.valueOf(colorDown));
                //mpo
                Tools.displayImageOriginal(m_Ctx, view.imvMpoIcon, R.drawable.ic_outline_arrow_upward);
                ImageViewCompat.setImageTintList(view.imvMpoIcon, ColorStateList.valueOf(colorUp));
            }

            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, m_Items.get(position), position);
                    }
                }
            });

            if (i.tag != null) {
                if (!i.tag.equals("detail")) {
                    view.imbExpand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            boolean show = toggleLayoutExpand(!i.expanded, v, view.lyExpand);
                            m_Items.get(position).expanded = show;
                        }
                    });

                    // void recycling view
                    if (i.expanded) {
                        view.lyExpand.setVisibility(View.VISIBLE);
                    } else {
                        view.lyExpand.setVisibility(View.GONE);
                    }
                    Tools.toggleArrow(i.expanded, view.imbExpand, false);
                }
            }

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View ly_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ToolAnimation.expand(ly_expand);
        } else {
            ToolAnimation.collapse(ly_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return m_Items.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;

        if (m_Items.get(position).tag == null){
            viewType = 0; //default

        }else if (m_Items.get(position).tag.equals("detail")) {
            viewType = 2; //detail

        }else {
            viewType = 1; //detail

        }

        return viewType;
    }
}
