package com.fikri.moviecatalogueakhir.db;

import android.provider.BaseColumns;

public class TvShowContract {
    public static final class TvShowColumns implements BaseColumns {
        public static String TABLE_TVSHOW = "tv_show";
        public static String ID = "id";
        public static String POSTERPATH = "poster_path";
        public static String TITLE = "title";
        public static String RELEASE_DATE = "release_date";
        public static String POPULARITY = "popularity";
        public static String VOTEAVERAGE = "vote_average";
        public static String OVERVIEW = "overview";
    }
}
