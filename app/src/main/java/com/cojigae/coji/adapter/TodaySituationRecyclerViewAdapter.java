package com.cojigae.coji.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.item.TodaySituationItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TodaySituationRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<TodaySituationItem> items = new ArrayList<TodaySituationItem>();
    Context mContext;
    DecimalFormat df = new DecimalFormat("###,###");

    public TodaySituationRecyclerViewAdapter(Context context){
        mContext = context;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cv;
        TextView tv_type;
        TextView tv_value;
        TextView tv_difference;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cv = itemView.findViewById(R.id.cv_item);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_difference = itemView.findViewById(R.id.tv_difference);
        }
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_current_situation, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder holder = (MyViewHolder)viewHolder;

        String type = items.get(position).getType();
        int value = items.get(position).getValue();
        int difference = items.get(position).getDifference();

        holder.tv_type.setText(type);
        holder.tv_value.setText(df.format(value));

        setDifference(holder, difference);
        setItemBackgroundColor(holder, type);
    }

    @SuppressLint("SetTextI18n")
    private void setDifference(MyViewHolder holder, int difference) {
        if(difference > 0)
            holder.tv_difference.setText(df.format(difference) + "▲");
        else if(difference < 0)
            holder.tv_difference.setText(df.format(difference).substring(1) + "▼");
        else
            holder.tv_difference.setText("-");
    }

    private void setItemBackgroundColor(MyViewHolder holder, String type) {
        switch (type){
            case "확진자":
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.decide));
                break;
            case "검사중":
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.exam));
                break;
            case "격리해제":
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.clear));
                break;
            case "사망자":
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.death));
                break;
            default:
                holder.cv.setCardBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
