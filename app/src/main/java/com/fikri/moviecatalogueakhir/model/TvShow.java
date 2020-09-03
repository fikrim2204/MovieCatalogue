package com.fikri.moviecatalogueakhir.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TvShow {
    @SerializedName("results")
    private ArrayList<ResultsTvShow> results;

    public TvShow(ArrayList<ResultsTvShow> results) {
        this.results = results;
    }

    public ArrayList<ResultsTvShow> getResults() {
        return results;
    }

}