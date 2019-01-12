package com.emansj.mpogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.model.Notif;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.sql.Types.TIMESTAMP;

public class AdapterNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notif> m_Items = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;


    public interface OnItemClickListener{
        void onItemClick(View view, Notif obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.m_OnItemClickListener = mItemClickListener;
    }

    public AdapterNotification(Context context, List<Notif> items){
        this.m_Items = items;
        this.m_Ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View lyRow;
        public TextView tvTitle, tvSubTitle, tvDate ;
        public ImageView imvIcon;

        public OriginalViewHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvSubTitle = v.findViewById(R.id.tvSubTitle);
            tvDate = v.findViewById(R.id.tvDate);
            imvIcon = v.findViewById(R.id.imvIcon);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        RecyclerView.ViewHolder vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            Notif i = m_Items.get(position);
            view.tvTitle.setText(i.Title);
            view.tvSubTitle.setText(i.Message);

            //date
            String timeAgo="";
            if (i.ReceivedDate != null) {
                try {
                    String dateString = i.ReceivedDate;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date date = sdf.parse(dateString);
                    long receivedDate = date.getTime();
                    timeAgo = AppUtils.TimeAgo.getTimeAgo(receivedDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                    timeAgo = "";
                }
            }
            view.tvDate.setText(timeAgo);

            //icon (read or unread)
            if (i.IsRead){
                Tools.displayImageOriginal(m_Ctx, view.imvIcon, R.drawable.img_msg_read);
            } else {
                Tools.displayImageOriginal(m_Ctx, view.imvIcon, R.drawable.img_msg_unread);
            }

            //item click listener
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

