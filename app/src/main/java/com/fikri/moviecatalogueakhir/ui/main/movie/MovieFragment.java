package com.fikri.moviecatalogueakhir.ui.main.movie;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.fikri.moviecatalogueakhir.BuildConfig;
import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.adapter.MovieAdapter;
import com.fikri.moviecatalogueakhir.model.Movie;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;
import com.fikri.moviecatalogueakhir.viewmodel.MoviesViewModel;

import java.util.ArrayList;
import java.util.Objects;


public class MovieFragment extends Fragment {
    private RecyclerView recyclerMovie;
    private MoviesViewModel viewModel;
    private MovieAdapter movieAdapter;
    private ProgressBar loadingMovie;
    private ArrayList<ResultsMovie> movies = new ArrayList<>();
    private Observer<ArrayList<ResultsMovie>> getMovie = new Observer<ArrayList<ResultsMovie>>() {
        @Override
        public void onChanged(ArrayList<ResultsMovie> movieItems) {
            if (movieItems != null) {
                movies.clear();
                movieAdapter.setListMovie(movieItems);
                loading(false);
            }
        }
    };

    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(MoviesViewModel.class);
        viewModel.getMovies().observe(this, getMovie);
        movieAdapter = new MovieAdapter(getContext(), movies);

        loadingMovie = view.findViewById(R.id.progress_movie);
        recyclerMovie = view.findViewById(R.id.recycler_list_movie);

        showRecyclerView();
        loading(true);
        viewModel.loadDataMovies();
    }

    private void showRecyclerView() {
        recyclerMovie.setHasFixedSize(true);
        recyclerMovie.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMovie.setAdapter(movieAdapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.searchBar);
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MoviesViewModel.class);
                viewModel.getMovies().observe(getActivity(), getMovie);
                viewModel.loadDataMovies();
                return true;
            }
        });
        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();

        if (searchView != null) {
            assert searchManager != null;
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MoviesViewModel.class);
                    viewModel.getMovies().observe(getActivity(), getMovie);
                    viewModel.loadDataMovies();
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    loadSearch(query);
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void loadSearch(String search) {
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" + BuildConfig.SecAPI +
                "&language=en-US&query=" + search;
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(Movie.class, new ParsedRequestListener<Movie>() {
                    @Override
                    public void onResponse(Movie response) {
                        movies = response.getResults();
                        movieAdapter.setListMovie(movies);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getErrorBody();
                        anError.printStackTrace();
                    }
                });
    }

    private void loading(Boolean state) {
        if (state) {
            loadingMovie.setVisibility(View.VISIBLE);
        } else {
            loadingMovie.setVisibility(View.INVISIBLE);
        }
    }
}
