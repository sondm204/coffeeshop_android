package vn.edu.fpt.coffeeshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ItemsModel> productList;
    private Context context;

    public ProductAdapter(Context context, List<ItemsModel> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(vn.edu.fpt.coffeeshop.R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ItemsModel product = productList.get(position);
        holder.name.setText(product.getTitle());
        holder.price.setText(String.valueOf(product.getPrice()));
        // dùng Glide hoặc Picasso để load ảnh
        Glide.with(context)
                .load(product.getPicUrl().get(0).toString())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.product_image);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
        }
    }
}

