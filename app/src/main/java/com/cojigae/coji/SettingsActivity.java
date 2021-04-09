package com.cojigae.coji;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cojigae.coji.adapter.SettingRecyclerViewAdapter;
import com.cojigae.coji.item.SettingItem;

public class SettingsActivity extends AppCompatActivity {

    SettingRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.tb_settings);
        ImageButton ib_back = findViewById(R.id.ib_back);
        RecyclerView rv_settings = findViewById(R.id.rv_settings);

        adapter = new SettingRecyclerViewAdapter(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ib_back.setOnClickListener(v -> finish());

        setSettings();

        rv_settings.setLayoutManager(new LinearLayoutManager(this));
        rv_settings.addItemDecoration(new DividerItemDecoration(this, 1));
        rv_settings.setAdapter(adapter);
    }

    private void setSettings() {
        adapter.settingItems.add(new SettingItem("알림 설정", "알림을 켜면 매일 오전 10시 코로나19 감염 현황을 알려줍니다.", true));
        adapter.settingItems.add(new SettingItem("오픈 라이선스", "", false));
    }
}