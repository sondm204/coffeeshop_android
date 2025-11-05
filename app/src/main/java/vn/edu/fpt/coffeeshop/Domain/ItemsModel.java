package vn.edu.fpt.coffeeshop.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemsModel implements Serializable {
    private String title;
    private String description;
    private List<String> picUrl;
    private Double price;
    private Double rating;
    private int numberInCart;
    private String extra;

    public ItemsModel() {
        this.title = "";
        this.description = "";
        this.picUrl = new ArrayList<>();
        this.price = 0.0;
        this.rating = 0.0;
        this.numberInCart = 0;
        this.extra = "";
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
