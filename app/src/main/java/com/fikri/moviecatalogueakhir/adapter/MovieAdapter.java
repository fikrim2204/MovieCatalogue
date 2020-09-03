package com.fikri.moviecatalogueakhir.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.fikri.moviecatalogueakhir.R;
import com.fikri.moviecatalogueakhir.model.ResultsMovie;
import com.fikri.moviecatalogueakhir.ui.main.movie.DetailMovieActivity;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ResultsMovie> listMovie;

    public MovieAdapter(Context context, ArrayList<ResultsMovie> listMovie) {
        this.context = context;
        this.listMovie = listMovie;
    }

    private List<ResultsMovie> getListMovie() {
        return listMovie;
    }

    public void setListMovie(ArrayList<ResultsMovie> listMovie) {
        if (listMovie.size() > 0) {
            this.listMovie.clear();
        }
        this.listMovie = listMovie;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_film, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(listMovie.get(position));
    }

    @Override
    public int getItemCount() {
        return getListMovie().size();
    }

    private String checkTextIfNull(String text) {
        if (text != null && !text.isEmpty()) {
            return text;
        } else {
            return "-";
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView tvTitle, tvRating, tvPopularity;
        private CardView cardMovie;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster_movie);
            tvTitle = itemView.findViewById(R.id.tv_title_movie);
            tvRating = itemView.findViewById(R.id.tv_rating_movie);
            tvPopularity = itemView.findViewById(R.id.tv_popularity_movie);
            cardMovie = itemView.findViewById(R.id.card_movie);
        }

        void onBind(final ResultsMovie movie) {
            String poster = "https://image.tmdb.org/t/p/w185" + movie.getPosterPath();

            if (movie.getPosterPath() != null && !movie.getPosterPath().isEmpty()) {
                Glide.with(context)
                        .load(poster)
                        .transform(new RoundedCorners(4))
                        .into(imgPoster);
            }
            String title = checkTextIfNull(movie.getTitle());
            if (title.length() > 40) {
                tvTitle.setText(String.format("%s...", title.substring(0, 39)));
            } else {
                tvTitle.setText(checkTextIfNull(movie.getTitle()));
            }

            tvRating.setText(checkTextIfNull(String.valueOf(movie.getVoteAverage())));
            tvPopularity.setText(checkTextIfNull(String.valueOf(movie.getPopularity())));
            cardMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentDetailMovie = new Intent(context, DetailMovieActivity.class);
                    intentDetailMovie.putExtra(DetailMovieActivity.EXTRA_MOVIE, movie);
                    context.startActivity(intentDetailMovie);
                }
            });
        }
    }
}
