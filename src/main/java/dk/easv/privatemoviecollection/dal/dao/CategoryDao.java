package dk.easv.privatemoviecollection.dal.dao;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao implements ICategoryDao {

    private ConnectionManager db;
    private Connection connection;

    public CategoryDao(ConnectionManager db) throws SQLException {
        this.db = db;
    }
    @Override
    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category (name) VALUES (?)";

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
        }
    }

    public List<Category> getAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT name FROM category";

        try (Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                categories.add(new Category(rs.getString("name")));
            }
        } return  categories;
    }

    //adding do db
    //deleting from db
    //getting from db
    //deleting movie from category

}
