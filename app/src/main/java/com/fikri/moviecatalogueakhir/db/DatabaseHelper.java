package com.fikri.moviecatalogueakhir.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.DATE_RELEASE;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.ID;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.OVERVIEW;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.POPULARITY;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.POSTERPATH;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.TITLE;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.VOTEAVERAGE;
import static com.fikri.moviecatalogueakhir.db.MovieContract.TABLE_MOVIE;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.RELEASE_DATE;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.TABLE_TVSHOW;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "dbmoviefikri";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_MOVIE,
            ID,
            POSTERPATH,
            TITLE,
            DATE_RELEASE,
            POPULARITY,
            VOTEAVERAGE,
            OVERVIEW
    );

    private static final String SQL_CREATE_TABLE_TVSHOW = String.format("CREATE TABLE %s" +
                    " (%s INTEGER PRIMARY KEY NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            TABLE_TVSHOW,
            ID,
            POSTERPATH,
            TITLE,
            RELEASE_DATE,
            POPULARITY,
            VOTEAVERAGE,
            OVERVIEW
    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_TVSHOW);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TVSHOW);
        onCreate(sqLiteDatabase);
    }
}
