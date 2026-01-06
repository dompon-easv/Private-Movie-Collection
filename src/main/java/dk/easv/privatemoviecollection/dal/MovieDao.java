package dk.easv.privatemoviecollection.dal;

import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MovieDao {
    private ConnectionManager db;
    private Connection connection;

    public MovieDao(ConnectionManager db) throws SQLException {
        this.db = db;
    }

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

    // adding movies to db
    // deleting movies from db
    //editing movies on db
    //getting movies from db
}
