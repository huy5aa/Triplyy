package com.example.triply;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.triply.util.TokenManager;
import com.google.android.material.textview.MaterialTextView;

public class ProfileActivity extends AppCompatActivity {

    private TokenManager tokenManager;
    private MaterialTextView profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        tokenManager = new TokenManager(this);
        profileName = findViewById(R.id.profile_name);
        
        loadUserInfo();
        setupMenuClicks();
        setupBottomNavigation();
    }
    
    private void loadUserInfo() {
        String fullName = tokenManager.getFullName();
        String userName = tokenManager.getUserName();
        String email = tokenManager.getUserEmail();
        
        if (fullName != null && !fullName.isEmpty()) {
            profileName.setText(fullName);
        } else if (userName != null && !userName.isEmpty()) {
            profileName.setText(userName);
        } else if (email != null && !email.isEmpty()) {
            profileName.setText(email);
        } else {
            profileName.setText("Guest");
        }
    }
    
    private void setupMenuClicks() {
        LinearLayout menuPersonalInfo = findViewById(R.id.menu_personal_info);
        LinearLayout menuSavedPlans = findViewById(R.id.menu_saved_plans);
        LinearLayout menuSettings = findViewById(R.id.menu_settings);
        LinearLayout menuLogout = findViewById(R.id.menu_logout);
        
        menuPersonalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, PersonalInformationActivity.class);
            startActivity(intent);
        });
        
        menuSavedPlans.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, SavedPlansActivity.class);
            startActivity(intent);
        });
        
        menuSettings.setOnClickListener(v -> {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
        });
        
        menuLogout.setOnClickListener(v -> {
            TokenManager tokenManager = new TokenManager(this);
            tokenManager.clear();
            
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
    
    private void setupBottomNavigation() {
        ImageView navHome = findViewById(R.id.nav_home);
        ImageView navPlan = findViewById(R.id.nav_plan);
        ImageView navFavorite = findViewById(R.id.nav_favorite);
        ImageView navProfile = findViewById(R.id.nav_profile);
        
        navHome.setSelected(false);
        navPlan.setSelected(false);
        navFavorite.setSelected(false);
        navProfile.setSelected(true);
        
        navHome.setOnClickListener(v -> {
            finish();
        });
        
        navPlan.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, ScheduleActivity.class);
            startActivity(intent);
        });
        
        navFavorite.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
        
        navProfile.setOnClickListener(v -> {
        });
    }
}