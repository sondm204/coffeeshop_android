package vn.edu.fpt.coffeeshop.Activity;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.coffeeshop.Adapter.BannerAdapter;
import vn.edu.fpt.coffeeshop.Adapter.CategoryAdapter;
import vn.edu.fpt.coffeeshop.Adapter.PopularAdapter;
import vn.edu.fpt.coffeeshop.Api.ApiClient;
import vn.edu.fpt.coffeeshop.Api.ApiService;
import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.ViewModel.MainViewModel;
import vn.edu.fpt.coffeeshop.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MainViewModel viewModel = new MainViewModel();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiService = ApiClient.getClient().create(ApiService.class);

        initBanner();
        initCategory();
        initPopular();
        initBottomMenu();
    }

    private void initBottomMenu() {
        binding.cartBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        binding.chatbotBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, ChatbotActivity.class));
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
                    List<String> bannersUrl = banners.stream().map(BannerModel::getUrl).collect(Collectors.toList());
                    BannerAdapter adapter = new BannerAdapter(MainActivity.this, bannersUrl);
                    binding.bannerSlider.setAdapter(adapter);

                    final Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            int current = binding.bannerSlider.getCurrentItem();
                            int next = (current + 1) % banners.size();
                            binding.bannerSlider.setCurrentItem(next, true);
                            handler.postDelayed(this, 3000);
                        }
                    };
                    handler.postDelayed(runnable, 3000);

                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }
        });
    }

}