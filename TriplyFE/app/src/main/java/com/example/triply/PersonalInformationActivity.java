package com.example.triply;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triply.data.remote.model.AuthResponse;
import com.example.triply.data.repository.UserRepository;
import com.example.triply.util.TokenManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class PersonalInformationActivity extends AppCompatActivity {

    private TextInputEditText etFullName, etUsername, etEmail, etPhone, etAddress;
    private MaterialButton btnSave;
    private ImageView btnBack;
    private TokenManager tokenManager;
    private UserRepository userRepository;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        tokenManager = new TokenManager(this);
        userRepository = new UserRepository(this);

        initViews();
        loadUserData();
        setupListeners();
    }

    private void initViews() {
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        btnSave = findViewById(R.id.btn_save);
        btnBack = findViewById(R.id.btn_back);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
    }

    private void loadUserData() {
        String fullName = tokenManager.getFullName();
        String userName = tokenManager.getUserName();
        String email = tokenManager.getUserEmail();
        String phone = tokenManager.getPhone();
        String address = tokenManager.getAddress();

        if (fullName != null) {
            etFullName.setText(fullName);
        }
        if (userName != null) {
            etUsername.setText(userName);
        }
        if (email != null) {
            etEmail.setText(email);
        }
        if (phone != null) {
            etPhone.setText(phone);
        }
        if (address != null) {
            etAddress.setText(address);
        }
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnSave.setOnClickListener(v -> saveUserData());
    }

    private void saveUserData() {
        String fullName = etFullName.getText().toString().trim();
        String userName = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Validate
        if (fullName.isEmpty()) {
            etFullName.setError("Full name is required");
            etFullName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email format");
            etEmail.requestFocus();
            return;
        }

        // Show progress
        progressDialog.show();

        // Call API to update profile
        userRepository.updateProfile(fullName, email, phone, address, new UserRepository.UpdateProfileCallback() {
            @Override
            public void onSuccess(AuthResponse response) {
                progressDialog.dismiss();
                
                // Also update local storage
                tokenManager.updateFullName(fullName);
                tokenManager.updateUserName(userName);
                tokenManager.updateEmail(email);
                tokenManager.savePhone(phone);
                tokenManager.saveAddress(address);
                
                Toast.makeText(PersonalInformationActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String message) {
                progressDialog.dismiss();
                Toast.makeText(PersonalInformationActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
