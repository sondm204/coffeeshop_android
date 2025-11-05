package vn.edu.fpt.coffeeshop.Repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.coffeeshop.Api.ApiClient;
import vn.edu.fpt.coffeeshop.Api.ApiService;
import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;

public class MainRepository {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(
            "https://sondm-coffeeshop-default-rtdb.asia-southeast1.firebasedatabase.app/"
    );

    public LiveData<List<BannerModel>> loadBanner() {
        MutableLiveData<List<BannerModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Banner");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<BannerModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    BannerModel item = childSnapshot.getValue(BannerModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu cần)
            }
        });

        return listData;
    }

//    public LiveData<List<CategoryModel>> loadCategory() {
//        MutableLiveData<List<CategoryModel>> listData = new MutableLiveData<>();
//        DatabaseReference ref = firebaseDatabase.getReference("Category");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<CategoryModel> list = new ArrayList<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    CategoryModel item = childSnapshot.getValue(CategoryModel.class);
//                    if (item != null) {
//                        list.add(item);
//                    }
//                }
//                listData.setValue(list);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Xử lý lỗi (nếu cần)
//            }
//        });
//
//        return listData;
//    }

    public LiveData<List<CategoryModel>> loadCategory() {
        MutableLiveData<List<CategoryModel>> listData = new MutableLiveData<>();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<CategoryModel>> call = apiService.getCategories();

        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    listData.setValue(response.body());
                } else {
                  listData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {
                Log.e("API_FAILURE", "Error: " + t.getMessage(), t);
                listData.setValue(new ArrayList<>());
            }
        });
        return listData;
    }

    public LiveData<List<ItemsModel>> loadPopular() {
        MutableLiveData<List<ItemsModel>> listData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Popular");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }
                listData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi (nếu cần)
            }
        });

        return listData;
    }

    public LiveData<List<ItemsModel>> loadItemCategory(String categoryId) {
        MutableLiveData<List<ItemsModel>> itemsLiveData = new MutableLiveData<>();
        DatabaseReference ref = firebaseDatabase.getReference("Items");
        Query query = ref.orderByChild("categoryId").equalTo(categoryId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<ItemsModel> list = new ArrayList<>();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    ItemsModel item = childSnapshot.getValue(ItemsModel.class);
                    if (item != null) {
                        list.add(item);
                    }
                }

                itemsLiveData.setValue(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return itemsLiveData;
    }
}
