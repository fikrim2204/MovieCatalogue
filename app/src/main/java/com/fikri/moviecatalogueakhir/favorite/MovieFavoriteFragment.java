package com.fikri.moviecatalogueakhir.favorite;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.adapter.MovieAdapter;
import com.fikri.moviecatalogueakhir.db.MovieHelper;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieFavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView tvBlank;
    private MovieAdapter movieAdapter;
    private MovieHelper movieHelper;
    private ArrayList<ResultsMovie> listFavMovie = new ArrayList<>();


    public MovieFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_list_movie_favorite);
        tvBlank = view.findViewById(R.id.tv_blank);
        showRecyclerView();
        loadData();
        checkIsEmpty();
    }

    private void checkIsEmpty() {
        if (listFavMovie.size() > 0) {
            tvBlank.setText("");
        } else {
            tvBlank.setText(R.string.add_some_movie_to_favorite);
        }
    }

    private void showRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void loadData() {
        movieHelper = MovieHelper.getInstance(getContext());
        listFavMovie.addAll(movieHelper.getAllMovie());
        movieAdapter = new MovieAdapter(getContext(), listFavMovie);
        recyclerView.setAdapter(movieAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
        movieAdapter.setListMovie(movieHelper.getAllMovie());
    }
}
