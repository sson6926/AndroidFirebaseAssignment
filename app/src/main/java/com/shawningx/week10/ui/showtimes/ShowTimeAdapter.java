package com.shawningx.week10.ui.showtimes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shawningx.week10.R;
import com.shawningx.week10.model.ShowTime;

import java.util.ArrayList;
import java.util.List;

public class ShowTimeAdapter extends RecyclerView.Adapter<ShowTimeAdapter.ShowTimeViewHolder> {
    private final List<ShowTime> items = new ArrayList<>();

    public void submitList(List<ShowTime> showTimes) {
        items.clear();
        if (showTimes != null) {
            items.addAll(showTimes);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ShowTimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_showtime, parent, false);
        return new ShowTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowTimeViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ShowTimeViewHolder extends RecyclerView.ViewHolder {
        private final TextView time;
        private final TextView theater;

        ShowTimeViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.text_time);
            theater = itemView.findViewById(R.id.text_theater);
        }

        void bind(ShowTime showTime) {
            time.setText(itemView.getContext().getString(
                R.string.showtime_label,
                showTime.getStartTime()
            ));
            theater.setText(itemView.getContext().getString(
                R.string.theater_label,
                showTime.getTheaterId()
            ));
        }
    }
}
