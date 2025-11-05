package vn.edu.fpt.coffeeshop.Activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.fpt.coffeeshop.Adapter.CategoryAdapter;
import vn.edu.fpt.coffeeshop.Adapter.PopularAdapter;
import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.ViewModel.MainViewModel;
import vn.edu.fpt.coffeeshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        initBottomMenu();
    }

    private void initBottomMenu() {
        binding.cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });
    }

    private void initPopular() {
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        viewModel.loadPopular().observeForever(popularList -> {
            binding.recyclerViewPopular.setLayoutManager(
                    new GridLayoutManager(MainActivity.this, 2)
            );
            binding.recyclerViewPopular.setAdapter(new PopularAdapter(popularList));
            binding.progressBarPopular.setVisibility(View.GONE);
        });
        viewModel.loadPopular();
    }

    private void initCategory() {
        binding.progressBarCategory.setVisibility(View.VISIBLE);
        viewModel.loadCategory().observeForever(categories -> {
            binding.categoryView.setLayoutManager(
                    new LinearLayoutManager(
                            MainActivity.this,
                            LinearLayoutManager.HORIZONTAL,
                            false
                    )
            );
            binding.categoryView.setAdapter(new CategoryAdapter(categories));
            binding.progressBarCategory.setVisibility(View.GONE);
        });
    }

    private void initBanner() {
        binding.progressBarBanner.setVisibility(View.VISIBLE);

        viewModel.loadBanner().observeForever(new Observer<List<BannerModel>>() {
            @Override
            public void onChanged(List<BannerModel> banners) {
                if (banners != null && !banners.isEmpty()) {
                    BannerModel banner = banners.get(0);

                    Glide.with(MainActivity.this)
                            .load(banner.getUrl())
                            .into(binding.banner);

                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }
        });
    }
}