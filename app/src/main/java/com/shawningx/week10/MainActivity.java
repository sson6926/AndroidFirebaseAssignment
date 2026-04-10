package com.shawningx.week10;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.shawningx.week10.fcm.FcmTokenRepository;
import com.shawningx.week10.ui.auth.LoginActivity;
import com.shawningx.week10.ui.home.HomeFragment;
import com.shawningx.week10.ui.profile.ProfileFragment;
import com.shawningx.week10.ui.tickets.MyTicketsFragment;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_NOTIFICATIONS = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new HomeFragment())
                    .commit();
                return true;
            }
            if (item.getItemId() == R.id.nav_tickets) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new MyTicketsFragment())
                    .commit();
                return true;
            }
            if (item.getItemId() == R.id.nav_profile) {
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, new ProfileFragment())
                    .commit();
                return true;
            }
            return false;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }

        requestNotificationPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        new FcmTokenRepository().refreshAndSaveToken();
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        ActivityCompat.requestPermissions(
            this,
            new String[] { Manifest.permission.POST_NOTIFICATIONS },
            REQUEST_NOTIFICATIONS
        );
    }
}