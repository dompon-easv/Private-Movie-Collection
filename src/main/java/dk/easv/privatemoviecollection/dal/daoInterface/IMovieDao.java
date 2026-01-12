package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface IMovieDao {

    void addMovie(Movie movie) throws SQLException;

    List<Movie> getAllMovies() throws SQLException;

    void editMovie(Movie movie) throws SQLException;

    void deleteMovie(int id) throws SQLException;

    void updateLastView(int movieId) throws SQLException;
}
