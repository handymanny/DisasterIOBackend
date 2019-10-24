package Objects.DisasterEventObjects;

import com.google.gson.annotations.SerializedName;

public class Category {

    // Field Variables
    @SerializedName("id")
    private int categoryId;
    @SerializedName("title")
    private String categoryTitle;

    // Constructor
    public Category(int categoryId, String categoryTitle) {
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
    }

    public Category() {
        // Default Constructor
    }

    // Getters & Setters
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }
}
