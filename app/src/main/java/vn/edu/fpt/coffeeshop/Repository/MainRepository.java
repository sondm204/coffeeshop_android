package vn.edu.fpt.coffeeshop.Repository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import vn.edu.fpt.coffeeshop.Activity.DashboardItemFormActivity;
import vn.edu.fpt.coffeeshop.Activity.LoginActivity;
import vn.edu.fpt.coffeeshop.Api.ApiClient;
import vn.edu.fpt.coffeeshop.Api.ApiService;
import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
import vn.edu.fpt.coffeeshop.Domain.ItemRequest;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;

public class MainRepository {

    private final ApiService apiService;

    public MainRepository() {
        apiService = ApiClient.getClient().create(ApiService.class);
    }

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(
            "https://sondm-coffeeshop-default-rtdb.asia-southeast1.firebasedatabase.app/"
    );

//    public LiveData<List<BannerModel>> loadBanner() {
//        MutableLiveData<List<BannerModel>> listData = new MutableLiveData<>();
//        DatabaseReference ref = firebaseDatabase.getReference("Banner");
//
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<BannerModel> list = new ArrayList<>();
//                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                    BannerModel item = childSnapshot.getValue(BannerModel.class);
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
//public LiveData<List<ItemsModel>> loadPopular() {
//    MutableLiveData<List<ItemsModel>> listData = new MutableLiveData<>();
//    DatabaseReference ref = firebaseDatabase.getReference("Popular");
//
//    ref.addValueEventListener(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            List<ItemsModel> list = new ArrayList<>();
//            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                ItemsModel item = childSnapshot.getValue(ItemsModel.class);
//                if (item != null) {
//                    list.add(item);
//                }
//            }
//            listData.setValue(list);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//            // Xử lý lỗi (nếu cần)
//        }
//    });
//
//    return listData;
//}
//public LiveData<List<ItemsModel>> loadItemCategory(String categoryId) {
//    MutableLiveData<List<ItemsModel>> itemsLiveData = new MutableLiveData<>();
//    DatabaseReference ref = firebaseDatabase.getReference("Items");
//    Query query = ref.orderByChild("categoryId").equalTo(categoryId);
//
//    query.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(@NonNull DataSnapshot snapshot) {
//            List<ItemsModel> list = new ArrayList<>();
//
//            for (DataSnapshot childSnapshot : snapshot.getChildren()) {
//                ItemsModel item = childSnapshot.getValue(ItemsModel.class);
//                if (item != null) {
//                    list.add(item);
//                }
//            }
//
//            itemsLiveData.setValue(list);
//        }
//
//        @Override
//        public void onCancelled(@NonNull DatabaseError error) {
//
//        }
//    });
//    return itemsLiveData;
//}

    public LiveData<List<BannerModel>> loadBanner() {
        MutableLiveData<List<BannerModel>> listData = new MutableLiveData<>();

        Call<List<BannerModel>> call = apiService.getBanners();

        call.enqueue(new Callback<List<BannerModel>>() {
            @Override
            public void onResponse(Call<List<BannerModel>> call, Response<List<BannerModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listData.setValue(response.body());
                } else {
                    listData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<BannerModel>> call, Throwable t) {
                listData.setValue(new ArrayList<>());
            }
        });
        return listData;
    }

    public LiveData<List<CategoryModel>> loadCategory() {
        MutableLiveData<List<CategoryModel>> listData = new MutableLiveData<>();

        Call<List<CategoryModel>> call = apiService.getCategories();

        call.enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
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
        Call<List<ItemsModel>> call = apiService.getPopulars();

        call.enqueue(new Callback<List<ItemsModel>>() {
            @Override
            public void onResponse(Call<List<ItemsModel>> call, Response<List<ItemsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listData.setValue(response.body());
                } else {
                    listData.setValue(new ArrayList<>());
                }

            }

            @Override
            public void onFailure(Call<List<ItemsModel>> call, Throwable t) {
                listData.setValue(new ArrayList<>());
            }
        });

        return listData;
    }

    public LiveData<List<ItemsModel>> loadItemCategory(String categoryId) {
        MutableLiveData<List<ItemsModel>> itemsLiveData = new MutableLiveData<>();
        Call<List<ItemsModel>> call = apiService.getProductByCategory(categoryId);

        call.enqueue(new Callback<List<ItemsModel>>() {
            @Override
            public void onResponse(Call<List<ItemsModel>> call, Response<List<ItemsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemsLiveData.setValue(response.body());
                } else {
                    itemsLiveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ItemsModel>> call, Throwable t) {
                itemsLiveData.setValue(new ArrayList<>());
            }
        });
        return itemsLiveData;
    }

    public LiveData<List<ItemsModel>> loadAllItems() {
        MutableLiveData<List<ItemsModel>> itemsLiveData = new MutableLiveData<>();
        Call<List<ItemsModel>> call = apiService.getAllProducts();

        call.enqueue(new Callback<List<ItemsModel>>() {
            @Override
            public void onResponse(Call<List<ItemsModel>> call, Response<List<ItemsModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    itemsLiveData.setValue(response.body());
                } else {
                    itemsLiveData.setValue(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(Call<List<ItemsModel>> call, Throwable t) {
                itemsLiveData.setValue(new ArrayList<>());
            }
        });
        return itemsLiveData;
    }

    public LiveData<ItemsModel> updateItem(Context context, String id, ItemRequest request) {
        MutableLiveData<ItemsModel> updateResult = new MutableLiveData<>();
        Call<ItemsModel> call = apiService.updateProduct(id, request);

        call.enqueue(new Callback<ItemsModel>() {
            @Override
            public void onResponse(Call<ItemsModel> call, Response<ItemsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateResult.postValue(response.body());
                    Toast.makeText(context, "Update successfully", Toast.LENGTH_SHORT).show();
                } else {
                    updateResult.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ItemsModel> call, Throwable t) {
                updateResult.postValue(null);
            }
        });
        return updateResult;
    }

    public LiveData<ItemsModel> createItem(Context context, ItemRequest request) {
        MutableLiveData<ItemsModel> updateResult = new MutableLiveData<>();
        Call<ItemsModel> call = apiService.createProduct(request);

        call.enqueue(new Callback<ItemsModel>() {
            @Override
            public void onResponse(Call<ItemsModel> call, Response<ItemsModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateResult.postValue(response.body());
                    Toast.makeText(context, "Created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    updateResult.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<ItemsModel> call, Throwable t) {
                updateResult.postValue(null);
            }
        });
        return updateResult;
    }

}
