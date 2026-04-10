package com.shawningx.week10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shawningx.week10.data.MoviesRepository;
import com.shawningx.week10.model.Movie;

import java.util.Collections;
import java.util.List;

public class MoviesViewModel extends ViewModel {
    private final MoviesRepository repository;
    private final MutableLiveData<List<Movie>> movies;
    private final MutableLiveData<Boolean> loading;
    private final MutableLiveData<String> error;

    public MoviesViewModel() {
        this.repository = new MoviesRepository();
        this.movies = new MutableLiveData<>(Collections.emptyList());
        this.loading = new MutableLiveData<>(false);
        this.error = new MutableLiveData<>(null);
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadMovies() {
        loading.setValue(true);
        error.setValue(null);

        repository.fetchMovies(new MoviesRepository.MoviesCallback() {
            @Override
            public void onSuccess(List<Movie> result) {
                movies.postValue(result);
                loading.postValue(false);
            }

            @Override
            public void onError(String message) {
                error.postValue(message);
                loading.postValue(false);
            }
        });
    }
}
