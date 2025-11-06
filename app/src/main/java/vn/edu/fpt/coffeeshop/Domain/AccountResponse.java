package vn.edu.fpt.coffeeshop.Domain;

public class AccountResponse {
    private int id;
    private String name;
    private String imageUrl;
    private String role;

    public AccountResponse() {}

    public AccountResponse(int id, String name, String imageUrl, String role) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
