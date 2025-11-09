package vn.edu.fpt.coffeeshop.ViewModel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
import vn.edu.fpt.coffeeshop.Domain.ItemRequest;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.Repository.MainRepository;

public class MainViewModel extends ViewModel {
    private final MainRepository repository = new MainRepository();

    public LiveData<List<BannerModel>> loadBanner() {
        return repository.loadBanner();
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        return repository.loadCategory();
    }
    public LiveData<List<ItemsModel>> loadPopular() {
        return repository.loadPopular();
    }
    public LiveData<List<ItemsModel>> loadItems(String categoryId) {
        return repository.loadItemCategory(categoryId);
    }

    public LiveData<List<ItemsModel>> loadAllItems() {
        return repository.loadAllItems();
    }

    public LiveData<ItemsModel> updateItem(Context context, String id, ItemRequest request) {
        return repository.updateItem(context, id, request);
    }

    public LiveData<ItemsModel> createItem(Context context, ItemRequest request) {
        return repository.createItem(context, request);
    }

    public void deleteItem(Context context, String id){
        repository.deleteItem(context, id);
    }
}
