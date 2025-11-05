package vn.edu.fpt.coffeeshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.Helper.ChangeNumberItemsListener;
import vn.edu.fpt.coffeeshop.Helper.ManagementCart;
import vn.edu.fpt.coffeeshop.databinding.ViewholderCartBinding;
import vn.edu.fpt.coffeeshop.databinding.ViewholderCategoryBinding;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    private ArrayList<ItemsModel> listItemSelected;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private ManagementCart managementCart;

    public CartAdapter(ArrayList<ItemsModel> listItemSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listItemSelected = listItemSelected;
        managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    public static class Viewholder extends RecyclerView.ViewHolder {
        private ViewholderCartBinding binding;

        public Viewholder(ViewholderCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public CartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, int position) {
        ItemsModel item = listItemSelected.get(position);
        holder.binding.titleTxt.setText(item.getTitle());
        holder.binding.feeEachItem.setText("$" + item.getPrice());
        holder.binding.totalEachItem.setText("$" + item.getNumberInCart() * item.getPrice());
        holder.binding.numberInCartTxt.setText(String.valueOf(item.getNumberInCart()));

        Glide.with(holder.itemView.getContext())
                .load(item.getPicUrl().get(0))
                .apply(new RequestOptions().transform(new CenterCrop()))
                .into(holder.binding.picCart);

        holder.binding.plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.plusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.onChanged();
                        }
                    }
                });
            }
        });

        holder.binding.minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.minusItem(listItemSelected, position, new ChangeNumberItemsListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.onChanged();
                        }
                    }
                });
            }
        });

        holder.binding.removeItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                managementCart.removeItem(listItemSelected, position, new ChangeNumberItemsListener() {
                    @Override
                    public void onChanged() {
                        notifyDataSetChanged();
                        if (changeNumberItemsListener != null) {
                            changeNumberItemsListener.onChanged();
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }
}
