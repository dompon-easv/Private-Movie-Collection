package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDao {

    void addMovie(Movie movie) throws SQLException;

    List<Movie> getAllMovies() throws SQLException;

    void editMovie(Movie movie) throws SQLException;

    void deleteMovie(Movie movie) throws SQLException;
}
