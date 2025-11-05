package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;

import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.Helper.ManagementCart;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private ItemsModel item;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagementCart(this);
        bundle();
        initSizeList();
    }

    private void initSizeList() {
        binding.smallBtn.setOnClickListener(v -> {
            binding.smallBtn.setBackgroundResource(R.drawable.brown_storke_bg);
            binding.mediumBtn.setBackgroundResource(0);
            binding.largeBtn.setBackgroundResource(0);
        });
        binding.mediumBtn.setOnClickListener(v -> {
            binding.smallBtn.setBackgroundResource(0);
            binding.mediumBtn.setBackgroundResource(R.drawable.brown_storke_bg);
            binding.largeBtn.setBackgroundResource(0);
        });
        binding.largeBtn.setOnClickListener(v -> {
            binding.smallBtn.setBackgroundResource(0);
            binding.mediumBtn.setBackgroundResource(0);
            binding.largeBtn.setBackgroundResource(R.drawable.brown_storke_bg);
        });
    }

    private void bundle() {
        ItemsModel item = (ItemsModel) getIntent().getSerializableExtra("object");

        Glide.with(DetailActivity.this)
                .load(item.getPicUrl().get(0))
                .into(binding.picMain);

        binding.titleTxt.setText(item.getTitle());
        binding.descriptionTxt.setText(item.getDescription());
        binding.priceTxt.setText("$" + item.getPrice());
        binding.ratingTxt.setText(String.valueOf(item.getRating()));

        binding.addToCartBtn.setOnClickListener(v -> {
            int numberInCart = Integer.parseInt(binding.numberInCartTxt.getText().toString());
            item.setNumberInCart(numberInCart);
            managementCart.insertItems(item);
        });

        binding.backBtn.setOnClickListener(v -> finish());

        binding.plusBtn.setOnClickListener(v -> {
            binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart() + 1));
            item.setNumberInCart(item.getNumberInCart() + 1);
        });

        binding.minusBtn.setOnClickListener(v -> {
            if (item.getNumberInCart() > 0) {
                binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart() - 1));
                item.setNumberInCart(item.getNumberInCart() - 1);
            }
        });
    }
}