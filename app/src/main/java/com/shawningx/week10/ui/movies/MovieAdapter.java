package com.shawningx.week10.ui.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shawningx.week10.R;
import com.shawningx.week10.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final List<Movie> items = new ArrayList<>();

    public void submitList(List<Movie> movies) {
        items.clear();
        if (movies != null) {
            items.addAll(movies);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ImageView poster;
        private final TextView title;
        private final TextView duration;
        private final TextView description;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.image_poster);
            title = itemView.findViewById(R.id.text_title);
            duration = itemView.findViewById(R.id.text_duration);
            description = itemView.findViewById(R.id.text_description);
        }

        void bind(Movie movie) {
            title.setText(movie.getTitle());
            duration.setText(itemView.getContext().getString(
                R.string.movie_duration,
                movie.getDuration()
            ));
            description.setText(movie.getDescription());

            Glide.with(itemView.getContext())
                .load(movie.getPosterUrl())
                .placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(poster);
        }
    }
}
