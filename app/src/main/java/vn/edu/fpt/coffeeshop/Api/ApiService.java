package vn.edu.fpt.coffeeshop.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.fpt.coffeeshop.Domain.AiRequest;
import vn.edu.fpt.coffeeshop.Domain.AiResponse;
import vn.edu.fpt.coffeeshop.Domain.BannerModel;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;
import vn.edu.fpt.coffeeshop.Domain.GoogleLoginRequest;
import vn.edu.fpt.coffeeshop.Domain.ItemRequest;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.Domain.LoginRequest;
import vn.edu.fpt.coffeeshop.Domain.AccountResponse;

public interface ApiService {

    @POST("/api/auth/login")
    Call<AccountResponse> login(@Body LoginRequest request);
    @POST("/api/auth/login/google")
    Call<AccountResponse> loginWithGoogle(@Body GoogleLoginRequest request);

    @GET("/api/banners")
    Call<List<BannerModel>> getBanners();

    @GET("/api/populars")
    Call<List<ItemsModel>> getPopulars();

    @GET("/api/categories")
    Call<List<CategoryModel>> getCategories();

    @GET("/api/categories/{id}/products")
    Call<List<ItemsModel>> getProductByCategory(@Path("id") String categoryId);

    @GET("/api/products")
    Call<List<ItemsModel>> getAllProducts();

    @POST("/api/products")
    Call<ItemsModel> createProduct(@Body ItemRequest request);

    @PUT("/api/products/{id}")
    Call<ItemsModel> updateProduct(@Path("id") String id, @Body ItemRequest request);

    @DELETE("/api/products/{id}")
    Call<Void> deleteProduct(@Path("id") String id);

    @POST("/api/ai/chat")
    Call<AiResponse> chat(@Body AiRequest request);
}
