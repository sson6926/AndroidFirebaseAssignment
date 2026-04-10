package com.shawningx.week10;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class MovieTicketApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Ensure Firebase is initialized before any Firebase usage.
        FirebaseApp.initializeApp(this);
    }
}
