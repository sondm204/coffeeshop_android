package vn.edu.fpt.coffeeshop.Helper;

// AuthManager.java
import android.content.Context;
import android.content.SharedPreferences;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import vn.edu.fpt.coffeeshop.R;

import vn.edu.fpt.coffeeshop.Domain.AccountResponse;

public class AuthManager {

    private static final String PREF_NAME = "MyApp";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_ROLE = "userRole";
    private static final String KEY_USER_IMAGE = "userImage";
    private static final String KEY_LOGIN_TYPE = "loginType"; // "NORMAL" hoặc "GOOGLE"

    private Context context;
    private SharedPreferences sharedPreferences;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;

    public AuthManager(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.firebaseAuth = FirebaseAuth.getInstance();

        // Initialize Google Sign In Client
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        this.googleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    /**
     * Lưu thông tin user sau khi login
     */
    public void saveUserInfo(AccountResponse response, String loginType) {
        sharedPreferences.edit()
                .putString(KEY_TOKEN, response.getToken())
                .putString(KEY_USER_ID, response.getId())
                .putString(KEY_USER_NAME, response.getName())
                .putString(KEY_USER_ROLE, response.getRole())
                .putString(KEY_USER_IMAGE, response.getImageUrl())
                .putString(KEY_LOGIN_TYPE, loginType)
                .apply();
    }

    /**
     * Kiểm tra user đã login chưa
     */
    public boolean isLoggedIn() {
        return sharedPreferences.contains(KEY_TOKEN) &&
                getToken() != null &&
                !getToken().isEmpty();
    }

    /**
     * Lấy token
     */
    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    /**
     * Lấy thông tin user
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, "");
    }

    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, 0);
    }

    public String getUserRole() {
        return sharedPreferences.getString(KEY_USER_ROLE, "");
    }

    public String getUserImage() {
        return sharedPreferences.getString(KEY_USER_IMAGE, "");
    }

    public String getLoginType() {
        return sharedPreferences.getString(KEY_LOGIN_TYPE, "NORMAL");
    }

    /**
     * Logout - Xóa toàn bộ data và sign out khỏi Firebase/Google
     */
    public void logout(OnLogoutCompleteListener listener) {
        String loginType = getLoginType();

        // Clear SharedPreferences
        sharedPreferences.edit().clear().apply();

        // Sign out from Firebase
        firebaseAuth.signOut();

        // Sign out from Google nếu đăng nhập bằng Google
        if ("GOOGLE".equals(loginType)) {
            googleSignInClient.signOut()
                    .addOnCompleteListener(task -> {
                        if (listener != null) {
                            listener.onLogoutComplete(task.isSuccessful());
                        }
                    });
        } else {
            if (listener != null) {
                listener.onLogoutComplete(true);
            }
        }
    }

    /**
     * Revoke Google access (xóa quyền truy cập hoàn toàn)
     */
    public void revokeGoogleAccess(OnLogoutCompleteListener listener) {
        googleSignInClient.revokeAccess()
                .addOnCompleteListener(task -> {
                    if (listener != null) {
                        listener.onLogoutComplete(task.isSuccessful());
                    }
                });
    }

    // Callback interface
    public interface OnLogoutCompleteListener {
        void onLogoutComplete(boolean success);
    }
}
