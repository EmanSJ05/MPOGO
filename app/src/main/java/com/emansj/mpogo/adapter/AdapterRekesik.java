package com.emansj.mpogo.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.ViewAnimation;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.RealisasiKeuanganFisik;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.ArrayList;
import java.util.List;

public class AdapterRekesik extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_DEFAULT = 0;
    private final int VIEW_HEADER = 1;
    private final int VIEW_DETAIL = 2;

    private List<RealisasiKeuanganFisik> m_Items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;
    public enum HolderMode {DEFAULT, KEGIATAN};
    private HolderMode m_HolderMode;


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

    public void setHolderMode(HolderMode holderMode){
        this.m_HolderMode = holderMode;
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
        public TextView tvFisikTarget;
        public TextView tvFisikValue;
        public TextView tvFisikPercent;
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
            tvFisikTarget = v.findViewById(R.id.tvFisikTarget);
            tvFisikValue = v.findViewById(R.id.tvFisikValue);
            tvFisikPercent = v.findViewById(R.id.tvFisikPercent);
            imbExpand = v.findViewById(R.id.imbExpand);
        }
    }

    public class KegiatanViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public TextView tvTitle;
        public TextView tvSubTitle;
        public TextView tvPagu;
        public TextView tvSmartValue;
        public TextView tvSmartPercent;
        public ImageView imvSmartIcon;
        public TextView tvMpoValue;
        public TextView tvMpoPercent;
        public ImageView imvMpoIcon;
        public TextView tvFisikProgress;
        public TextView tvFisikProgressTerbobot;

        public KegiatanViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvPagu = v.findViewById(R.id.tvPagu);
            tvSmartValue = v.findViewById(R.id.tvSmartValue);
            tvSmartPercent = v.findViewById(R.id.tvSmartPercent);
            imvSmartIcon = v.findViewById(R.id.imvSmartIcon);
            tvMpoValue = v.findViewById(R.id.tvMpoValue);
            tvMpoPercent = v.findViewById(R.id.tvMpoPercent);
            imvMpoIcon = v.findViewById(R.id.imvMpoIcon);
            tvFisikProgress = v.findViewById(R.id.tvFisikProgress);
            tvFisikProgressTerbobot = v.findViewById(R.id.tvFisikProgressTerbobot);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (m_HolderMode == HolderMode.KEGIATAN) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_fisik_kegiatan, parent, false);
            vh = new KegiatanViewHolder(v);

        }else {
            if (viewType == VIEW_DEFAULT) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_fisik, parent, false);
                vh = new OriginalViewHolder(v);

            }else if (viewType == VIEW_DETAIL) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_fisik_prov_detail, parent, false);
                vh = new OriginalViewHolder(v);

            }else {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realisasi_keuangan_fisik_prov, parent, false);
                vh = new OriginalViewHolder(v);
            }
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final RealisasiKeuanganFisik i = m_Items.get(position);

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
            view.tvFisikTarget.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.fisikTarget)));
            view.tvFisikValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.fisikValue)));
            view.tvFisikPercent.setText(String.format("%,.2f", i.fisikPercent) + "%");

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

        }else if (holder instanceof KegiatanViewHolder) {
            KegiatanViewHolder view = (KegiatanViewHolder) holder;

            RealisasiKeuanganFisik i = m_Items.get(position);

            view.tvTitle.setText(i.title);
            view.tvPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.pagu)));
            view.tvSmartValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.smartValue)));
            view.tvSmartPercent.setText(String.format("%,.2f", i.smartPercent) + "%");
            view.tvMpoValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.mpoValue)));
            view.tvMpoPercent.setText(String.format("%,.2f", i.mpoPercent) + "%");
            view.tvFisikProgress.setText(String.format("%,.2f", i.fisikProgress) + "%");
            view.tvFisikProgressTerbobot.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f",i.fisikProgressTerbobot)));

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

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View ly_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(ly_expand);
        } else {
            ViewAnimation.collapse(ly_expand);
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

