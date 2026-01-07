package dk.easv.privatemoviecollection.model;

public class Category {
    private String name;
    private int id;

    public Category(String name)
    { this.name = name; }

    public String getName() { return name; }

    public Category (String name, int id)
    { this.name = name;
        this.id = id; }
}
