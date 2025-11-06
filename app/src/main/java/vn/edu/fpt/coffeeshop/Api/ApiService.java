package vn.edu.fpt.coffeeshop.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;

public interface ApiService {

    @GET("/api/banners")
    Call<List<BannerModel>> getBanners();

    @GET("/api/populars")
    Call<List<ItemsModel>> getPopulars();

    @GET("/api/categories")
    Call<List<CategoryModel>> getCategories();

    @GET("/api/categories/{id}/products")
    Call<List<ItemsModel>> getProductByCategory(@Path("id") String categoryId);
}
