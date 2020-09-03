package com.fikri.moviecatalogueakhir.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;

import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.CONTENT_URI;
import static com.fikri.moviecatalogueakhir.widget.MovieFavoriteWidgetProvider.EXTRA_ITEM;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private final Context context;
    private Cursor cursor;

    StackRemoteViewsFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDataSetChanged() {
        if (cursor != null) {
            cursor.close();
        }

        final long identityToken = Binder.clearCallingIdentity();
        cursor = context.getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null
        );
        Binder.restoreCallingIdentity(identityToken);
    }

    public RemoteViews getViewAt(int position) {
        ResultsMovie movie = getMovie(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);
        if (movie != null) {
            String poster = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
            Bitmap bitmap = null;

            try {
                bitmap = Glide.with(context)
                        .asBitmap()
                        .load(poster)
                        .submit(500, 750)
                        .get();
            } catch (Exception e) {
                e.printStackTrace();
            }

            remoteViews.setImageViewBitmap(R.id.imageMovie, bitmap);

            Bundle bundle = new Bundle();
            bundle.putInt(EXTRA_ITEM, position);
            Intent fillIntent = new Intent();
            fillIntent.putExtras(bundle);
            remoteViews.setOnClickFillInIntent(R.id.imageMovie, fillIntent);
        }
        return remoteViews;
    }

    private ResultsMovie getMovie(int position) {
        if (!cursor.moveToPosition(position)) {
            return null;
        } else {
            return new ResultsMovie(cursor);
        }

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
