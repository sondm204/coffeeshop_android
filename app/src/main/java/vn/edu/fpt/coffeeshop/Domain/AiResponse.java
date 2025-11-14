package vn.edu.fpt.coffeeshop.Domain;

import java.util.List;

public class AiResponse {
    private String message;
    private List<ItemsModel> products;

    public String getMessage() {
        return message;
    }

    public void setMessage(String text) {
        this.message = text;
    }

    public List<ItemsModel> getProducts() {
        return products;
    }

    public void setProducts(List<ItemsModel> products) {
        this.products = products;
    }
}
