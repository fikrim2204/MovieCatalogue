package com.fikri.moviecatalogueakhir.ui.main.tvshow;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.db.DatabaseHelper;
import com.fikri.moviecatalogueakhir.db.TvShowContract;
import com.fikri.moviecatalogueakhir.db.TvShowHelper;
import com.fikri.moviecatalogueakhir.model.ResultsTvShow;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.snackbar.Snackbar;

public class DetailTvShowActivity extends AppCompatActivity {
    public static final String EXTRA_TV_SHOW = "extra_tv_show";

    TvShowHelper tvShowHelper;
    ResultsTvShow tvShow;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tvshow);

        tvShowHelper = new TvShowHelper(getApplicationContext());

        ImageView photo = findViewById(R.id.img_photo_detail_tv_show);
        TextView title = findViewById(R.id.txt_title_detail_tv_show);
        TextView description = findViewById(R.id.txt_description_detail_tv_show);
        TextView rating = findViewById(R.id.txt_content_rating_detail_tv_show);
        TextView dateRelease = findViewById(R.id.txt_date_release_detail_tv_show);
        TextView popularity = findViewById(R.id.txt_content_popularity_detail_show);
        MaterialFavoriteButton favoriteButton = findViewById(R.id.btnFavoriteTvShow);

        tvShowHelper.open();

        tvShow = getIntent().getParcelableExtra(EXTRA_TV_SHOW);

        if (tvShow != null) {
            String poster = "https://image.tmdb.org/t/p/w342" + tvShow.getPosterPath();
            Glide.with(getApplicationContext())
                    .load(poster)
                    .into(photo);
            title.setText(tvShow.getName());
            rating.setText(String.valueOf(tvShow.getVoteAverage()));
            popularity.setText(String.valueOf(tvShow.getPopularity()));
            description.setText(tvShow.getOverview());
            dateRelease.setText(tvShow.getFirstAirDate());

            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            database = databaseHelper.getWritableDatabase();

            String idTvShow = String.valueOf(tvShow.getId());
            if (checkIfExists(idTvShow)) {
                favoriteButton.setFavorite(true);
                favoriteButton.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                    @Override
                    public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                        if (favorite) {
                            addFavorite();
                            Snackbar.make(buttonView, R.string.added_favorite, Snackbar.LENGTH_SHORT).show();
                        } else {
                            int tvShowId = tvShow.getId();
                            tvShowHelper = new TvShowHelper(getApplicationContext());
                            tvShowHelper.deleteTvShow(tvShowId);
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
                        } else {
                            int tvShowId = tvShow.getId();
                            tvShowHelper = new TvShowHelper(getApplicationContext());
                            tvShowHelper.deleteTvShow(tvShowId);
                            Snackbar.make(buttonView, R.string.removed_favorite, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }

    private void addFavorite() {
        ResultsTvShow rTvShow = new ResultsTvShow();

        rTvShow.setId(tvShow.getId());
        rTvShow.setPosterPath(tvShow.getPosterPath());
        rTvShow.setName(tvShow.getName());
        rTvShow.setFirstAirDate(tvShow.getFirstAirDate());
        rTvShow.setPopularity(tvShow.getPopularity());
        rTvShow.setVoteAverage(tvShow.getVoteAverage());
        rTvShow.setOverview(tvShow.getOverview());
        tvShowHelper.insertTvShow(rTvShow);
    }

    private boolean checkIfExists(String args) {
        String[] projection = {
                TvShowContract.TvShowColumns.ID,
                TvShowContract.TvShowColumns.POSTERPATH,
                TvShowContract.TvShowColumns.TITLE,
                TvShowContract.TvShowColumns.RELEASE_DATE,
                TvShowContract.TvShowColumns.POPULARITY,
                TvShowContract.TvShowColumns.VOTEAVERAGE,
                TvShowContract.TvShowColumns.OVERVIEW
        };
        String searching = TvShowContract.TvShowColumns.ID + " =?";
        String[] searchingArgs = {args};
        String limit = "1";

        Cursor cursor = database.query(TvShowContract.TvShowColumns.TABLE_TVSHOW, projection, searching, searchingArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
