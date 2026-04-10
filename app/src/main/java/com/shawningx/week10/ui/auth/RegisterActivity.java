package com.shawningx.week10.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.shawningx.week10.MainActivity;
import com.shawningx.week10.R;
import com.shawningx.week10.ui.common.AuthResult;
import com.shawningx.week10.viewmodel.AuthViewModel;

public class RegisterActivity extends AppCompatActivity {
    private AuthViewModel viewModel;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button registerButton;
    private TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        nameInput = findViewById(R.id.input_name);
        emailInput = findViewById(R.id.input_email);
        passwordInput = findViewById(R.id.input_password);
        registerButton = findViewById(R.id.button_register);
        loginLink = findViewById(R.id.link_login);

        registerButton.setOnClickListener(view -> attemptRegister());
        loginLink.setOnClickListener(view -> finish());

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

    private void attemptRegister() {
        String name = getTextOrEmpty(nameInput);
        String email = getTextOrEmpty(emailInput);
        String password = getTextOrEmpty(passwordInput);

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.register(name, email, password);
    }

    private String getTextOrEmpty(TextInputEditText input) {
        if (input.getText() == null) {
            return "";
        }
        return input.getText().toString().trim();
    }

    private void setLoading(boolean isLoading) {
        registerButton.setEnabled(!isLoading);
        loginLink.setEnabled(!isLoading);
    }
}
