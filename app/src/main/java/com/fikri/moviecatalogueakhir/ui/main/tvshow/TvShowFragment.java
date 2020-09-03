package com.fikri.moviecatalogueakhir.ui.main.tvshow;


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
import com.fikri.moviecatalogueakhir.adapter.TvShowAdapter;
import com.fikri.moviecatalogueakhir.model.ResultsTvShow;
import com.fikri.moviecatalogueakhir.model.TvShow;
import com.fikri.moviecatalogueakhir.viewmodel.TvShowViewModel;

import java.util.ArrayList;
import java.util.Objects;


public class TvShowFragment extends Fragment {
    private RecyclerView recyclerTvShow;
    private TvShowAdapter tvShowAdapter;
    private ProgressBar loadingTvShow;
    private TvShowViewModel viewModel;
    private ArrayList<ResultsTvShow> tvShows = new ArrayList<>();
    private androidx.lifecycle.Observer<ArrayList<ResultsTvShow>> getTvShow = new Observer<ArrayList<ResultsTvShow>>() {
        @Override
        public void onChanged(ArrayList<ResultsTvShow> tvShowsItems) {
            if (tvShowsItems != null) {
                tvShowAdapter.setListTvShow(tvShowsItems);
                loading(false);
            }
        }
    };

    public TvShowFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
                viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TvShowViewModel.class);
                viewModel.getTvShows().observe(getActivity(), getTvShow);
                viewModel.loadDataTvShow();
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
                    viewModel = ViewModelProviders.of(getActivity()).get(TvShowViewModel.class);
                    viewModel.getTvShows().observe(getActivity(), getTvShow);
                    viewModel.loadDataTvShow();
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
        String url = "https://api.themoviedb.org/3/search/tv?api_key=" + BuildConfig.SecAPI +
                "&language=en-US&query=" + search;
        AndroidNetworking.get(url)
                .setTag(this)
                .setPriority(Priority.LOW)
                .build()
                .getAsObject(TvShow.class, new ParsedRequestListener<TvShow>() {
                    @Override
                    public void onResponse(TvShow response) {
                        tvShows = response.getResults();
                        tvShowAdapter.setListTvShow(tvShows);
                    }

                    @Override
                    public void onError(ANError anError) {
                        anError.getErrorBody();
                        anError.printStackTrace();
                    }
                });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tvshow, container, false);
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TvShowViewModel viewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        viewModel.getTvShows().observe(this, getTvShow);
        tvShowAdapter = new TvShowAdapter(getContext(), tvShows);
        loadingTvShow = view.findViewById(R.id.progress_tvshow);

        recyclerTvShow = view.findViewById(R.id.recycler_list_tv_show);
        showrecycler();
        recyclerTvShow.setAdapter(tvShowAdapter);
        loading(true);
        viewModel.loadDataTvShow();
    }

    private void loading(Boolean state) {
        if (state) {
            loadingTvShow.setVisibility(View.VISIBLE);
        } else {
            loadingTvShow.setVisibility(View.INVISIBLE);
        }
    }

    private void showrecycler() {
        recyclerTvShow.setHasFixedSize(true);
        recyclerTvShow.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
