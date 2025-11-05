package vn.edu.fpt.coffeeshop.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
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
}
