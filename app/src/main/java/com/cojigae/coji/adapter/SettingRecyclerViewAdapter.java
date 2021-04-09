package com.cojigae.coji.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.OpenLicenseActivity;
import com.cojigae.coji.R;
import com.cojigae.coji.item.SettingItem;

import java.util.ArrayList;

public class SettingRecyclerViewAdapter extends RecyclerView.Adapter<SettingRecyclerViewAdapter.MyViewHolder> {

    public ArrayList<SettingItem> settingItems = new ArrayList<>();
    Context mContext;

    public SettingRecyclerViewAdapter(Context context){
        mContext = context;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_subtitle;
        Switch switchOnOff;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_title = itemView.findViewById(R.id.tv_settingTitle);
            tv_subtitle = itemView.findViewById(R.id.tv_settingSubtitle);
            switchOnOff = itemView.findViewById(R.id.switchOnOff);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_settings, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("pref", Activity.MODE_PRIVATE);

        holder.tv_title.setText(settingItems.get(position).getTitle());

        if(settingItems.get(position).getSubtitle().length() <= 0)
            holder.tv_subtitle.setVisibility(View.GONE);
        else {
            holder.tv_subtitle.setVisibility(View.VISIBLE);
            holder.tv_subtitle.setText(settingItems.get(position).getSubtitle());
        }

        if(settingItems.get(position).getSwitchExist()) {
            holder.switchOnOff.setVisibility(View.VISIBLE);

            if(position == 0) {
                holder.switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isSwitchOn", isChecked);
                    editor.apply();
                });

                if(sharedPreferences != null && sharedPreferences.contains("isSwitchOn")) {
                    boolean isSwitchOn = sharedPreferences.getBoolean("isSwitchOn", false);
                    holder.switchOnOff.setChecked(isSwitchOn);
                }
            }
        } else
            holder.switchOnOff.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(v -> {
            switch (position){
                case 0:
                    holder.switchOnOff.setChecked(!holder.switchOnOff.isChecked());
                    break;
                case 1:
                    mContext.startActivity(new Intent(mContext, OpenLicenseActivity.class));
                    break;
            }
        });

    }

    @Override
    public int getItemCount() {
        return settingItems.size();
    }


}
