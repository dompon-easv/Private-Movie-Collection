package dk.easv.privatemoviecollection.dal.dao;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    public ObservableList<Category> getAllCategories() throws SQLException {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String sql = "SELECT id, name FROM category";

        try (Connection con = db.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                categories.add(new Category(rs.getString("name"), rs.getInt("id")));
            }
        } return  categories;
    }

    public void deleteCategory(int id) {
        String sql = "DELETE FROM category WHERE id = ?";
        try  (Connection con = ConnectionManager.getConnection();
              PreparedStatement stmt = con.prepareStatement(sql))
        { stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch(SQLException e) {
        }
    }

    //adding do db
    //deleting from db
    //getting from db
    //deleting movie from category

}
