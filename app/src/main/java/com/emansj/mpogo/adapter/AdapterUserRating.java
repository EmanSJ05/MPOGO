package com.emansj.mpogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.model.UserRating;

import java.util.ArrayList;
import java.util.List;

public class AdapterUserRating extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<UserRating> m_Items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;
    public enum HolderMode {PROVINSI, KABUPATEN, ALLUSER};
    private HolderMode m_HolderMode;


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

    public void setHolderMode(HolderMode holderMode){
        this.m_HolderMode = holderMode;
    }

    public class ProvinsiViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public TextView tvTitle, tvTotalPoin, tvTotalUser, tvTotalAktifitas ;

        public ProvinsiViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvTotalPoin = v.findViewById(R.id.tvTotalPoin);
            tvTotalUser = v.findViewById(R.id.tvTotalUser);
            tvTotalAktifitas = v.findViewById(R.id.tvTotalAktifitas);
        }
    }

    public class KabupatenViewHolder extends RecyclerView.ViewHolder {
        public View lyRow, lySection;
        public TextView tvTitle, tvSection, tvTotalPoin, tvTotalUser, tvTotalAktifitas ;

        public KabupatenViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            lySection = v.findViewById(R.id.lySection);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSection = v.findViewById(R.id.tvSection);
            tvTotalPoin = v.findViewById(R.id.tvTotalPoin);
            tvTotalUser = v.findViewById(R.id.tvTotalUser);
            tvTotalAktifitas = v.findViewById(R.id.tvTotalAktifitas);
        }
    }

    public class AllUserViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public TextView tvTitle, tvSubtitle, tvCaption, tvTotalPoin, tvTotalAktifitas ;
        public ImageView imvImage;

        public AllUserViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSubtitle = v.findViewById(R.id.tvSubtitle);
            tvCaption = v.findViewById(R.id.tvCaption);
            tvTotalPoin = v.findViewById(R.id.tvTotalPoin);
            tvTotalAktifitas = v.findViewById(R.id.tvTotalAktifitas);
            imvImage = v.findViewById(R.id.imvImage);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;

        if (m_HolderMode == HolderMode.PROVINSI) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rating_provinsi, parent, false);
            vh = new ProvinsiViewHolder(v);

        }else if (m_HolderMode == HolderMode.KABUPATEN) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rating_kabupaten, parent, false);
            vh = new KabupatenViewHolder(v);

        }else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_rating_semua_user, parent, false);
            vh = new AllUserViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ProvinsiViewHolder) {
            ProvinsiViewHolder view = (ProvinsiViewHolder) holder;

            UserRating i = m_Items.get(position);
            view.tvTitle.setText(i.name);
            view.tvTotalPoin.setText(i.totalPoin.toString());
            view.tvTotalUser.setText(i.totalUser.toString());
            view.tvTotalAktifitas.setText(i.totalAktifitas.toString());
            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, m_Items.get(position), position);
                    }
                }
            });

        }else if (holder instanceof KabupatenViewHolder) {
            KabupatenViewHolder view = (KabupatenViewHolder) holder;

            UserRating i = m_Items.get(position);
            if (position > 0 && i.groupName.equals(m_Items.get(position-1).groupName)) {
                view.lySection.setVisibility(View.GONE);
            } else {
                view.lySection.setVisibility(View.VISIBLE);
            }
            view.tvSection.setText(i.groupName);
            view.tvTitle.setText(i.name);
            view.tvTotalPoin.setText(i.totalPoin.toString());
            view.tvTotalUser.setText(i.totalUser.toString());
            view.tvTotalAktifitas.setText(i.totalAktifitas.toString());
            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, m_Items.get(position), position);
                    }
                }
            });

        }else { //AllUserViewHolder)
            AllUserViewHolder view = (AllUserViewHolder) holder;

            UserRating i = m_Items.get(position);
            view.tvTitle.setText(i.name);
            view.tvSubtitle.setText(i.subTitle);
            view.tvCaption.setText(i.caption);
            view.tvTotalPoin.setText(i.totalPoin.toString());
            view.tvTotalAktifitas.setText(i.totalAktifitas.toString());
            view.lyRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (m_OnItemClickListener != null) {
                        m_OnItemClickListener.onItemClick(view, m_Items.get(position), position);
                    }
                }
            });

            if (i.imageUrl != null) {
                Tools.displayImageRound(m_Ctx, view.imvImage, i.imageUrl, 36, 36);
            }
        }
    }

//    private void displayImage(AllUserViewHolder holder, UserRating item) {
////        if (item.imageDrawableResource != null) {
////            Tools.displayImageRound(m_Ctx, holder.imvImage, item.imageDrawableResource);
////            holder.imvImage.setColorFilter(null);
////        } else {
////            @DrawableRes int drawableRes = Tools.getDrawableResource(m_Ctx, "photo_user_no_image");
////            Tools.displayImageRound(m_Ctx, holder.imvImage, drawableRes);
////            //holder.imvImage.setImageResource(R.drawable.photo_user_no_image);
////            holder.imvImage.setColorFilter(null);
////        }
//
//        if (item.imageDrawableResource != null) {
//            Tools.displayImageRound(m_Ctx, holder.imvImage, item.imageDrawableResource);
//            holder.imvImage.setColorFilter(null);
//        } else {
//            @DrawableRes int drawableRes = Tools.getDrawableResource(m_Ctx, "photo_user_no_image");
//            Tools.displayImageRound(m_Ctx, holder.imvImage, drawableRes);
//            //holder.imvImage.setImageResource(R.drawable.photo_user_no_image);
//            holder.imvImage.setColorFilter(null);
//
//        }
//    }

    @Override
    public int getItemCount() {
        return m_Items.size();
    }

}

