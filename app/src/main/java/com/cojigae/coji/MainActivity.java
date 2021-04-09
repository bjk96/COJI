package com.cojigae.coji;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.cojigae.coji.fragment.HomeFragment;
import com.cojigae.coji.fragment.KoreaSituationFragment;
import com.cojigae.coji.fragment.ScreeningCenterFragment;
import com.cojigae.coji.fragment.WorldSituationFragment;
import com.cojigae.coji.service.NotificationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    Fragment homeFragment;
    Fragment koreaSituationFragment;
    Fragment worldSituationFragment;
    Fragment screeningCenterFragment;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        bottomNav = findViewById(R.id.bottomNav);

        homeFragment = new HomeFragment();
        koreaSituationFragment = new KoreaSituationFragment();
        worldSituationFragment = new WorldSituationFragment();
        screeningCenterFragment = new ScreeningCenterFragment();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        FragmentManager fragmentManager = getSupportFragmentManager();

        bottomNav.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.bottom_home:
                    fragmentManager.beginTransaction().replace(R.id.container, homeFragment).commit();
                    return true;
                case R.id.bottom_situation_korea:
                    fragmentManager.beginTransaction().replace(R.id.container, koreaSituationFragment).commit();
                    return true;
                case R.id.bottom_situation_world:
                    fragmentManager.beginTransaction().replace(R.id.container, worldSituationFragment).commit();
                    return true;
                case R.id.bottom_screening_center:
                    fragmentManager.beginTransaction().replace(R.id.container, screeningCenterFragment).commit();
                    return true;
            }
            return false;
        });

        Intent notificationServiceIntent = new Intent(getApplicationContext(), NotificationService.class);
        startService(notificationServiceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.toolbar_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClicked(View view){
        switch (view.getId()){
            case R.id.tv_title:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                bottomNav.setSelectedItemId(R.id.bottom_home);
                break;
        }
    }
}