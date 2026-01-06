package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Movie;

import java.sql.SQLException;

public interface IMovieDao {
    void addMovie(Movie movie) throws SQLException;
}
