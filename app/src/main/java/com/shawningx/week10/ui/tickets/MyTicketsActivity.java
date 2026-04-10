package com.shawningx.week10.ui.tickets;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.shawningx.week10.R;
import com.shawningx.week10.ui.auth.LoginActivity;
import com.shawningx.week10.viewmodel.TicketsViewModel;

public class MyTicketsActivity extends AppCompatActivity {
    private TicketsViewModel viewModel;
    private TicketAdapter adapter;
    private ProgressBar loadingView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tickets);

        loadingView = findViewById(R.id.progress_loading);
        emptyView = findViewById(R.id.text_empty);
        RecyclerView recyclerView = findViewById(R.id.recycler_tickets);

        adapter = new TicketAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TicketsViewModel.class);
        observeTickets();
        viewModel.loadTickets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void observeTickets() {
        viewModel.getTickets().observe(this, tickets -> {
            adapter.submitList(tickets);
            emptyView.setVisibility(tickets == null || tickets.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.isLoading().observe(this, isLoading -> {
            boolean show = isLoading != null && isLoading;
            loadingView.setVisibility(show ? View.VISIBLE : View.GONE);
        });

        viewModel.getError().observe(this, message -> {
            if (message != null && !message.isEmpty()) {
                emptyView.setText(getString(R.string.tickets_error));
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setText(getString(R.string.tickets_empty));
            }
        });
    }
}
