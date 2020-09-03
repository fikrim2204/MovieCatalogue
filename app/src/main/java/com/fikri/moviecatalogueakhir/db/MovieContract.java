package com.fikri.moviecatalogueakhir.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {
    public static final String AUTHORITY = "com.fikri.moviecatalogueakhir";
    private static final String SCHEME = "content";
    public static String TABLE_MOVIE = "movie";

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static final class MovieColumns implements BaseColumns {
        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_MOVIE)
                .build();
        public static String ID = "id";
        public static String POSTERPATH = "poster_path";
        public static String TITLE = "title";
        public static String DATE_RELEASE = "date_release";
        public static String POPULARITY = "popularity";
        public static String VOTEAVERAGE = "vote_average";
        public static String OVERVIEW = "overview";
    }
}
