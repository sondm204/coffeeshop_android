package vn.edu.fpt.coffeeshop.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://10.0.2.2:8080";
    private static Retrofit retrofit;
    private static AuthInterceptor authInterceptor;

    public static Retrofit getClient() {
        if (retrofit == null) {
            authInterceptor = new AuthInterceptor();

            // Tạo OkHttpClient với AuthInterceptor
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static void setAuthToken(String token) {
        if (authInterceptor != null) {
            authInterceptor.setToken(token);
        }
    }
}
