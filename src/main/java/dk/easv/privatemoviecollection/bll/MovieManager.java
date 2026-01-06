package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.dal.dao.MovieDao;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.model.Movie;

import java.sql.SQLException;

public class MovieManager {

    private IMovieDao movieDao;

    public MovieManager(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public Movie addMovie(String title, double imdbRating, double myRating, String filePath) throws SQLException {
        Movie movie = new Movie(title, imdbRating, myRating, filePath);

        movieDao.addMovie(movie);
        return movie;
    }
    // opening the movie by the default app
    // preventing adding another movie with the same path?
    // checking if format is correct - only mp4
    // creating instances of movies
    // editing them
    // getting all movies cause GUI is not allowed to talk to database directly
}
