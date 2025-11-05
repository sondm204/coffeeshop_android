package vn.edu.fpt.coffeeshop.Api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import vn.edu.fpt.coffeeshop.Domain.CategoryModel;

public interface ApiService {
    @GET("/api/categories")
    Call<List<CategoryModel>> getCategories();
}
