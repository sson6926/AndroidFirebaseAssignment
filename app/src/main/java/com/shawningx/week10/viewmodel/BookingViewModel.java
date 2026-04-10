package com.shawningx.week10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shawningx.week10.data.TicketsRepository;
import com.shawningx.week10.ui.common.BookingResult;

public class BookingViewModel extends ViewModel {
    private final TicketsRepository repository;
    private final MutableLiveData<BookingResult> bookingState;

    public BookingViewModel() {
        this.repository = new TicketsRepository();
        this.bookingState = new MutableLiveData<>(BookingResult.idle());
    }

    public LiveData<BookingResult> getBookingState() {
        return bookingState;
    }

    public void bookTicket(String showtimeId, String seatNumber) {
        bookingState.setValue(BookingResult.loading());
        repository.createTicket(showtimeId, seatNumber, new TicketsRepository.TicketCallback() {
            @Override
            public void onSuccess() {
                bookingState.postValue(BookingResult.success());
            }

            @Override
            public void onError(String message) {
                bookingState.postValue(BookingResult.error(message));
            }
        });
    }

    public void bookTickets(String showtimeId, java.util.List<String> seatNumbers) {
        bookingState.setValue(BookingResult.loading());
        repository.createTickets(showtimeId, seatNumbers, new TicketsRepository.TicketCallback() {
            @Override
            public void onSuccess() {
                bookingState.postValue(BookingResult.success());
            }

            @Override
            public void onError(String message) {
                bookingState.postValue(BookingResult.error(message));
            }
        });
    }

    public void resetState() {
        bookingState.setValue(BookingResult.idle());
    }
}
