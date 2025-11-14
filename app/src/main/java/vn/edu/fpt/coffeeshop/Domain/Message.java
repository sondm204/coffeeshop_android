package vn.edu.fpt.coffeeshop.Domain;

import java.util.List;

public class Message {
    public static final int TYPE_USER = 1;
    public static final int TYPE_BOT = 2;
    public static final int TYPE_TYPING = 3;
    public static final int TYPE_ERROR = 4;

    private String message;
    private int type;
    private List<ItemsModel> products;

    private long timestamp;

    public Message(String message, int type) {
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String message, int type, List<ItemsModel> products) {
        this.message = message;
        this.type = type;
        this.products = products;
    }

    public List<ItemsModel> getProducts() {
        return products;
    }

    public void setProducts(List<ItemsModel> products) {
        this.products = products;
    }

    public String getMessage() {
        return message;
    }

    public int getType() {
        return type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isUser() {
        return type == TYPE_USER;
    }

    public boolean isBot() {
        return type == TYPE_BOT;
    }

    public boolean isTyping() {
        return type == TYPE_TYPING;
    }

    public boolean isError() {
        return type == TYPE_ERROR;
    }
}