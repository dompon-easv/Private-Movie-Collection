package dk.easv.privatemoviecollection.dal.dao;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieDao implements IMovieDao {
    private ConnectionManager db;
    private Connection connection;

    public MovieDao(ConnectionManager db) throws SQLException {
        this.db = db;
    }
    @Override
    public void addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movie (title, imdbrating, myrating, filelink) VALUES (?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, movie.getTitle());
            stmt.setDouble(2, movie.getImdbRating());
            stmt.setDouble(3, movie.getMyRating());
            stmt.setString(4, movie.getFileLink());
            stmt.executeUpdate();
        }

    }

    public ObservableList<Movie> getAllMovies() throws SQLException {
        ObservableList<Movie> movies = FXCollections.observableArrayList();
        String sql = "SELECT * FROM movie";

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next()) {
                movies.add(new Movie(rs.getInt("id"), rs.getString("title"), rs.getDouble("imdbRating"), rs.getDouble("myRating"), rs.getString("fileLink")));
            }
        } return movies;}

    public void deleteMovie(int id) {
        String sql = "DELETE FROM movie WHERE id = ?";
        try  (Connection con = ConnectionManager.getConnection();
              PreparedStatement stmt = con.prepareStatement(sql))
        { stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // adding movies to db
    // deleting movies from db
    //editing movies on db
    //getting movies from db
}
