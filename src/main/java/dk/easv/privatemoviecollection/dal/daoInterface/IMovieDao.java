package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public interface IMovieDao {
    void addMovie(Movie movie) throws SQLException;
    ObservableList<Movie> getAllMovies() throws SQLException;
}
