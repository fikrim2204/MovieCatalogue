package com.fikri.moviecatalogueakhir.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.notification.AlarmMovieDaily;
import com.fikri.moviecatalogueakhir.notification.AlarmMovieRelease;

import java.util.Objects;

public class SettingsReminderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
        AlarmMovieDaily alarmMovieDaily;
        AlarmMovieRelease alarmMovieRelease;


        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            alarmMovieDaily = new AlarmMovieDaily();
            alarmMovieRelease = new AlarmMovieRelease();
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(getString(R.string.key_daiy))) {
                boolean daily = sharedPreferences.getBoolean("daily", false);
                if (daily) {
                    alarmMovieDaily.setAlarm(Objects.requireNonNull(getContext()));
                } else {
                    alarmMovieDaily.cancelAlarm(Objects.requireNonNull(getContext()));
                    Toast.makeText(getContext(), "Daily Reminder OFF", Toast.LENGTH_SHORT).show();
                }
            } else if (key.equals(getString(R.string.key_release))) {
                boolean release = sharedPreferences.getBoolean("release", false);
                if (release) {
                    alarmMovieRelease.setAlarm(getActivity());
                } else {
                    alarmMovieRelease.cancelAlarm(Objects.requireNonNull(getContext()));
                    Toast.makeText(getContext(), "Release Reminder OFF", Toast.LENGTH_SHORT).show();
                }
            }
        }


        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }
    }
}