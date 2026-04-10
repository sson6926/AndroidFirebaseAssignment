package com.shawningx.week10;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shawningx.week10.ui.auth.LoginActivity;
import com.shawningx.week10.ui.movies.MovieAdapter;
import com.shawningx.week10.ui.movies.MovieDetailActivity;
import com.shawningx.week10.viewmodel.MoviesViewModel;

public class MainActivity extends AppCompatActivity {
    private MoviesViewModel moviesViewModel;
    private MovieAdapter adapter;
    private ProgressBar loadingView;
    private TextView emptyView;
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userEmail = findViewById(R.id.text_user_email);
        Button logoutButton = findViewById(R.id.button_logout);
        RecyclerView recyclerView = findViewById(R.id.recycler_movies);
        loadingView = findViewById(R.id.progress_loading);
        emptyView = findViewById(R.id.text_empty);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String email = currentUser == null ? "" : currentUser.getEmail();
        userEmail.setText(getString(R.string.home_user_email, email));

        adapter = new MovieAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnMovieClickListener(movie -> {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movie.getId());
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_TITLE, movie.getTitle());
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DESCRIPTION, movie.getDescription());
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_POSTER, movie.getPosterUrl());
            intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_DURATION, movie.getDuration());
            startActivity(intent);
        });

        moviesViewModel = new ViewModelProvider(this).get(MoviesViewModel.class);
        observeMovies();
        moviesViewModel.loadMovies();

        logoutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void observeMovies() {
        moviesViewModel.getMovies().observe(this, movies -> {
            adapter.submitList(movies);
            emptyView.setVisibility(movies == null || movies.isEmpty() ? View.VISIBLE : View.GONE);
        });

        moviesViewModel.isLoading().observe(this, isLoading -> {
            boolean show = isLoading != null && isLoading;
            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        });

        moviesViewModel.getError().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                emptyView.setText(getString(R.string.movies_error));
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setText(getString(R.string.movies_empty));
            }
        });
    }
}