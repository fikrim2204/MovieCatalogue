package com.fikri.moviecatalogueakhir.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.adapter.SectionsPagerAdapter;
import com.fikri.moviecatalogueakhir.base.BaseAppCompat;
import com.fikri.moviecatalogueakhir.db.MovieHelper;
import com.fikri.moviecatalogueakhir.db.TvShowHelper;
import com.fikri.moviecatalogueakhir.favorite.FavoriteActivity;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends BaseAppCompat {

    MovieHelper movieHelper;
    TvShowHelper tvShowHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        movieHelper = new MovieHelper(getApplicationContext());
        tvShowHelper = new TvShowHelper(getApplicationContext());
        movieHelper.open();
        tvShowHelper.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.move_to_favorite) {
            Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.action_change_reminder) {
            Intent intent = new Intent(this, SettingsReminderActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieHelper.close();
        tvShowHelper.close();
    }
}