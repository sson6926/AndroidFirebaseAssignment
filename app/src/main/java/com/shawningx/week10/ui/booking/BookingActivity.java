package com.shawningx.week10.ui.booking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.shawningx.week10.R;
import com.shawningx.week10.fcm.FcmFunctionsRepository;
import com.shawningx.week10.fcm.NotificationHelper;
import com.shawningx.week10.ui.auth.LoginActivity;
import com.shawningx.week10.ui.common.BookingResult;
import com.shawningx.week10.viewmodel.BookingViewModel;

import java.util.ArrayList;
import java.util.List;

public class BookingActivity extends AppCompatActivity {
    public static final String EXTRA_SHOWTIME_ID = "extra_showtime_id";
    public static final String EXTRA_MOVIE_TITLE = "extra_movie_title";
    public static final String EXTRA_SHOWTIME_TIME = "extra_showtime_time";
    public static final String EXTRA_THEATER_ID = "extra_theater_id";

    private BookingViewModel viewModel;
    private SeatAdapter seatAdapter;
    private Button confirmButton;
    private ProgressBar loadingView;
    private TextView selectedSeatView;
    private FcmFunctionsRepository functionsRepository;
    private String movieTitle;
    private String showtimeTime;
    private List<String> lastSelectedSeats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        TextView movieTitleView = findViewById(R.id.text_movie_title);
        TextView showtimeTimeView = findViewById(R.id.text_showtime_time);
        TextView theater = findViewById(R.id.text_theater);
        selectedSeatView = findViewById(R.id.text_selected_seat);
        confirmButton = findViewById(R.id.button_confirm);
        loadingView = findViewById(R.id.progress_loading);
        RecyclerView seatRecycler = findViewById(R.id.recycler_seats);

        Intent intent = getIntent();
        String showtimeId = intent.getStringExtra(EXTRA_SHOWTIME_ID);
        movieTitle = intent.getStringExtra(EXTRA_MOVIE_TITLE);
        showtimeTime = intent.getStringExtra(EXTRA_SHOWTIME_TIME);
        String theaterId = intent.getStringExtra(EXTRA_THEATER_ID);

        movieTitleView.setText(movieTitle);
        showtimeTimeView.setText(getString(R.string.showtime_label, showtimeTime));
        theater.setText(getString(R.string.theater_label, theaterId));

        seatAdapter = new SeatAdapter();
        seatRecycler.setLayoutManager(new GridLayoutManager(this, 4));
        seatRecycler.setAdapter(seatAdapter);

        seatAdapter.submitList(buildSeatList());
        seatAdapter.setOnSeatClickListener(seats -> {
            if (seats.isEmpty()) {
                selectedSeatView.setText(getString(R.string.selected_seat_empty));
            } else {
                selectedSeatView.setText(getString(
                    R.string.selected_seat_label,
                    joinSeats(seats)
                ));
            }
        });

        viewModel = new ViewModelProvider(this).get(BookingViewModel.class);
        functionsRepository = new FcmFunctionsRepository();
        observeBooking(showtimeId);

        confirmButton.setOnClickListener(view -> {
            List<String> seats = seatAdapter.getSelectedSeats();
            if (showtimeId == null || seats.isEmpty()) {
                Toast.makeText(this, R.string.error_select_seat, Toast.LENGTH_SHORT).show();
                return;
            }
            lastSelectedSeats = new ArrayList<>(seats);
            viewModel.bookTickets(showtimeId, seats);
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

    private void observeBooking(String showtimeId) {
        if (showtimeId == null) {
            confirmButton.setEnabled(false);
            selectedSeatView.setText(getString(R.string.error_missing_showtime));
            return;
        }

        viewModel.getBookingState().observe(this, result -> {
            if (result == null) {
                return;
            }

            switch (result.getStatus()) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    Toast.makeText(this, R.string.booking_success, Toast.LENGTH_SHORT).show();
                    functionsRepository.sendBookingNotification(
                        movieTitle,
                        showtimeTime,
                        lastSelectedSeats,
                        () -> new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (isFinishing() || isDestroyed()) {
                                return;
                            }
                            NotificationHelper.showNotification(
                                this,
                                getString(R.string.notification_default_title),
                                getString(R.string.notification_default_body)
                            );
                        }, 2000)
                    );
                    viewModel.resetState();
                    finish();
                    break;
                case ERROR:
                    setLoading(false);
                    String message = result.getMessage() == null
                        ? getString(R.string.error_unknown)
                        : result.getMessage();
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    viewModel.resetState();
                    break;
                case IDLE:
                default:
                    setLoading(false);
                    break;
            }
        });
    }

    private void setLoading(boolean loading) {
        confirmButton.setEnabled(!loading);
        loadingView.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private List<String> buildSeatList() {
        List<String> seats = new ArrayList<>();
        char[] rows = new char[] { 'A', 'B', 'C', 'D' };
        for (char row : rows) {
            for (int i = 1; i <= 8; i++) {
                seats.add(row + String.valueOf(i));
            }
        }
        return seats;
    }

    private String joinSeats(List<String> seats) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < seats.size(); i++) {
            builder.append(seats.get(i));
            if (i < seats.size() - 1) {
                builder.append(", ");
            }
        }
        return builder.toString();
    }
}
