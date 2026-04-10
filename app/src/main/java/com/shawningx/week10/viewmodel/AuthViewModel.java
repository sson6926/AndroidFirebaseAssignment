package com.shawningx.week10.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shawningx.week10.data.AuthRepository;
import com.shawningx.week10.ui.common.AuthResult;

public class AuthViewModel extends ViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<AuthResult> authState;

    public AuthViewModel() {
        this.repository = new AuthRepository();
        this.authState = new MutableLiveData<>(AuthResult.idle());
    }

    public LiveData<AuthResult> getAuthState() {
        return authState;
    }

    public void login(String email, String password) {
        authState.setValue(AuthResult.loading());
        repository.login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                authState.postValue(AuthResult.success());
            }

            @Override
            public void onError(String message) {
                authState.postValue(AuthResult.error(message));
            }
        });
    }

    public void register(String name, String email, String password) {
        authState.setValue(AuthResult.loading());
        repository.register(name, email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                authState.postValue(AuthResult.success());
            }

            @Override
            public void onError(String message) {
                authState.postValue(AuthResult.error(message));
            }
        });
    }

    public void resetState() {
        authState.setValue(AuthResult.idle());
    }
}
