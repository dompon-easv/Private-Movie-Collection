package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.be.Movie;
import java.sql.SQLException;
import java.util.List;

public interface IMovieDao {

    void addMovie(Movie movie) throws SQLException;
    void updateMovie(Movie movie) throws SQLException;
    void deleteMovie(int id) throws SQLException;
    List<Movie> getAllMovies() throws SQLException;
    void updateLastView(int movieId) throws SQLException;
    boolean isOldAndHasLowRating() throws SQLException;
    boolean filePathExists(String filePath) throws SQLException;
}