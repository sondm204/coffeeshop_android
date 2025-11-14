package vn.edu.fpt.coffeeshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.edu.fpt.coffeeshop.Domain.Message;
import vn.edu.fpt.coffeeshop.R;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messages;

    public MessageAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        return messages.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Message.TYPE_TYPING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_typing_indicator, parent, false);
            return new TypingViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message, parent, false);
            return new MessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessageViewHolder) {
            Message message = messages.get(position);
            MessageViewHolder messageHolder = (MessageViewHolder) holder;

            if (message.isUser()) {
                // Tin nhắn user
                messageHolder.userMessageText.setText(message.getMessage());
                messageHolder.userMessageText.setVisibility(View.VISIBLE);
                messageHolder.botMessageText.setVisibility(View.GONE);
            } else {
                messageHolder.botMessageText.setText(message.getMessage());
                messageHolder.botMessageText.setVisibility(View.VISIBLE);
                messageHolder.userMessageText.setVisibility(View.GONE);

                if (message.isError()) {
                    messageHolder.botMessageText.setTextColor(0xFFD32F2F); // đỏ
                } else {
                    messageHolder.botMessageText.setTextColor(0xFF000000); // đen
                }

                // Hiển thị products nếu có
                if (message.getProducts() != null && !message.getProducts().isEmpty()) {
                    messageHolder.botProductsRecyclerView.setVisibility(View.VISIBLE);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            messageHolder.botProductsRecyclerView.getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                    );
                    messageHolder.botProductsRecyclerView.setLayoutManager(layoutManager);
                    messageHolder.botProductsRecyclerView.setAdapter(
                            new ProductAdapter(messageHolder.botProductsRecyclerView.getContext(), message.getProducts())
                    );
                } else {
                    messageHolder.botProductsRecyclerView.setVisibility(View.GONE);
                }
            }
        }
        // TypingViewHolder không cần bind gì
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // ViewHolder cho tin nhắn thường
    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView userMessageText;
        TextView botMessageText;
        RecyclerView botProductsRecyclerView;


        public MessageViewHolder(View itemView) {
            super(itemView);
            userMessageText = itemView.findViewById(R.id.user_message_text);
            botMessageText = itemView.findViewById(R.id.bot_message_text);
            botProductsRecyclerView = itemView.findViewById(R.id.bot_products_recyclerview);
        }
    }

    // ViewHolder cho typing indicator
    public static class TypingViewHolder extends RecyclerView.ViewHolder {
        public TypingViewHolder(View itemView) {
            super(itemView);
        }
    }
}