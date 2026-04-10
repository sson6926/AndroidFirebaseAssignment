package com.shawningx.week10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shawningx.week10.data.ShowTimesRepository;
import com.shawningx.week10.model.ShowTime;

import java.util.Collections;
import java.util.List;

public class ShowTimesViewModel extends ViewModel {
    private final ShowTimesRepository repository;
    private final MutableLiveData<List<ShowTime>> showTimes;
    private final MutableLiveData<Boolean> loading;
    private final MutableLiveData<String> error;

    public ShowTimesViewModel() {
        this.repository = new ShowTimesRepository();
        this.showTimes = new MutableLiveData<>(Collections.emptyList());
        this.loading = new MutableLiveData<>(false);
        this.error = new MutableLiveData<>(null);
    }

    public LiveData<List<ShowTime>> getShowTimes() {
        return showTimes;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadShowTimes(String movieId) {
        loading.setValue(true);
        error.setValue(null);

        repository.fetchShowTimes(movieId, new ShowTimesRepository.ShowTimesCallback() {
            @Override
            public void onSuccess(List<ShowTime> result) {
                showTimes.postValue(result);
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
