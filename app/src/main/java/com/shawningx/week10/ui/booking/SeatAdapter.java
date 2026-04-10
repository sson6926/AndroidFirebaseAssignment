package com.shawningx.week10.ui.booking;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawningx.week10.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SeatAdapter extends RecyclerView.Adapter<SeatAdapter.SeatViewHolder> {
    private final List<String> seats = new ArrayList<>();
    private final Set<Integer> selectedPositions = new HashSet<>();
    private OnSeatClickListener listener;

    public interface OnSeatClickListener {
        void onSeatSelected(List<String> seats);
    }

    public void setOnSeatClickListener(OnSeatClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<String> newSeats) {
        seats.clear();
        if (newSeats != null) {
            seats.addAll(newSeats);
        }
        selectedPositions.clear();
        notifyDataSetChanged();
    }

    public List<String> getSelectedSeats() {
        List<String> selected = new ArrayList<>();
        for (Integer position : selectedPositions) {
            if (position >= 0 && position < seats.size()) {
                selected.add(seats.get(position));
            }
        }
        return selected;
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
        holder.bind(seat, selectedPositions.contains(position));
        holder.itemView.setOnClickListener(view -> {
            int current = holder.getBindingAdapterPosition();
            if (selectedPositions.contains(current)) {
                selectedPositions.remove(current);
            } else {
                selectedPositions.add(current);
            }
            notifyItemChanged(current);
            if (listener != null) {
                listener.onSeatSelected(getSelectedSeats());
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
