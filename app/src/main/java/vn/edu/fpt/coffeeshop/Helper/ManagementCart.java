package vn.edu.fpt.coffeeshop.Helper;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.fpt.coffeeshop.Domain.ItemsModel;

public class ManagementCart {

    private TinyDB tinyDB;
    private Context context;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    // ✅ Thêm sản phẩm vào giỏ hàng
    public void insertItems(ItemsModel item) {
        ArrayList<ItemsModel> listItem = getListCart();

        boolean existAlready = false;
        int index = -1;

        for (int i = 0; i < listItem.size(); i++) {
            if (listItem.get(i).getTitle().equals(item.getTitle())) {
                existAlready = true;
                index = i;
                break;
            }
        }

        if (existAlready) {
            listItem.get(index).setNumberInCart(item.getNumberInCart());
        } else {
            listItem.add(item);
        }

        tinyDB.putListObject("CartList", listItem);
        Toast.makeText(context, "Added to your Cart", Toast.LENGTH_SHORT).show();
    }

    // ✅ Lấy danh sách sản phẩm trong giỏ hàng
    public ArrayList<ItemsModel> getListCart() {
        ArrayList<ItemsModel> list = tinyDB.getListObject("CartList");
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    // ✅ Giảm số lượng sản phẩm
    public void minusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        if (listItems.get(position).getNumberInCart() == 1) {
            listItems.remove(position);
        } else {
            listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() - 1);
        }

        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    // ✅ Xóa sản phẩm khỏi giỏ hàng
    public void removeItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        listItems.remove(position);
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    // ✅ Tăng số lượng sản phẩm
    public void plusItem(ArrayList<ItemsModel> listItems, int position, ChangeNumberItemsListener listener) {
        listItems.get(position).setNumberInCart(listItems.get(position).getNumberInCart() + 1);
        tinyDB.putListObject("CartList", listItems);
        listener.onChanged();
    }

    // ✅ Tính tổng tiền
    public double getTotalFee() {
        ArrayList<ItemsModel> listItem = getListCart();
        double fee = 0.0;

        for (ItemsModel item : listItem) {
            fee += item.getPrice() * item.getNumberInCart();
        }
        return fee;
    }
}
