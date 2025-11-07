package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.ItemRequest;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.ViewModel.MainViewModel;
import vn.edu.fpt.coffeeshop.databinding.ActivityDashboardItemFormBinding;
import vn.edu.fpt.coffeeshop.databinding.ActivityDetailBinding;

public class DashboardItemFormActivity extends AppCompatActivity {

    private EditText inputTitle, inputDescription, inputPrice, inputRating, inputExtra;
    private ImageView imageProduct;
    private Button btnSave;

    private ActivityDashboardItemFormBinding binding;
    private ItemsModel item;
    private MainViewModel viewModel = new MainViewModel();

    private boolean isCreateMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardItemFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        item = (ItemsModel) getIntent().getSerializableExtra("object");

        imageProduct = binding.imageProduct;
        inputTitle = binding.inputTitle;
        inputDescription = binding.inputDescription;
        inputPrice = binding.inputPrice;
        inputRating = binding.inputRating;
        inputExtra = binding.inputExtra;
        btnSave = binding.btnSave;

        isCreateMode = item.getId() != null;

        bundle();
    }

    private void bundle() {

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
                ItemRequest request = new ItemRequest(inputTitle.getText().toString(), inputDescription.getText().toString(), item.getPicUrl(), price, inputExtra.getText().toString(), "0");
                viewModel.createItem(DashboardItemFormActivity.this, request);
            });
        } else {
            btnSave.setOnClickListener(v -> {
                String priceText = inputPrice.getText().toString().replace("$", "").trim();
                double price = Double.parseDouble(priceText);
                ItemRequest request = new ItemRequest(inputTitle.getText().toString(), inputDescription.getText().toString(), item.getPicUrl(), price, inputExtra.getText().toString(), "0");
                viewModel.updateItem(DashboardItemFormActivity.this, item.getId(), request);
            });
        }
    }
}