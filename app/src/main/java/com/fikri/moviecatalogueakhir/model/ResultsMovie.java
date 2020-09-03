package com.fikri.moviecatalogueakhir.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.fikri.moviecatalogueakhir.db.MovieContract;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import static com.fikri.moviecatalogueakhir.db.MovieContract.MovieColumns.ID;

public class ResultsMovie implements Parcelable {

    public static final Parcelable.Creator<ResultsMovie> CREATOR = new Parcelable.Creator<ResultsMovie>() {
        @Override
        public ResultsMovie createFromParcel(Parcel source) {
            return new ResultsMovie(source);
        }

        @Override
        public ResultsMovie[] newArray(int size) {
            return new ResultsMovie[size];
        }
    };
    @SerializedName("overview")
    private String overview;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("video")
    private boolean video;
    @SerializedName("title")
    private String title;
    @SerializedName("genre_ids")
    private List<Integer> genreIds;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("popularity")
    private double popularity;
    @SerializedName("id")
    private int id;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("vote_count")
    private int voteCount;

    public ResultsMovie() {

    }

    private ResultsMovie(Parcel in) {
        this.overview = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();
        this.video = in.readByte() != 0;
        this.title = in.readString();
        this.genreIds = new ArrayList<>();
        in.readList(this.genreIds, Integer.class.getClassLoader());
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
        this.popularity = in.readDouble();
        this.id = in.readInt();
        this.adult = in.readByte() != 0;
        this.voteCount = in.readInt();
    }

    public ResultsMovie(Cursor cursor) {
        this.id = MovieContract.getColumnInt(cursor, ID);
        this.posterPath = MovieContract.getColumnString(cursor, MovieContract.MovieColumns.POSTERPATH);
        this.title = MovieContract.getColumnString(cursor, MovieContract.MovieColumns.TITLE);
        this.voteAverage = Double.parseDouble(MovieContract.getColumnString(cursor, MovieContract.MovieColumns.VOTEAVERAGE));
        this.popularity = Double.parseDouble(MovieContract.getColumnString(cursor, MovieContract.MovieColumns.POPULARITY));
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.overview);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.originalTitle);
        dest.writeByte(this.video ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeList(this.genreIds);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.releaseDate);
        dest.writeDouble(this.voteAverage);
        dest.writeDouble(this.popularity);
        dest.writeInt(this.id);
        dest.writeByte(this.adult ? (byte) 1 : (byte) 0);
        dest.writeInt(this.voteCount);
    }
}