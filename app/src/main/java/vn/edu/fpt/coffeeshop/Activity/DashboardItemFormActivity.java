package vn.edu.fpt.coffeeshop.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
import vn.edu.fpt.coffeeshop.Domain.ItemRequest;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.ViewModel.MainViewModel;
import vn.edu.fpt.coffeeshop.databinding.ActivityDashboardItemFormBinding;
import vn.edu.fpt.coffeeshop.databinding.ActivityDetailBinding;

public class DashboardItemFormActivity extends AppCompatActivity {

    private EditText inputTitle, inputDescription, inputPrice, inputRating, inputExtra;
    private ImageView imageProduct;
    private Spinner spinnerCategory;
    private Button btnSave, btnDelete;

    private ActivityDashboardItemFormBinding binding;
    private ItemsModel item;
    private List<CategoryModel> categories;
    private MainViewModel viewModel = new MainViewModel();
    private CategoryModel selectedCategory = null;

    private boolean isCreateMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardItemFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        item = (ItemsModel) getIntent().getSerializableExtra("object");
        String itemCategoryId = item != null ? item.getCategoryId() : null;

        imageProduct = binding.imageProduct;
        inputTitle = binding.inputTitle;
        inputDescription = binding.inputDescription;
        inputPrice = binding.inputPrice;
        inputRating = binding.inputRating;
        inputExtra = binding.inputExtra;
        spinnerCategory = binding.spinnerCategory;
        btnSave = binding.btnSave;
        btnDelete = binding.btnDelete;

        isCreateMode = item.getId() == null || item.getId() == "";

        bundle();
    }

    private void bundle() {
        viewModel.loadCategory().observe(this, new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categories) {
                if (categories != null && !categories.isEmpty()) {
                    setupSpinner(categories);
                }
            }
        });


        if (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) {
            Glide.with(DashboardItemFormActivity.this)
                    .load(item.getPicUrl().get(0))
                    .into(binding.imageProduct);
        }

        binding.inputTitle.setText(item.getTitle());
        binding.inputDescription.setText(item.getDescription());
        binding.inputPrice.setText("$" + item.getPrice());
        binding.inputRating.setText(String.valueOf(item.getRating()));
        binding.inputExtra.setText(item.getExtra());

        binding.backBtn.setOnClickListener(v -> finish());

        if (isCreateMode) {
            btnSave.setOnClickListener(v -> {
                String priceText = inputPrice.getText().toString().replace("$", "").trim();
                double price = Double.parseDouble(priceText);
                ItemRequest request = new ItemRequest(inputTitle.getText().toString(), inputDescription.getText().toString(), item.getPicUrl(), price, inputExtra.getText().toString(), String.valueOf(selectedCategory.getId()));
                viewModel.createItem(DashboardItemFormActivity.this, request);
                Intent intent = new Intent(DashboardItemFormActivity.this, DashboardItemsActivity.class);
                startActivity(intent);
            });
            btnDelete.setVisibility(View.GONE);
        } else {
            btnSave.setOnClickListener(v -> {
                String priceText = inputPrice.getText().toString().replace("$", "").trim();
                double price = Double.parseDouble(priceText);
                ItemRequest request = new ItemRequest(inputTitle.getText().toString(), inputDescription.getText().toString(), item.getPicUrl(), price, inputExtra.getText().toString(), String.valueOf(selectedCategory.getId()));
                viewModel.updateItem(DashboardItemFormActivity.this, item.getId(), request);
            });
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                viewModel.deleteItem(DashboardItemFormActivity.this, item.getId());
                Intent intent = new Intent(DashboardItemFormActivity.this, DashboardItemsActivity.class);
                startActivity(intent);
            });
        }
    }
    private void setupSpinner(List<CategoryModel> categories) {
        ArrayAdapter<CategoryModel> spinnerAdapter = new ArrayAdapter<CategoryModel>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        ) {
            @Override
            public TextView getView(int position, @Nullable android.view.View convertView, android.view.ViewGroup parent) {
                TextView label = (TextView) super.getView(position, convertView, parent);
                label.setText(getItem(position).getTitle());
                return label;
            }

            @Override
            public TextView getDropDownView(int position, @Nullable android.view.View convertView, android.view.ViewGroup parent) {
                TextView label = (TextView) super.getDropDownView(position, convertView, parent);
                label.setText(getItem(position).getTitle());
                return label;
            }
        };

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        if (item != null && item.getCategoryId() != null) {
            String itemCategoryIdStr = item.getCategoryId();
            for (int i = 0; i < categories.size(); i++) {
                CategoryModel category = categories.get(i);
                if (String.valueOf(category.getId()).equals(itemCategoryIdStr)) {
                    spinnerCategory.setSelection(i);
                    selectedCategory = category;
                    break;
                }
            }
        }

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedCategory = (CategoryModel) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null;
            }
        });
    }
}