package com.shawningx.week10.data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.shawningx.week10.model.ShowTime;

import java.util.ArrayList;
import java.util.List;

public class ShowTimesRepository {
    private final FirebaseFirestore firestore;

    public ShowTimesRepository() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void fetchShowTimes(String movieId, ShowTimesCallback callback) {
        firestore.collection("showtimes")
            .whereEqualTo("movieId", movieId)
            .get()
            .addOnSuccessListener(snapshot -> {
                List<ShowTime> items = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    ShowTime showTime = doc.toObject(ShowTime.class);
                    showTime.setId(doc.getId());
                    items.add(showTime);
                }
                callback.onSuccess(items);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public interface ShowTimesCallback {
        void onSuccess(List<ShowTime> showTimes);

        void onError(@NonNull String message);
    }
}
