package com.cojigae.coji.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.item.CoronaSituationKR;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class KorSituationAdapter extends RecyclerView.Adapter<KorSituationAdapter.ViewHolder> {

    private static final int MAX_LOCAL_SIZE = 18;

    public ArrayList<CoronaSituationKR> items = new ArrayList<CoronaSituationKR>();

    DecimalFormat df = new DecimalFormat("###,###");

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView local;
        TextView total;
        TextView death;
        TextView increase;
        TextView increase2;

        public ViewHolder(@NonNull View itemview) {
            super(itemview);
            local = itemview.findViewById(R.id.local);
            total = itemview.findViewById(R.id.total);
            death = itemview.findViewById(R.id.death);
            increase = itemview.findViewById(R.id.increase);
            increase2 = itemview.findViewById(R.id.increase2);
        }
    }

    //
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_kor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        ViewHolder holder = (ViewHolder)viewholder;

        String guBun = items.get(position).getGuBun();
        int defCnt = items.get(position).getDefCnt();
        int deathCnt = items.get(position).getDeathCnt();
        int incDec = items.get(position).getIncDec();
        int deathDec = deathCnt - items.get(MAX_LOCAL_SIZE + 1 + position).getDeathCnt();

        holder.local.setText(guBun);
        holder.total.setText(df.format(defCnt));
        holder.death.setText(df.format(deathCnt));
        holder.increase.setText(df.format(incDec));
        holder.increase2.setText(df.format(deathDec));

        //  증가 표시
        setIncDec(holder, incDec);
        setDeathDec(holder, deathDec);
    }
    private void setIncDec(ViewHolder holder, int incDec) {
        if(incDec > 0)
            holder.increase.setText("▲ " + df.format(incDec));
        else
            holder.increase.setText("-");
    }
    private void setDeathDec(ViewHolder holder, int deathDec) {
        if(deathDec > 0)
            holder.increase2.setText("▲ " + df.format(deathDec));
        else
            holder.increase2.setText("-");
    }

    @Override
    public int getItemCount() {
        return MAX_LOCAL_SIZE ;
    }
}
