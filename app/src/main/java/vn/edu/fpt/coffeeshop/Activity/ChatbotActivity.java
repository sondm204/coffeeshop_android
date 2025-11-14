package vn.edu.fpt.coffeeshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.fpt.coffeeshop.Adapter.MessageAdapter;
import vn.edu.fpt.coffeeshop.Api.ApiClient;
import vn.edu.fpt.coffeeshop.Api.ApiService;
import vn.edu.fpt.coffeeshop.Domain.AccountResponse;
import vn.edu.fpt.coffeeshop.Domain.AiRequest;
import vn.edu.fpt.coffeeshop.Domain.AiResponse;
import vn.edu.fpt.coffeeshop.Domain.ItemsModel;
import vn.edu.fpt.coffeeshop.Domain.Message;
import vn.edu.fpt.coffeeshop.R;

public class ChatbotActivity extends AppCompatActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private ImageButton sendButton;
    private MessageAdapter messageAdapter;
    private List<Message> messageList;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbot);
        apiService = ApiClient.getClient().create(ApiService.class);

        chatRecyclerView = findViewById(R.id.chat_recyclerview);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(messageAdapter);

        // Tin nhắn chào mừng từ bot
        addBotMessage("Xin chào! Tôi có thể giúp gì cho bạn?");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Cho phép gửi bằng Enter
        messageEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                return true;
            }
            return false;
        });
    }

    private void sendMessage() {
        String message = messageEditText.getText().toString().trim();
        if (!message.isEmpty()) {
            sendButton.setEnabled(false);

            addUserMessage(message);
            messageEditText.setText("");

            showTypingIndicator();

            sendMessageToApi(message);
        }
    }

    private void addUserMessage(String text) {
        messageList.add(new Message(text, Message.TYPE_USER));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void addBotMessage(String text, List<ItemsModel> products) {
        messageList.add(new Message(text, Message.TYPE_BOT, products));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }
    private void addBotMessage(String text) {
        messageList.add(new Message(text, Message.TYPE_BOT));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void addErrorMessage(String text) {
        messageList.add(new Message(text, Message.TYPE_ERROR));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void showTypingIndicator() {
        messageList.add(new Message("", Message.TYPE_TYPING));
        messageAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void removeTypingIndicator() {
        // Tìm và xóa typing indicator
        for (int i = messageList.size() - 1; i >= 0; i--) {
            if (messageList.get(i).isTyping()) {
                messageList.remove(i);
                messageAdapter.notifyItemRemoved(i);
                break;
            }
        }
    }

    private void scrollToBottom() {
        chatRecyclerView.postDelayed(() -> {
            if (messageList.size() > 0) {
                chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
        }, 100);
    }

    private void sendMessageToApi(String message) {
        AiRequest userMessage = new AiRequest(message);

        Call<AiResponse> call = apiService.chat(userMessage);
        call.enqueue(new Callback<AiResponse>() {
            @Override
            public void onResponse(Call<AiResponse> call, Response<AiResponse> response) {
                // Xóa typing indicator
                removeTypingIndicator();

                // Bật lại nút gửi
                sendButton.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    String botResponse = response.body().getMessage();
                    List<ItemsModel> productList = response.body().getProducts();

                    // Giả lập độ trễ tự nhiên (tùy chọn)
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        addBotMessage(botResponse, productList);
                    }, 500);
                } else {
                    addErrorMessage("Đã xảy ra lỗi. Vui lòng thử lại.");
                }
            }

            @Override
            public void onFailure(Call<AiResponse> call, Throwable t) {
                // Xóa typing indicator
                removeTypingIndicator();

                // Bật lại nút gửi
                sendButton.setEnabled(true);

                // Hiển thị lỗi cụ thể
                String errorMessage = "Không thể kết nối. Vui lòng kiểm tra mạng.";
                if (t instanceof SocketTimeoutException) {
                    errorMessage = "Hết thời gian chờ. Vui lòng thử lại.";
                } else if (t instanceof IOException) {
                    errorMessage = "Lỗi mạng. Vui lòng kiểm tra kết nối.";
                }

                addErrorMessage(errorMessage);
            }
        });
    }
}