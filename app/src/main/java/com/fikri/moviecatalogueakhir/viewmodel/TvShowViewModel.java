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
import com.fikri.moviecatalogueakhir.model.ResultsTvShow;
import com.fikri.moviecatalogueakhir.model.TvShow;

import java.util.ArrayList;

public class TvShowViewModel extends ViewModel {
    private final static String TAG = TvShowViewModel.class.getSimpleName();
    private MutableLiveData<ArrayList<ResultsTvShow>> liveData;

    public LiveData<ArrayList<ResultsTvShow>> getTvShows() {
        if (liveData == null) {
            liveData = new MutableLiveData<>();
            loadDataTvShow();
        }
        return liveData;
    }

    public void loadDataTvShow() {
        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + BuildConfig.SecAPI +
                "&language=en-US";
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(TvShow.class, new ParsedRequestListener<TvShow>() {
                    @Override
                    public void onResponse(TvShow response) {
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
