package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.bumptech.glide.Glide;

import vn.edu.fpt.coffeeshop.Domain.AccountResponse;
import vn.edu.fpt.coffeeshop.Helper.TinyDB;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.databinding.ActivityDashboardBinding;
import vn.edu.fpt.coffeeshop.databinding.ActivityMainBinding;

public class DashboardActivity extends AppCompatActivity {

    private TinyDB tinyDB;

    private ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        tinyDB = new TinyDB(this);
        setContentView(binding.getRoot());
        AccountResponse account = tinyDB.getObject("current_account", AccountResponse.class);
        binding.accountName.setText(account.getName());
        String imageUrl = account.getImageUrl();
        Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .placeholder(R.drawable.profile)
                .into(binding.avatarUrl);

        binding.productBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, DashboardItemsActivity.class);
            startActivity(intent);
        });
    }
}