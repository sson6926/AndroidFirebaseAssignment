package com.shawningx.week10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shawningx.week10.data.TicketsRepository;
import com.shawningx.week10.model.Ticket;

import java.util.Collections;
import java.util.List;

public class TicketsViewModel extends ViewModel {
    private final TicketsRepository repository;
    private final MutableLiveData<List<Ticket>> tickets;
    private final MutableLiveData<Boolean> loading;
    private final MutableLiveData<String> error;

    public TicketsViewModel() {
        this.repository = new TicketsRepository();
        this.tickets = new MutableLiveData<>(Collections.emptyList());
        this.loading = new MutableLiveData<>(false);
        this.error = new MutableLiveData<>(null);
    }

    public LiveData<List<Ticket>> getTickets() {
        return tickets;
    }

    public LiveData<Boolean> isLoading() {
        return loading;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void loadTickets() {
        loading.setValue(true);
        error.setValue(null);

        repository.fetchMyTickets(new TicketsRepository.TicketsCallback() {
            @Override
            public void onSuccess(List<Ticket> result) {
                tickets.postValue(result);
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
