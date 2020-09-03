package com.fikri.moviecatalogueakhir.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fikri.moviecatalogueakhir.model.ResultsMovie;

import java.util.ArrayList;

import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.DATE_RELEASE;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.ID;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.OVERVIEW;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.POPULARITY;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.POSTERPATH;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.TITLE;
import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.VOTEAVERAGE;
import static com.fikri.moviecatalogueakhir.db.MovieContract.TABLE_MOVIE;

public class MovieHelper {
    private static final String DATABASE_TABLE = TABLE_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieHelper INSTANCE;

    private static SQLiteDatabase database;

    public MovieHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MovieHelper(context);
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

    public ArrayList<ResultsMovie> getAllMovie() {
        ArrayList<ResultsMovie> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null,
                null,
                null,
                null,
                null,
                ID + " ASC",
                null);
        cursor.moveToFirst();
        ResultsMovie movie;
        if (cursor.getCount() > 0) {
            do {
                movie = new ResultsMovie();
                movie.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndexOrThrow(POSTERPATH)));
                movie.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE_RELEASE)));
                movie.setPopularity(cursor.getDouble(cursor.getColumnIndexOrThrow(POPULARITY)));
                movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndexOrThrow(VOTEAVERAGE)));
                movie.setOverview(cursor.getString(cursor.getColumnIndexOrThrow(OVERVIEW)));

                arrayList.add(movie);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public void insertMovie(ResultsMovie movie) {
        ContentValues args = new ContentValues();
        args.put(ID, movie.getId());
        args.put(POSTERPATH, movie.getPosterPath());
        args.put(TITLE, movie.getTitle());
        args.put(DATE_RELEASE, movie.getReleaseDate());
        args.put(POPULARITY, movie.getPopularity());
        args.put(VOTEAVERAGE, movie.getVoteAverage());
        args.put(OVERVIEW, movie.getOverview());
        database.insert(DATABASE_TABLE, null, args);
    }

    public void deleteMovie(int id) {
        database.delete(TABLE_MOVIE, ID + " = '" + id + "'", null);
    }

    public Cursor queryByIdProvider(String id) {
        return database.query(DATABASE_TABLE, null
                , ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public Cursor queryProvider() {
        return database.query(DATABASE_TABLE
                , null
                , null
                , null,
                null,
                null
                , ID + " ASC");
    }

    public long insertProvider(ContentValues contentValues) {
        return database.insert(DATABASE_TABLE, null, contentValues);
    }

    public int deleteProvider(String id) {
        return database.delete(DATABASE_TABLE, ID + " = ?", new String[]{id});
    }
}
