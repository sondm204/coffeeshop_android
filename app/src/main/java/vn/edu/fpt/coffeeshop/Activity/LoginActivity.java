package vn.edu.fpt.coffeeshop.Activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.coffeeshop.Api.ApiClient;
import vn.edu.fpt.coffeeshop.Api.ApiService;
import vn.edu.fpt.coffeeshop.Domain.GoogleLoginRequest;
import vn.edu.fpt.coffeeshop.Domain.LoginRequest;
import vn.edu.fpt.coffeeshop.Domain.AccountResponse;
import vn.edu.fpt.coffeeshop.Helper.AuthManager;
import vn.edu.fpt.coffeeshop.R;

public class LoginActivity extends AppCompatActivity {

    EditText usernameInput, pwInput;
    Button btnLogin;
    ImageButton ggBtnLogin;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ApiService apiService;
    private AuthManager authManager; // Thêm


    private ActivityResultLauncher<Intent> googleSignInLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        apiService = ApiClient.getClient().create(ApiService.class);
        authManager = new AuthManager(this);
        if (authManager.isLoggedIn()) {
            navigateToDashboard();
            return;
        }
        setContentView(R.layout.activity_login);


        usernameInput = findViewById(R.id.usernameInput);
        pwInput = findViewById(R.id.pwInput);
        btnLogin = findViewById(R.id.loginBtn);
        ggBtnLogin = findViewById(R.id.ggLoginBtn);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Tự động generate từ google-services.json
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Setup Google Sign In launcher
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        handleGoogleSignInResult(task);
                    } else {
                        hideLoading();
                        Toast.makeText(this, "Google sign in cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        ggBtnLogin.setOnClickListener(v -> loginWithGoogle());
        btnLogin.setOnClickListener(v -> doLogin());

    }

    private void doLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = pwInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(username, password);
        Call<AccountResponse> call = apiService.login(request);

        call.enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AccountResponse account = response.body();
                    if (account.getRole().equalsIgnoreCase("admin") || account.getRole().equalsIgnoreCase("employee")) {
                        Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();
                        authManager.saveUserInfo(account, "NORMAL");
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

    private void loginWithGoogle() {
        showLoading();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        googleSignInLauncher.launch(signInIntent);
    }

    private void handleGoogleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Authenticate with Firebase
            firebaseAuthWithGoogle(account.getIdToken());

        } catch (ApiException e) {
            hideLoading();
            Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Get Firebase ID Token để gửi lên backend
                            user.getIdToken(true)
                                    .addOnCompleteListener(tokenTask -> {
                                        if (tokenTask.isSuccessful()) {
                                            String firebaseIdToken = tokenTask.getResult().getToken();
                                            sendTokenToBackend(firebaseIdToken);
                                        } else {
                                            hideLoading();
                                            Toast.makeText(LoginActivity.this,
                                                    "Failed to get token", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    } else {
                        hideLoading();
                        Toast.makeText(LoginActivity.this,
                                "Firebase authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendTokenToBackend(String idToken) {
        GoogleLoginRequest request = new GoogleLoginRequest(idToken);

        apiService.loginWithGoogle(request).enqueue(new Callback<AccountResponse>() {
            @Override
            public void onResponse(Call<AccountResponse> call, Response<AccountResponse> response) {
                hideLoading();

                if (response.isSuccessful() && response.body() != null) {
                    AccountResponse accountResponse = response.body();
                    authManager.saveUserInfo(accountResponse, "GOOGLE");
                    ApiClient.setAuthToken(accountResponse.getToken());
                    saveTokenAndNavigate(accountResponse);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Backend authentication failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountResponse> call, Throwable t) {
                hideLoading();
                Toast.makeText(LoginActivity.this,
                        "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void navigateToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void saveTokenAndNavigate(AccountResponse response) {
        // Lưu token vào SharedPreferences
        getSharedPreferences("MyApp", MODE_PRIVATE)
                .edit()
                .putString("token", response.getToken())
                .putString("userId", response.getId())
                .putString("userName", response.getName())
                .putString("userRole", response.getRole())
                .putString("userImage", response.getImageUrl())
                .apply();

        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

        // Navigate to main activity
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    private void showLoading() {
        btnLogin.setEnabled(false);
        ggBtnLogin.setEnabled(false);
    }

    private void hideLoading() {
        btnLogin.setEnabled(true);
        ggBtnLogin.setEnabled(true);
    }
}