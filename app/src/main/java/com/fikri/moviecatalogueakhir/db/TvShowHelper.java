package com.fikri.moviecatalogueakhir.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fikri.moviecatalogueakhir.model.ResultsTvShow;

import java.util.ArrayList;

import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.ID;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.OVERVIEW;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.POPULARITY;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.POSTERPATH;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.RELEASE_DATE;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.TABLE_TVSHOW;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.TITLE;
import static com.fikri.moviecatalogueakhir.db.TvShowContract.TvShowColumns.VOTEAVERAGE;

public class TvShowHelper {
    private static final String DATABASE_TABLE = TABLE_TVSHOW;
    private static DatabaseHelper databaseHelper;
    private static TvShowHelper INSTANCE;

    private static SQLiteDatabase database;

    public TvShowHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvShowHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TvShowHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();

        if (database.isOpen()) {
            database.close();
        }
    }

    public ArrayList<ResultsTvShow> getAllTvShow() {
        ArrayList<ResultsTvShow> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " ASC",
                null);
        cursor.moveToFirst();
        ResultsTvShow tvShow;
        if (cursor.getCount() > 0) {
            do {
                tvShow = new ResultsTvShow();
                tvShow.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                tvShow.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTERPATH)));
                tvShow.setName(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                tvShow.setFirstAirDate(cursor.getString(cursor.getColumnIndexOrThrow(RELEASE_DATE)));
                tvShow.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                tvShow.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTEAVERAGE)));
                tvShow.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                arrayList.add(tvShow);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public void insertTvShow(ResultsTvShow tvShow) {
        ContentValues args = new ContentValues();
        args.put(ID, tvShow.getId());
        args.put(POSTERPATH, tvShow.getPosterPath());
        args.put(TITLE, tvShow.getName());
        args.put(RELEASE_DATE, tvShow.getFirstAirDate());
        args.put(POPULARITY, tvShow.getPopularity());
        args.put(VOTEAVERAGE, tvShow.getVoteAverage());
        args.put(OVERVIEW, tvShow.getOverview());
        database.insert(DATABASE_TABLE, null, args);
    }

    public void deleteTvShow(int id) {
        database.delete(TABLE_TVSHOW, ID + " = '" + id + "'", null);
    }
}
