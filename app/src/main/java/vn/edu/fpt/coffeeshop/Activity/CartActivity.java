package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import vn.edu.fpt.coffeeshop.Adapter.CartAdapter;
import vn.edu.fpt.coffeeshop.Helper.ChangeNumberItemsListener;
import vn.edu.fpt.coffeeshop.Helper.ManagementCart;
import vn.edu.fpt.coffeeshop.R;
import vn.edu.fpt.coffeeshop.databinding.ActivityCartBinding;

public class CartActivity extends AppCompatActivity {

    private ActivityCartBinding binding;
    private ManagementCart managementCart;
    private Double tax = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        managementCart = new ManagementCart(this);

        calculateCart();
        setVariable();
        initCartList();
    }

    private void initCartList() {
        binding.listView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );

        binding.listView.setAdapter(new CartAdapter(
                managementCart.getListCart(),
                this,
                new ChangeNumberItemsListener() {
                    @Override
                    public void onChanged() {
                        calculateCart();
                    }
                }
        ));
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> finish());
    }
    private void calculateCart() {
        Double percentTax = 0.02;
        int delivery = 15;
        tax = ((managementCart.getTotalFee()*percentTax)*100)/100.0;
        long total = Math.round((managementCart.getTotalFee() + tax + delivery) * 100) / 100;
        Double itemtotal = (managementCart.getTotalFee() * 100) / 100;
        binding.totalFeeTxt.setText("$" + itemtotal);
        binding.totalTaxTxt.setText("$" + tax);
        binding.deliveryTxt.setText("$" + delivery);
        binding.totalTxt.setText("$" + total);
    }
}