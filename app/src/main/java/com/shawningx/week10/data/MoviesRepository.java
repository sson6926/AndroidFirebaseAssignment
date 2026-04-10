package com.shawningx.week10.data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.shawningx.week10.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MoviesRepository {
    private final FirebaseFirestore firestore;

    public MoviesRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void fetchMovies(MoviesCallback callback) {
        firestore.collection("movies")
            .get()
            .addOnSuccessListener(snapshot -> {
                List<Movie> movies = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    Movie movie = doc.toObject(Movie.class);
                    movie.setId(doc.getId());
                    movies.add(movie);
                }
                callback.onSuccess(movies);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public interface MoviesCallback {
        void onSuccess(List<Movie> movies);

        void onError(@NonNull String message);
    }
}
