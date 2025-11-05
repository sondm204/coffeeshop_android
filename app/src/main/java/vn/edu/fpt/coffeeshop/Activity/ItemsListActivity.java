package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import vn.edu.fpt.coffeeshop.Adapter.ItemListCategoryAdapter;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.ViewModel.MainViewModel;
import vn.edu.fpt.coffeeshop.databinding.ActivityItemsListBinding;

public class ItemsListActivity extends AppCompatActivity {

    private ActivityItemsListBinding binding;
    private MainViewModel viewModel = new MainViewModel();
    private String id = "";
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        getBundles();
        initList();
    }


    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.loadItems(id).observe(this, items -> {
            binding.listView.setLayoutManager(new GridLayoutManager(this, 2));
            binding.listView.setAdapter(new ItemListCategoryAdapter(items));
            binding.progressBar.setVisibility(View.GONE);
        });
        binding.backBtn.setOnClickListener(v -> finish());
    }
    private void getBundles() {
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        binding.categoryTxt.setText(title);
    }
}