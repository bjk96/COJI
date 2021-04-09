package com.cojigae.coji;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageButton;

import com.cojigae.coji.adapter.OpenLicenseRecyclerViewAdapter;
import com.cojigae.coji.item.SettingItem;

public class OpenLicenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_lisence);

        RecyclerView rv_license = findViewById(R.id.rv_license);
        rv_license.setLayoutManager(new LinearLayoutManager(this));
        rv_license.addItemDecoration(new DividerItemDecoration(this, 1));

        String[] title = getResources().getStringArray(R.array.license_title);
        String[] subtitle = getResources().getStringArray(R.array.license_subtitle);

        OpenLicenseRecyclerViewAdapter adapter = new OpenLicenseRecyclerViewAdapter();

        for(int i = 0 ; i < title.length; i++){
            adapter.items.add(new SettingItem(title[i], subtitle[i], false));
        }

        rv_license.setAdapter(adapter);

        ImageButton ib_back = findViewById(R.id.ib_back);

        ib_back.setOnClickListener(v -> {
            finish();
        });
    }
}