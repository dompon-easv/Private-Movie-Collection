package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.dal.dao.MovieDao;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.model.Movie;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MovieManager {

    private static IMovieDao movieDao;

    public MovieManager(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public Movie addMovie(String title, double imdbRating, double myRating, String filePath) throws SQLException {
        Movie movie = new Movie(title, imdbRating, myRating, filePath);
        if(isFormatCorrect(filePath)) {
            movieDao.addMovie(movie);
            return movie;
        }
        else {
            return null;
        }

    }
    public List<Movie> getAllMovies() throws SQLException {
        return movieDao.getAllMovies();
    }

    public void deleteMovie(int id) throws SQLException {
        movieDao.deleteMovie(id);
    }

    public boolean canOpenMovie(String filePath) {
        return filePath != null && new File(filePath).exists();
    }

    public boolean isFormatCorrect(String filePath)
    {
        return filePath.endsWith(".mp4") || filePath.endsWith(".mpeg4");
    }

    public void updateLastView(int movieId) throws SQLException {
        movieDao.updateLastView(movieId);
    }

    public boolean shouldWarnAboutOldAndLowRatedMovies() throws SQLException {
        return movieDao.isOldAndHasLowRating();
    }


    // preventing adding another movie with the same path?
    // checking if format is correct - only mp4
    // creating instances of movies
    // editing them
    // getting all movies cause GUI is not allowed to talk to database directly
}
