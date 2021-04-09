package com.cojigae.coji.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.item.SettingItem;

import java.util.ArrayList;

public class OpenLicenseRecyclerViewAdapter extends RecyclerView.Adapter<OpenLicenseRecyclerViewAdapter.MyViewHolder>{

    public ArrayList<SettingItem> items = new ArrayList<>();

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_subtitle;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_license_title);
            tv_subtitle = itemView.findViewById(R.id.tv_license_subtitle);
        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_license, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_title.setText(items.get(position).getTitle());
        holder.tv_subtitle.setText(items.get(position).getSubtitle());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
