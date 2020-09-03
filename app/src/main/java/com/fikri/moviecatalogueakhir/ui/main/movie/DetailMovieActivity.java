package com.fikri.moviecatalogueakhir.ui.main.movie;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.db.DatabaseHelper;
import com.fikri.moviecatalogueakhir.db.MovieContract;
import com.fikri.moviecatalogueakhir.db.MovieHelper;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;
import com.fikri.moviecatalogueakhir.widget.MovieFavoriteWidgetProvider;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

public class DetailMovieActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String UPDATE_WIDGET = "com.fikri.moviecatalogueakhir.UPDATE_WIDGET";

    MovieHelper movieHelper;
    ResultsMovie movie;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_film);

        movieHelper = new MovieHelper(getApplicationContext());

        ImageView photo = findViewById(R.id.img_photo_detail_film);
        TextView rating = findViewById(R.id.txt_content_rating_detail_film);
        TextView description = findViewById(R.id.txt_description_detail_film);
        TextView dateRelease = findViewById(R.id.txt_date_release_detail_film);
        TextView popularity = findViewById(R.id.txt_content_popularity_detail_film);
        TextView title = findViewById(R.id.txt_title_detail_film);
        MaterialFavoriteButton favoriteButton = findViewById(R.id.btnFavoriteFilm);

        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);

        if (movie != null) {
            String poster = "https://image.tmdb.org/t/p/w342" + movie.getPosterPath();
            Glide.with(getApplicationContext())
                    .load(poster)
                    .into(photo);
            title.setText(movie.getTitle());
            rating.setText(String.valueOf(movie.getVoteAverage()));
            popularity.setText(String.valueOf(movie.getPopularity()));
            description.setText(movie.getOverview());
            dateRelease.setText(movie.getReleaseDate());

            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            database = databaseHelper.getWritableDatabase();

            String idMovie = String.valueOf(movie.getId());
            if (checkIfExists(idMovie)) {
                favoriteButton.setFavorite(true);
                favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            addFavorite();
                            Snackbar.make(buttonView, R.string.added_favorite, Snackbar.LENGTH_SHORT).show();

                            Intent intentBroadcast = new Intent(getApplicationContext(), MovieFavoriteWidgetProvider.class);
                            intentBroadcast.setAction(UPDATE_WIDGET);
                            sendBroadcast(intentBroadcast);
                        } else {
                            int movieId = movie.getId();
                            movieHelper = new MovieHelper(getApplicationContext());
                            movieHelper.deleteMovie(movieId);
                            Snackbar.make(buttonView, R.string.removed_favorite, Snackbar.LENGTH_SHORT).show();
                        }
                    }

                });
            } else {
                favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            addFavorite();
                            Snackbar.make(buttonView, R.string.added_favorite, Snackbar.LENGTH_SHORT).show();

                            Intent intentBroadcast = new Intent(getApplicationContext(), MovieFavoriteWidgetProvider.class);
                            intentBroadcast.setAction(UPDATE_WIDGET);
                            sendBroadcast(intentBroadcast);
                        } else {
                            int movieId = movie.getId();
                            movieHelper = new MovieHelper(getApplicationContext());
                            movieHelper.deleteMovie(movieId);
                            Snackbar.make(buttonView, R.string.removed_favorite, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }


    public void addFavorite() {
        ResultsMovie rMovie = new ResultsMovie();

        rMovie.setId(movie.getId());
        rMovie.setPosterPath(movie.getPosterPath());
        rMovie.setTitle(movie.getTitle());
        rMovie.setReleaseDate(movie.getReleaseDate());
        rMovie.setPopularity(movie.getPopularity());
        rMovie.setVoteAverage(movie.getVoteAverage());
        rMovie.setOverview(movie.getOverview());
        movieHelper.insertMovie(rMovie);
    }

    private boolean checkIfExists(String args) {

        String[] projection = {
                MovieContract.MovieColumns.ID,
                MovieContract.MovieColumns.POSTERPATH,
                MovieContract.MovieColumns.TITLE,
                MovieContract.MovieColumns.DATE_RELEASE,
                MovieContract.MovieColumns.POPULARITY,
                MovieContract.MovieColumns.VOTEAVERAGE,
                MovieContract.MovieColumns.OVERVIEW
        };
        String searching = MovieContract.MovieColumns.ID + " =?";
        String[] searchingArgs = {args};
        String limit = "1";

        Cursor cursor = database.query(MovieContract.TABLE_MOVIE, projection, searching, searchingArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
