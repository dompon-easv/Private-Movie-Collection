package dk.easv.privatemoviecollection.be;

public class Category {
    private String name;
    private int id;

    public Category(String name)
    { this.name = name; }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }
    public int getId() { return id; }

    @Override
    public String toString() {
        return name;
    }

    /* Avoid duplicates in categories while adding movie,
    if id match they represent the same category */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}