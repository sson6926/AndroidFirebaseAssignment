package com.shawningx.week10.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.android.material.textfield.TextInputEditText;
import com.shawningx.week10.MainActivity;
import com.shawningx.week10.R;
import com.shawningx.week10.ui.common.AuthResult;
import com.shawningx.week10.viewmodel.AuthViewModel;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel viewModel;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button loginButton;
    private TextView registerLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        loginButton = findViewById(R.id.button_login);
        registerLink = findViewById(R.id.link_register);

        loginButton.setOnClickListener(view -> attemptLogin());
        registerLink.setOnClickListener(view -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

        viewModel.getAuthState().observe(this, authResult -> {
            if (authResult == null) {
                return;
            }

            switch (authResult.getStatus()) {
                case LOADING:
                    setLoading(true);
                    break;
                case SUCCESS:
                    setLoading(false);
                    viewModel.resetState();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                    break;
                case ERROR:
                    setLoading(false);
                    String message = authResult.getMessage() == null
                        ? getString(R.string.error_unknown)
                        : authResult.getMessage();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void attemptLogin() {
        String email = getTextOrEmpty(emailInput);
        String password = getTextOrEmpty(passwordInput);

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.login(email, password);
    }

    private String getTextOrEmpty(TextInputEditText input) {
        if (input.getText() == null) {
            return "";
        }
        return input.getText().toString().trim();
    }

    private void setLoading(boolean isLoading) {
        loginButton.setEnabled(!isLoading);
        registerLink.setEnabled(!isLoading);
    }
}
