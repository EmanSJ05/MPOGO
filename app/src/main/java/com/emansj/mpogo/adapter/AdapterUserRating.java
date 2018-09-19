package com.emansj.mpogo.adapter;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.UserRating;

import java.util.ArrayList;
import java.util.List;

public class AdapterUserRating extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserRating> m_Items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view, UserRating obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.m_OnItemClickListener = mItemClickListener;
    }

    public AdapterUserRating(Context context, List<UserRating> items){
        this.m_Items = items;
        this.m_Ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public TextView tvTitle;
        public TextView tvSubTitle;
        public TextView tvCaption;
        public TextView tvTotalPoin;
        public ImageView imvImage;

        public OriginalViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSubTitle = v.findViewById(R.id.tvSubtitle);
            tvCaption = v.findViewById(R.id.tvCaption);
            tvTotalPoin = v.findViewById(R.id.tvTotalPoin);
            imvImage = v.findViewById(R.id.imvImage);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rating, parent,false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        String caption;

        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            UserRating i = m_Items.get(position);

            view.tvTitle.setText(Tools.initCaps(i.name));
            view.tvSubTitle.setText(Tools.initCaps(i.subTitle));
            if (!i.caption.equals("-")){
                caption = Tools.initCaps(i.groupName) + " | " + Tools.initCaps(i.caption);
            }else{
                caption = Tools.initCaps(i.groupName);
            }
            view.tvCaption.setText(caption);
            view.tvTotalPoin.setText(i.totalPoin.toString());

            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, m_Items.get(position), position);
                    }
                }
            });

            displayImage(view, i);

        }
    }

    private void displayImage(OriginalViewHolder holder, UserRating item) {
        if (item.imageDrawableResource != null) {
            Tools.displayImageRound(m_Ctx, holder.imvImage, item.imageDrawableResource);
            holder.imvImage.setColorFilter(null);
        } else {
            @DrawableRes int drawableRes = Tools.getDrawableResource(m_Ctx, "photo_user_no_image");
            Tools.displayImageRound(m_Ctx, holder.imvImage, drawableRes);
            //holder.imvImage.setImageResource(R.drawable.photo_user_no_image);
            holder.imvImage.setColorFilter(null);
        }
    }

    @Override
    public int getItemCount() {
        return m_Items.size();
    }
}

