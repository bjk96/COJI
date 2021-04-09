package com.cojigae.coji.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.item.WorldSituationItem;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class WorldSituationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public ArrayList<WorldSituationItem> items = new ArrayList<>();
    public ArrayList<WorldSituationItem> items2 = new ArrayList<>();

    DecimalFormat df = new DecimalFormat("###,###");

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_nationNm;
        TextView tv_natDefCnt;
        TextView tv_natDeathCnt;
        TextView incdef;
        TextView incdet;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_nationNm = itemView.findViewById(R.id.tv_nationNm);
            tv_natDefCnt = itemView.findViewById(R.id.tv_natDefCnt);
            tv_natDeathCnt = itemView.findViewById(R.id.tv_natDeathCnt);
            incdef = itemView.findViewById(R.id.incdef);
            incdet = itemView.findViewById(R.id.incdet);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("Main", "onCreateViewHolder");
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_world_situation, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.i("Main", "position=" + position);
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        String nationNm = items.get(position).getNationNm();
        int natDefCnt = Integer.parseInt(items.get(position).getNatDefCnt());
        int natDeathCnt = Integer.parseInt(items.get(position).getNatDeathCnt());

        int definc = 0;
        int detinc = 0;

        if(items.get(position).getNationNm().equals(items2.get(position).getNationNm())) {
            definc = natDefCnt - Integer.parseInt(items2.get(position).getNatDefCnt());
            detinc = natDeathCnt - Integer.parseInt(items2.get(position).getNatDeathCnt());
        } else if(items.get(position).getNationNm().equals(items2.get(position+(items.size()-items2.size())).getNationNm())){
            definc = natDefCnt - Integer.parseInt(items2.get(position+(items.size()-items2.size())).getNatDefCnt());
            detinc = natDeathCnt - Integer.parseInt(items2.get(position+(items.size()-items2.size())).getNatDeathCnt());
        }

        myViewHolder.tv_nationNm.setText(nationNm);
        myViewHolder.tv_natDefCnt.setText(df.format(natDefCnt));
        myViewHolder.tv_natDeathCnt.setText(df.format(natDeathCnt));
        myViewHolder.incdef.setText(df.format(definc));
        myViewHolder.incdet.setText(df.format(detinc));

        setIncDec(myViewHolder, definc);
        setDeathDec(myViewHolder, detinc);
    }
    private void setIncDec(WorldSituationAdapter.MyViewHolder holder, int definc) {
        if(definc > 0)
            holder.incdef.setText("▲ " + df.format(definc));
        else
            holder.incdef.setText("-");
    }
    private void setDeathDec(WorldSituationAdapter.MyViewHolder holder, int detinc) {
        if(detinc > 0)
            holder.incdet.setText("▲ " + df.format(detinc));
        else
            holder.incdet.setText("-");
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

}

