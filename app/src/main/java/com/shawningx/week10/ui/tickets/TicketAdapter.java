package com.shawningx.week10.ui.tickets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawningx.week10.R;
import com.shawningx.week10.model.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    private final List<Ticket> items = new ArrayList<>();

    public void submitList(List<Ticket> tickets) {
        items.clear();
        if (tickets != null) {
            items.addAll(tickets);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_ticket, parent, false);
        return new TicketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {
        private final TextView seat;
        private final TextView showtime;

        TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            seat = itemView.findViewById(R.id.text_ticket_seat);
            showtime = itemView.findViewById(R.id.text_ticket_showtime);
        }

        void bind(Ticket ticket) {
            seat.setText(itemView.getContext().getString(
                R.string.ticket_seat_label,
                ticket.getSeatNumber()
            ));
            showtime.setText(itemView.getContext().getString(
                R.string.ticket_showtime_label,
                ticket.getShowtimeId()
            ));
        }
    }
}
