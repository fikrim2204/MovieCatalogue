package com.fikri.moviecatalogueakhir.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.fikri.moviecatalogueakhir.BuildConfig;
import com.fikri.moviecatalogueakhir.model.Movie;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;

import java.util.ArrayList;

public class MoviesViewModel extends ViewModel {
    private final static String TAG = MoviesViewModel.class.getSimpleName();
    private MutableLiveData<ArrayList<ResultsMovie>> liveData;

    public LiveData<ArrayList<ResultsMovie>> getMovies() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            loadDataMovies();
        }
        return liveData;
    }

    public void loadDataMovies() {
        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + BuildConfig.SecAPI +
                "&language=en-US";
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Movie.class, new ParsedRequestListener<Movie>() {
                    @Override
                    public void onResponse(Movie response) {
                        Log.d(TAG, "onResponse");
                        liveData.postValue(response.getResults());
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError : " + anError);
                    }
                });
    }

}
