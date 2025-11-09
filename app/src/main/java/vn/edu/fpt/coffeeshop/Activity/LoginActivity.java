package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.coffeeshop.Api.ApiClient;
import vn.edu.fpt.coffeeshop.Api.ApiService;
import vn.edu.fpt.coffeeshop.Domain.LoginRequest;
import vn.edu.fpt.coffeeshop.Domain.AccountResponse;
import vn.edu.fpt.coffeeshop.Helper.TinyDB;
import vn.edu.fpt.coffeeshop.R;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput, pwInput;
    Button btnLogin;

    private TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tinyDB = new TinyDB(this);
        usernameInput = findViewById(R.id.usernameInput);
        pwInput = findViewById(R.id.pwInput);
        btnLogin = findViewById(R.id.loginBtn);

        btnLogin.setOnClickListener(v -> doLogin());

    }

    private void doLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = pwInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(username, password);
        Call<AccountResponse> call = apiService.login(request);

        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountResponse account = response.body();
                    if (account.getRole().equalsIgnoreCase("admin") || account.getRole().equalsIgnoreCase("employee")) {
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        tinyDB.putObject("current_account", account);
                        tinyDB.putString("access_token", account.getToken());
                        ApiClient.setAuthToken(account.getToken());
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "This user is not authorized!", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Username or password is wrong!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error connection: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}