package vn.edu.fpt.coffeeshop.Domain;

public class CategoryModel {
    private int id;
    private String title;

    public CategoryModel() {
        this.id = 0;
        this.title = "";
    }

    public CategoryModel(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
