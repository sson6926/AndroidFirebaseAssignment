package com.shawningx.week10.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawningx.week10.R;

import java.util.ArrayList;
import java.util.List;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private final List<String> seats = new ArrayList<>();
    private int selectedPosition = -1;
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatSelected(String seat);
    }

    public void setOnSeatClickListener(OnSeatClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<String> newSeats) {
        seats.clear();
        if (newSeats != null) {
            seats.addAll(newSeats);
        }
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public String getSelectedSeat() {
        if (selectedPosition < 0 || selectedPosition >= seats.size()) {
            return null;
        }
        return seats.get(selectedPosition);
    }

    @NonNull
    @Override
    public SeatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_seat, parent, false);
        return new SeatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SeatViewHolder holder, int position) {
        String seat = seats.get(position);
        holder.bind(seat, position == selectedPosition);
        holder.itemView.setOnClickListener(view -> {
            int previous = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            if (previous >= 0) {
                notifyItemChanged(previous);
            }
            notifyItemChanged(selectedPosition);
            if (listener != null) {
                listener.onSeatSelected(seat);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seats.size();
    }

    static class SeatViewHolder extends RecyclerView.ViewHolder {
        private final TextView label;

        SeatViewHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.text_seat);
        }

        void bind(String seat, boolean selected) {
            label.setText(seat);
            int background = selected ? R.drawable.bg_seat_selected : R.drawable.bg_seat_default;
            label.setBackgroundResource(background);
        }
    }
}
