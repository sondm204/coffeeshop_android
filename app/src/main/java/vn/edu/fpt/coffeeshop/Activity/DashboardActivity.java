package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import vn.edu.fpt.coffeeshop.Domain.AccountResponse;
import vn.edu.fpt.coffeeshop.Helper.AuthManager;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.databinding.ActivityDashboardBinding;
import vn.edu.fpt.coffeeshop.databinding.ActivityMainBinding;

public class DashboardActivity extends AppCompatActivity {

    private ImageView logoutBtn;
    private AuthManager authManager;


    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        authManager = new AuthManager(this);

        // Kiểm tra login
        if (!authManager.isLoggedIn()) {
            navigateToLogin();
            return;
        }
        binding.accountName.setText(authManager.getUserName());
        logoutBtn = binding.logoutBtn;
        String imageUrl = authManager.getUserImage();
        Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .placeholder(R.drawable.profile)
                .into(binding.avatarUrl);

        binding.productBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DashboardItemsActivity.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v -> showLogoutConfirmation());
    }

    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("No", null)
                .show();
    }

    private void performLogout() {
        // Hiển thị loading (optional)
        Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();

        authManager.logout(success -> {
            if (success) {
                Toast.makeText(DashboardActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                navigateToLogin();
            } else {
                Toast.makeText(DashboardActivity.this, "Logout failed, but local data cleared", Toast.LENGTH_SHORT).show();
                navigateToLogin(); // Vẫn navigate về login
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // ĐÚNG: Hiển thị dialog TRƯỚC, chỉ exit khi user chọn Yes
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Do you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> finishAffinity())
                .setNegativeButton("No", null)
                .show();
        // KHÔNG gọi super.onBackPressed() ở đây
    }
}