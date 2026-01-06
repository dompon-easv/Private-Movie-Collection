package dk.easv.privatemoviecollection.dal;

import dk.easv.privatemoviecollection.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoryDao {

    private ConnectionManager db;
    private Connection connection;

    public CategoryDao(ConnectionManager db) throws SQLException {
        this.db = db;
    }

    public void addCategory(Category category) throws SQLException {
        String sql = "INSERT INTO category (name) VALUES (?)";

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, category.getName());
            stmt.executeUpdate();
        }
    }

    //adding do db
    //deleting from db
    //getting from db
    //deleting movie from category

}
