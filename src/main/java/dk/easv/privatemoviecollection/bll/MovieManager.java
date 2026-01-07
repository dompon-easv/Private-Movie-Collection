package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.dal.dao.MovieDao;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.ObservableList;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class MovieManager {

    private static IMovieDao movieDao;

    public MovieManager(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public Movie addMovie(String title, double imdbRating, double myRating, String filePath) throws SQLException {
        Movie movie = new Movie(title, imdbRating, myRating, filePath);

        movieDao.addMovie(movie);
        return movie;
    }

    public static ObservableList<Movie> getMovies() throws SQLException {
        return movieDao.getAllMovies();
    }

    public void deleteMovie(int id) throws SQLException {
        MovieDao.deleteMovie(id);
    }
    // opening the movie by the default app
    public void openMovieInApp(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.exists()) {
            Desktop.getDesktop().open(file);
        }
    }
    // preventing adding another movie with the same path?
    // checking if format is correct - only mp4
    // creating instances of movies
    // editing them
    // getting all movies cause GUI is not allowed to talk to database directly
}
