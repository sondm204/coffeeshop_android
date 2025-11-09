package vn.edu.fpt.coffeeshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.fpt.coffeeshop.Activity.DetailActivity;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.databinding.ViewholderItemListBinding;

public class ItemListCategoryAdapter extends RecyclerView.Adapter<ItemListCategoryAdapter.Viewholder> {
    private List<ItemsModel> items;

    public ItemListCategoryAdapter(List items) {
        this.items = items;
    }

    private Context context;

    public static class Viewholder extends RecyclerView.ViewHolder {
        private ViewholderItemListBinding binding;
        public Viewholder(ViewholderItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ItemListCategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        ViewholderItemListBinding binding = ViewholderItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemListCategoryAdapter.Viewholder holder, int position) {
        ItemsModel item = items.get(position);
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.priceTxt.setText("$" + item.getPrice().toString());
        holder.binding.subtitleTxt.setText(item.getExtra());

//        Glide.with(context)
//                .load(item.getPicUrl().get(0))
//                .into(holder.binding.pic);

        if (item.getPicUrl() != null && !item.getPicUrl().isEmpty()) {
            Glide.with(context)
                    .load(item.getPicUrl().get(0))
                    .into(holder.binding.pic);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra("object", items.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
