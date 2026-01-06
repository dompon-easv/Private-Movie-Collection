package dk.easv.privatemoviecollection.dal.dao;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.model.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    // adding movies to db
    // deleting movies from db
    //editing movies on db
    //getting movies from db
}
