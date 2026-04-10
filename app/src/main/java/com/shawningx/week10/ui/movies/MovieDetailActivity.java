package com.shawningx.week10.ui.movies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shawningx.week10.R;
import com.shawningx.week10.viewmodel.ShowTimesViewModel;
import com.shawningx.week10.ui.showtimes.ShowTimeAdapter;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "extra_movie_id";
    public static final String EXTRA_MOVIE_TITLE = "extra_movie_title";
    public static final String EXTRA_MOVIE_DESCRIPTION = "extra_movie_description";
    public static final String EXTRA_MOVIE_POSTER = "extra_movie_poster";
    public static final String EXTRA_MOVIE_DURATION = "extra_movie_duration";

    private ShowTimesViewModel viewModel;
    private ShowTimeAdapter adapter;
    private ProgressBar loadingView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ImageView poster = findViewById(R.id.image_poster);
        TextView title = findViewById(R.id.text_title);
        TextView description = findViewById(R.id.text_description);
        TextView duration = findViewById(R.id.text_duration);
        loadingView = findViewById(R.id.progress_loading);
        emptyView = findViewById(R.id.text_empty);
        RecyclerView recyclerView = findViewById(R.id.recycler_showtimes);

        Intent intent = getIntent();
        String movieId = intent.getStringExtra(EXTRA_MOVIE_ID);
        String movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE);
        String movieDescription = intent.getStringExtra(EXTRA_MOVIE_DESCRIPTION);
        String moviePoster = intent.getStringExtra(EXTRA_MOVIE_POSTER);
        int movieDuration = intent.getIntExtra(EXTRA_MOVIE_DURATION, 0);

        title.setText(movieTitle);
        description.setText(movieDescription);
        duration.setText(getString(R.string.movie_duration, movieDuration));

        Glide.with(this)
            .load(moviePoster)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_foreground)
            .centerCrop()
            .into(poster);

        adapter = new ShowTimeAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(ShowTimesViewModel.class);
        observeShowTimes();

        if (movieId != null) {
            viewModel.loadShowTimes(movieId);
        } else {
            emptyView.setText(getString(R.string.showtimes_error));
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    private void observeShowTimes() {
        viewModel.getShowTimes().observe(this, showTimes -> {
            adapter.submitList(showTimes);
            emptyView.setVisibility(showTimes == null || showTimes.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.isLoading().observe(this, isLoading -> {
            boolean show = isLoading != null && isLoading;
            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                emptyView.setText(getString(R.string.showtimes_error));
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setText(getString(R.string.showtimes_empty));
            }
        });
    }
}
