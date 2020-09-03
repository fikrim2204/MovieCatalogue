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
import com.fikri.moviecatalogueakhir.model.ResultsTvShow;
import com.fikri.moviecatalogueakhir.ui.main.tvshow.DetailTvShowActivity;

import java.util.ArrayList;

public class TvShowAdapter extends RecyclerView.Adapter<TvShowAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ResultsTvShow> listTvShow;

    public TvShowAdapter(Context context, ArrayList<ResultsTvShow> listTvShow) {
        this.context = context;
        this.listTvShow = listTvShow;
    }

    private ArrayList<ResultsTvShow> getListTvShow() {
        return listTvShow;
    }

    public void setListTvShow(ArrayList<ResultsTvShow> listTvShow) {
        if (listTvShow.size() > 0) {
            this.listTvShow.clear();
        }
        this.listTvShow = listTvShow;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tv_show, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(listTvShow.get(position));
    }

    @Override
    public int getItemCount() {
        return getListTvShow().size();
    }

    private String checkTextIfNull(String text) {
        if (text != null && !text.isEmpty()) {
            return text;
        } else {
            return "-";
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardTVShow;
        private ImageView imageTVShow;
        private TextView tvTitle, tvRating, tvPopularity;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTVShow = itemView.findViewById(R.id.img_poster_tv_show);
            cardTVShow = itemView.findViewById(R.id.card_tv_show);
            tvTitle = itemView.findViewById(R.id.tv_title_tv_shows);
            tvRating = itemView.findViewById(R.id.tv_rating_tv_show);
            tvPopularity = itemView.findViewById(R.id.tv_popularity_tv_show);
        }

        void onBind(final ResultsTvShow tvShow) {
            String poster = "https://image.tmdb.org/t/p/w185" + tvShow.getPosterPath();

            if (tvShow.getPosterPath() != null && !tvShow.getPosterPath().isEmpty()) {
                Glide.with(context)
                        .load(poster)
                        .transform(new RoundedCorners(4))
                        .into(imageTVShow);
            }
            String title = checkTextIfNull(tvShow.getName());
            if (title.length() > 40) {
                tvTitle.setText(String.format("%s...", title.substring(0, 39)));
            } else {
                tvTitle.setText(checkTextIfNull(tvShow.getName()));
            }

            tvRating.setText(checkTextIfNull(String.valueOf(tvShow.getVoteAverage())));
            tvPopularity.setText(checkTextIfNull(String.valueOf(tvShow.getPopularity())));
            cardTVShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentDetailMovie = new Intent(context, DetailTvShowActivity.class);
                    intentDetailMovie.putExtra(DetailTvShowActivity.EXTRA_TV_SHOW, tvShow);
                    context.startActivity(intentDetailMovie);
                }
            });
        }
    }
}
