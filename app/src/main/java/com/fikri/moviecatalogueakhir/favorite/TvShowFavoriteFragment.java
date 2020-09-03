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
import com.fikri.moviecatalogueakhir.adapter.TvShowAdapter;
import com.fikri.moviecatalogueakhir.db.TvShowHelper;
import com.fikri.moviecatalogueakhir.model.ResultsTvShow;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowFavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private TextView tvBlank;
    private TvShowAdapter tvShowAdapter;
    private TvShowHelper tvShowHelper;
    private ArrayList<ResultsTvShow> listFavTvShow = new ArrayList<>();

    public TvShowFavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tv_show_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_list_tvshow_favorite);
        tvBlank = view.findViewById(R.id.tv_blank);
        showRecyclerView();
        loadData();
        checkIsEmpty();
    }

    private void checkIsEmpty() {
        if (listFavTvShow.size() > 0) {
            tvBlank.setText("");
        } else {
            tvBlank.setText(R.string.add_some_tv_show_to_favorite);
        }
    }

    private void showRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
    }

    private void loadData() {
        tvShowHelper = TvShowHelper.getInstance(getContext());
        listFavTvShow.addAll(tvShowHelper.getAllTvShow());
        tvShowAdapter = new TvShowAdapter(getContext(), listFavTvShow);
        recyclerView.setAdapter(tvShowAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvShowAdapter.setListTvShow(tvShowHelper.getAllTvShow());
    }
}
