package com.github.recycleritemdecoration;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @data 2018-09-26
 * @desc
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {

    private ArrayList<ContactInfo> mInfoList;

    public ContactsAdapter(ArrayList<ContactInfo> infoList) {
        mInfoList = infoList;
    }

    @Override
    public ContactsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rlv_contacts, parent, false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactsViewHolder holder, int position) {
        ContactInfo contactInfo = mInfoList.get(position);
        holder.tvName.setText(contactInfo.getName());
        holder.tvNumber.setText(contactInfo.getPhone());
    }

    @Override
    public int getItemCount() {
        return mInfoList != null ? mInfoList.size() : 0;
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder{
        TextView tvName;
        TextView tvNumber;
        public ContactsViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
        }
    }

    public void setNewData(ArrayList<ContactInfo> infoList){
        this.mInfoList = infoList;
        notifyDataSetChanged();
    }

    public ArrayList<ContactInfo> getData(){
        return mInfoList;
    }

}
