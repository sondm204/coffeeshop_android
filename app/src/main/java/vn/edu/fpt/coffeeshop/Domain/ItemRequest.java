package vn.edu.fpt.coffeeshop.Domain;

import java.util.List;

public class ItemRequest {
    private String title;
    private String description;
    private List<String> picUrl;
    private Double price;
    private String extra;
    private String categoryId;

    public ItemRequest() {}

    public ItemRequest(String title, String description, List<String> picUrl, Double price, String extra, String categoryId) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.price = price;
        this.extra = extra;
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(List<String> picUrl) {
        this.picUrl = picUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
