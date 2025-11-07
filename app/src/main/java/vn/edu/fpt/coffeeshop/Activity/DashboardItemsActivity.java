package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import vn.edu.fpt.coffeeshop.Adapter.DashboardItemsAdapter;
import vn.edu.fpt.coffeeshop.Adapter.ItemListCategoryAdapter;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.ViewModel.MainViewModel;
import vn.edu.fpt.coffeeshop.databinding.ActivityDashboardItemsBinding;
import vn.edu.fpt.coffeeshop.databinding.ActivityItemsListBinding;

public class DashboardItemsActivity extends AppCompatActivity {

    private ActivityDashboardItemsBinding binding;
    private MainViewModel viewModel = new MainViewModel();
    private String id = "";
    private String title = "";

    @Override
    protected void onResume() {
        super.onResume();
        getBundles();
        initList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getBundles();
        initList();

        binding.addBtn.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardItemsActivity.this, DashboardItemFormActivity.class);
            ItemsModel newItem = new ItemsModel();
            intent.putExtra("object", newItem);
            startActivity(intent);
        });
    }

    private void initList() {
        binding.progressBar.setVisibility(View.VISIBLE);

        viewModel.loadAllItems().observe(this, items -> {
            binding.listView.setLayoutManager(new GridLayoutManager(this, 2));
            binding.listView.setAdapter(new DashboardItemsAdapter(items));
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