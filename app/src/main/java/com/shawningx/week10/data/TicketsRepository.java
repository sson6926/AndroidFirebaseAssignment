package com.shawningx.week10.data;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.shawningx.week10.model.Ticket;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TicketsRepository {
    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public TicketsRepository() {
        this.auth = FirebaseAuth.getInstance();
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void createTicket(String showtimeId, String seatNumber, TicketCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError("User not logged in.");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("userId", user.getUid());
        data.put("showtimeId", showtimeId);
        data.put("seatNumber", seatNumber);
        data.put("createdAt", FieldValue.serverTimestamp());

        firestore.collection("tickets")
            .add(data)
            .addOnSuccessListener(doc -> callback.onSuccess())
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void fetchMyTickets(TicketsCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onError("User not logged in.");
            return;
        }

        firestore.collection("tickets")
            .whereEqualTo("userId", user.getUid())
            .get()
            .addOnSuccessListener(snapshot -> {
                List<Ticket> items = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshot) {
                    Ticket ticket = doc.toObject(Ticket.class);
                    ticket.setId(doc.getId());
                    items.add(ticket);
                }
                callback.onSuccess(items);
            })
            .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public interface TicketCallback {
        void onSuccess();

        void onError(@NonNull String message);
    }

    public interface TicketsCallback {
        void onSuccess(List<Ticket> tickets);

        void onError(@NonNull String message);
    }
}
