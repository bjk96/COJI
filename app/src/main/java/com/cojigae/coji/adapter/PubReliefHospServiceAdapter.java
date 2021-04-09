package com.cojigae.coji.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.R;
import com.cojigae.coji.item.PubReliefHospServiceItem;

import java.util.List;

public class PubReliefHospServiceAdapter extends RecyclerView.Adapter<PubReliefHospServiceAdapter.MyViewHolder> {

    public List<PubReliefHospServiceItem> items;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;
        public TextView textViewSido;
        public TextView textViewSgg;
        public TextView textViewTelno;
        public ImageButton imageButton;

        public MyViewHolder(View v) {
            super(v);
            textViewName = v.findViewById(R.id.yadmNm);
            textViewSido = v.findViewById(R.id.sidoNm);
            textViewSgg = v.findViewById(R.id.sgguNm);
            textViewTelno = v.findViewById(R.id.telno);
            imageButton = v.findViewById(R.id.hospLocation);
        }
    }

    public PubReliefHospServiceAdapter(List<PubReliefHospServiceItem> PubReliefHospServiceItems) {
        this.items = PubReliefHospServiceItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hosp, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textViewName.setText(items.get(position).getYadmNm());
        holder.textViewSido.setText(items.get(position).getSidoNm());
        holder.textViewSgg.setText(items.get(position).getSgguNm());
        holder.textViewTelno.setText(items.get(position).getTelno());

        holder.imageButton.setOnClickListener(v -> {
            openMap(v.getContext(), position);
        });

        holder.textViewTelno.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+items.get(position).getTelno()));
            v.getContext().startActivity(callIntent);
        });

        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setItems(R.array.screeningCenter_dialog_menu, (dialog, which) -> {
                switch(which){
                    case 0:
                        openMap(v.getContext(), position);
                        break;
                    case 1:
                        Intent callIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+items.get(position).getTelno()));
                        v.getContext().startActivity(callIntent);
                        break;
                }
            }).show();
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void openMap(Context context, int position) {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + items.get(position).getSgguNm() + " " + items.get(position).getYadmNm()));
        context.startActivity(mapIntent);
    }
}
