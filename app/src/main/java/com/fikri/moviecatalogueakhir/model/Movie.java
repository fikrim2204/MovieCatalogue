package com.fikri.moviecatalogueakhir.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movie {

    @SerializedName("results")
    private ArrayList<ResultsMovie> results;

    public Movie(ArrayList<ResultsMovie> results) {
        this.results = results;
    }

    public ArrayList<ResultsMovie> getResults() {
        return results;
    }


}