package com.example.triply.data.repository;

import android.content.Context;

import com.example.triply.data.remote.ApiService;
import com.example.triply.data.remote.RetrofitClient;
import com.example.triply.data.remote.model.AuthResponse;
import com.example.triply.data.remote.model.UpdateUserRequest;
import com.example.triply.util.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private final ApiService apiService;
    private final TokenManager tokenManager;

    public interface UpdateProfileCallback {
        void onSuccess(AuthResponse response);
        void onError(String message);
    }

    public UserRepository(Context context) {
        this.apiService = RetrofitClient.api();
        this.tokenManager = new TokenManager(context.getApplicationContext());
    }

    public void updateProfile(String fullName, String email, String phone, String address, UpdateProfileCallback callback) {
        String bearerToken = getBearerToken();
        if (bearerToken == null) {
            callback.onError("Not authenticated");
            return;
        }

        UpdateUserRequest request = new UpdateUserRequest(fullName, email, phone, address);
        apiService.updateProfile(bearerToken, request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    // Update local storage with new info
                    tokenManager.saveUserInfo(authResponse.getFullName(), authResponse.getUserName(),
                            authResponse.getEmail(), authResponse.getPhone(), authResponse.getAddress());
                    callback.onSuccess(authResponse);
                } else {
                    callback.onError("Update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                callback.onError(t.getMessage() != null ? t.getMessage() : "Network error");
            }
        });
    }

    private String getBearerToken() {
        String token = tokenManager.getAccessToken();
        return token == null ? null : "Bearer " + token;
    }
}
