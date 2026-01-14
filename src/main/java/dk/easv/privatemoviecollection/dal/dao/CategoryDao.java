package dk.easv.privatemoviecollection.dal.dao;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
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
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete category", e);
        }
    }

    public void addMovieToCategory(int movieId, int categoryId) throws SQLException {
        String sql = "INSERT INTO CatMovie (MovieId, CategoryId) VALUES (?, ?)";
        try(Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql)){
            stmt.setInt(1, movieId);
            stmt.setInt(2, categoryId);
            stmt.executeUpdate();
        }
    }

    public List<Movie> getAllMoviesForCategory(int categoryId) throws SQLException {
        List<Movie> movies = new ArrayList<>();
        String sqlPrompt = """
            SELECT m.id, m.title, m.imdbrating, m.myrating, m.filelink
            FROM Movie m
            INNER JOIN CatMovie cm ON m.id = cm.MovieId
            WHERE cm.CategoryId = ?
            ORDER BY m.title
            """;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlPrompt)){
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                movies.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("imdbrating"),
                        rs.getDouble("myrating"),
                        rs.getString("filelink")
                ));
            }
        }
        return movies;
    }

    public List<Category> getCategoriesForMovie(int movieId) throws SQLException {

        List<Category> categories = new ArrayList<>();

        String sql = """
            SELECT c.id, c.name
            FROM Category c
            INNER JOIN CatMovie cm ON c.id = cm.CategoryId
            WHERE cm.MovieId = ?
            ORDER BY c.name
        """;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    categories.add(new Category(
                            rs.getInt("id"),
                            rs.getString("name")
                    ));
                }
            }
        }

        return categories;
    }
// trying to delete categories from movie so that we can /edit/update ,PD trine rules

public void deleteCategoriesForMovie(int movieId) throws SQLException {

    String sql = "DELETE FROM CatMovie WHERE MovieId = ?";

    try (Connection con = ConnectionManager.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, movieId);
        ps.executeUpdate();
    }
}
    public void addCategoryToMovie(int movieId, int categoryId) throws SQLException {

        String sql = """
        INSERT INTO CatMovie (MovieId, CategoryId)
        VALUES (?, ?)
    """;

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ps.setInt(2, categoryId);
            ps.executeUpdate();
        }
    }


    //adding do db
    //deleting from db
    //getting from db
    //deleting movie from category

}
