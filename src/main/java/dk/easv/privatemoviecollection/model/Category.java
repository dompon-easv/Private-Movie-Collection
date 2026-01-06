package dk.easv.privatemoviecollection.model;

public class Category {
    private String category;
    private int id;

    public Category(String category)
    { this.category = category; }

    public String getCategory() { return category; }

    public Category (String category, int id)
    { this.category = category;
        this.id = id; }

}
