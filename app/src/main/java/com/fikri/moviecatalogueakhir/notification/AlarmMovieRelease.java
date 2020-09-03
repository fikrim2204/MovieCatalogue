package com.fikri.moviecatalogueakhir.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.fikri.moviecatalogueakhir.BuildConfig;
import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.model.Movie;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;
import com.fikri.moviecatalogueakhir.ui.main.SettingsReminderActivity;
import com.fikri.moviecatalogueakhir.ui.main.SplashScreenActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmMovieRelease extends BroadcastReceiver {
    private static int notifIdRelease = 1002;
    ArrayList<ResultsMovie> movies = new ArrayList<>();

    public void setAlarm(Context context) {
        int time = 0;
        Toast.makeText(context, "Release Reminder ON", Toast.LENGTH_SHORT).show();
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmMovieRelease.class);
        intent.putExtra("id", notifIdRelease);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 101, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        assert alarmManager != null;
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis() + time,
                AlarmManager.INTERVAL_DAY, pendingIntent);
        notifIdRelease += 1;
        time += 2000;
        Log.d(AlarmMovieRelease.class.getSimpleName(), "setAlarm");
    }

    private void sendNotification(Context context, String title, String message, int notifId) {
        String CHANNEL_ID = "channel_release";
        String CHANNEL_NAME = "Release Reminder channel";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, SplashScreenActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifIdRelease, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(alarmSound);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();

        if (notificationManager != null) {
            notificationManager.notify(notifId, notification);
        }
        Log.d(AlarmMovieRelease.class.getSimpleName(), "sendNotif");
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmMovieDaily.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notifIdRelease, intent, 0);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

    private void getMovieRelease(final Context context) {
        final Intent intent = new Intent(context, AlarmMovieRelease.class);
        final int id = intent.getIntExtra("id", 0);
        final String message = context.getString(R.string.release_reminder);
        final String appName = context.getString(R.string.app_name);
        String DATE_FORMAT = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        Date date = new Date();
        final String todayDate = dateFormat.format(date);
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.SecAPI +
                "&primary_release_date.gte=" + todayDate + "&primary_release_date.lte=" + todayDate;
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Movie.class, new ParsedRequestListener<Movie>() {
                    @Override
                    public void onResponse(Movie response) {
                        if (response != null) {
                            movies = response.getResults();
                            for (int i = 0; i < movies.size(); i++) {
                                if (movies.get(i).getReleaseDate().equals(todayDate)) {
                                    String title = movies.get(i).getTitle();
                                    String date = movies.get(id).getReleaseDate();
                                    sendNotification(context, appName, title + " " + message, id);
                                    Log.d(AlarmMovieRelease.class.getSimpleName(), "onSend : " + title + " on " + date);
                                }
                                Log.d(SettingsReminderActivity.class.getSimpleName(), "onResponse");
                            }
                        } else {
                            Log.d(SettingsReminderActivity.class.getSimpleName(), "noResponse");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getErrorBody();
                        anError.printStackTrace();
                    }
                });
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        getMovieRelease(context);
    }
}
