package com.emansj.mpogo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import com.emansj.mpogo.R;
import com.emansj.mpogo.model.Satker;
import java.util.ArrayList;
import java.util.List;

public class AdapterSatkerFilter extends RecyclerView.Adapter<AdapterSatkerFilter.MyHolder> implements Filterable {

    public List<Satker> m_Items = new ArrayList<>();
    public List<Satker> m_ItemsChecked = new ArrayList<>();
    public List<Satker> m_FilterList = new ArrayList<>();
    private Context m_Ctx;
    private OnItemClickListener m_OnItemClickListener;
    public CustomFilter filter;
    private boolean m_IsNotified = true;


    public interface OnItemClickListener{
        void onItemClick(View view);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener){
        this.m_OnItemClickListener = mItemClickListener;
    }

    public AdapterSatkerFilter(Context ctx, List<Satker> items) {
        this.m_Ctx = ctx;
        this.m_Items = items;
        this.m_FilterList = items;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        View lyRow;
        TextView tvKdSatker;
        TextView tvNmSatker;
        CheckBox chkSelected;

        public MyHolder(View v) {
            super(v);
            lyRow = v.findViewById(R.id.lyItem);
            tvKdSatker = v.findViewById(R.id.tvKdSatker);
            tvNmSatker = v.findViewById(R.id.tvNmSatker);
            chkSelected = v.findViewById(R.id.chkSelected);
        }
    }

    //init viewholder
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_satker_filter,parent,false);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    //bind data
    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {
        Satker i = m_Items.get(position);
        holder.tvKdSatker.setText(i.kdSatker);
        holder.tvNmSatker.setText(i.nmSatker);
        holder.chkSelected.setChecked(i.isSelected);

        //row clicked
        holder.lyRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.chkSelected.isChecked() == false) {
                    holder.chkSelected.setChecked(true);
                } else {
                    holder.chkSelected.setChecked(false);
                }
                selectItem(holder, position);

                if (m_OnItemClickListener != null) {
                    m_OnItemClickListener.onItemClick(view);
                }
            }
        });

        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectItem(holder, position);

                if (m_OnItemClickListener != null) {
                    m_OnItemClickListener.onItemClick(view);
                }
            }
        });
    }

    private void selectItem(MyHolder holder, int position){

        Satker i = m_Items.get(position);
        boolean value = holder.chkSelected.isChecked();

        i.isSelected = value;
        m_Items.get(position).isSelected = value;
        if (value){
            m_ItemsChecked.add(i);
        }else{
            m_ItemsChecked.remove(i);
        }

        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return m_Items.size();
    }

    @Override
    public long getItemId(int position) {
        Satker item = m_Items.get(position);
        return item.idSatker;
    }

    public long getSelectedItemCount(){

        if (m_ItemsChecked.size() <= 0){
            for (Satker i : m_Items) {
                if (i.isSelected){
                    m_ItemsChecked.add(i);
                }
            }
        }

        return m_ItemsChecked.size();
    }

    public List<Integer> getSelectedItem(){
        List<Integer> items = new ArrayList<>();

        if (getSelectedItemCount() > 0){
            for (Satker i : m_ItemsChecked) {
                items.add(i.idSatker);
            }
        }
        return items;
    }

    //RETURN FILTER OBJ
    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new CustomFilter(m_FilterList,this);
        }
        return filter;
    }


    public class CustomFilter extends Filter {

        AdapterSatkerFilter adapter;
        List<Satker> filterList;

        public CustomFilter(List<Satker> filterList, AdapterSatkerFilter adapter)
        {
            this.adapter = adapter;
            this.filterList = filterList;
        }

        //FILTERING OCURS
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            //CHECK CONSTRAINT VALIDITY
            if(constraint != null && constraint.length() > 0)
            {
                //CHANGE TO UPPER
                constraint = constraint.toString().toUpperCase();
                //STORE OUR FILTERED PLAYERS
                List<Satker> filteredItems = new ArrayList<>();

                for (int i=0;i<filterList.size();i++)
                {
                    //CHECK
                    if(filterList.get(i).kdSatker.toUpperCase().contains(constraint))
                    {
                        //ADD PLAYER TO FILTERED PLAYERS
                        filteredItems.add(filterList.get(i));
                    }
                }

                results.count = filteredItems.size();
                results.values = filteredItems;
            }else
            {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.m_Items = (List<Satker>) results.values;

            //REFRESH
            adapter.notifyDataSetChanged();
        }
    }
}