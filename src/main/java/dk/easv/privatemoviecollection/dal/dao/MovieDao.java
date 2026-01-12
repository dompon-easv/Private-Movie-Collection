package dk.easv.privatemoviecollection.dal.dao;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDao implements IMovieDao {
    private static ConnectionManager db;

    public MovieDao(ConnectionManager db) throws SQLException {
        this.db = db;
    }

    @Override
    public boolean isOldAndHasLowRating() throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM movie
                WHERE myrating < 6
                AND (lastview IS NULL 
                    OR lastview < DATEADD(YEAR, -2, GETDATE()))
                """;

        try (Connection con = db.getConnection();
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    @Override
    public void addMovie(Movie movie) throws SQLException {
        String sql = "INSERT INTO movie (title, imdbrating, myrating, filelink) VALUES (?, ?, ?, ?)";

        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, movie.getTitle());
            stmt.setDouble(2, movie.getImdbRating());
            stmt.setDouble(3, movie.getMyRating());
            stmt.setString(4, movie.getFileLink());
            stmt.executeUpdate();

            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    movie.setId(keys.getInt(1)); }
            }
        }
    }

    @Override
    public List<Movie> getAllMovies() throws SQLException { List<Movie> movies = new ArrayList<>(); String sql = """
        SELECT id, title, imdbrating, myrating, filelink
        FROM movie """;
        try (Connection con = db.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getDouble("imdbrating"),
                        rs.getDouble("myrating"),
                        rs.getString("filelink") ); movies.add(movie);}
        }
            return movies;
    }



    @Override
            public void editMovie(Movie movie) throws SQLException {String sql = """
        UPDATE movie SET title = ?, imdbrating = ?, myrating = ?, filelink = ?
        WHERE id = ? """;

            try (Connection con = db.getConnection();
                 PreparedStatement stmt = con.prepareStatement(sql)) {

                stmt.setString(1, movie.getTitle());
                stmt.setDouble(2, movie.getImdbRating());
                stmt.setDouble(3, movie.getMyRating());
                stmt.setString(4, movie.getFileLink());
                stmt.setInt(5, movie.getId());

                stmt.executeUpdate();
            }
        }

    public void deleteMovie(int id) {
        String sql = "DELETE FROM movie WHERE id = ?";
        String sql2 = "DELETE FROM CatMovie WHERE MovieId = ?";
        try  (Connection con = ConnectionManager.getConnection();
              PreparedStatement stmt = con.prepareStatement(sql);
              PreparedStatement stmt2 = con.prepareStatement(sql2))
        { stmt2.setInt(1, id);
            stmt2.executeUpdate();

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateLastView(int movieId) throws SQLException {
        String sql = "UPDATE movie SET lastview = GETDATE() WHERE id = ?";
        try(Connection con = ConnectionManager.getConnection();
        PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, movieId);
            stmt.executeUpdate();
        }
    }

    // adding movies to db
    // deleting movies from db
    //editing movies on db
    //getting movies from db
}
