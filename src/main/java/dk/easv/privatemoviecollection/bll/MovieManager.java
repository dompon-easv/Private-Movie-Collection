package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.bll.exceptions.MovieException;
import dk.easv.privatemoviecollection.dal.daoInterface.IMovieDao;
import dk.easv.privatemoviecollection.be.Movie;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MovieManager {

    private IMovieDao movieDao;

    public MovieManager(IMovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public Movie addMovie(String title, double imdbRating, double myRating, String filePath) {
        if (title == null || title.length() == 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (imdbRating < 0 || imdbRating > 10) {
            throw new IllegalArgumentException("IMDB rating must be between 0 and 10");
        }
        if (myRating < 0 || myRating > 10) {
            throw new IllegalArgumentException("My rating must be between 0 and 10");
        }
        if (!isFormatCorrect(filePath)) {
            throw new IllegalArgumentException("Invalid file path");
        }
        if (filePathExists(filePath)) {
            throw new IllegalArgumentException("File already exists");
        }

        Movie movie = new Movie(title, imdbRating, myRating, filePath);

        try {
            movieDao.addMovie(movie);
        } catch (SQLException e) {
            throw new MovieException("Failed to add movie", e);
        }

        return movie;
    }

    public void updateMovie(Movie movie)  {
        if (movie.getTitle() == null || movie.getTitle().length()== 0) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (movie.getImdbRating() < 0 || movie.getImdbRating() > 10) {
            throw new IllegalArgumentException("IMDB rating must be between 0 and 10");
        }
        if (movie.getMyRating() < 0 || movie.getMyRating() > 10) {
            throw new IllegalArgumentException("My rating must be between 0 and 10");
        }
        if (!isFormatCorrect(movie.getFileLink())) {
            throw new IllegalArgumentException("Invalid file path");
        }

        try{
            movieDao.updateMovie(movie);
        }catch (SQLException e) {
            throw new MovieException("Failed to update movie", e);
        }
    }

    public void deleteMovie(int id) {
        try {
            movieDao.deleteMovie(id);
        } catch (SQLException e) {
            throw new MovieException("Failed to delete movie", e);
        }
    }

    public List<Movie> getAllMovies() {
        try {
            return movieDao.getAllMovies();
        } catch (SQLException e) {
            throw new MovieException("Failed to get all movies", e);
        }
    }

    public void updateLastView(int movieId) {
        try {
            movieDao.updateLastView(movieId);
        } catch (SQLException e) {
            throw new MovieException("Failed to update last view", e);
        }
    }

    public boolean shouldWarnAboutOldAndLowRatedMovies() {
        try {
            return movieDao.isOldAndHasLowRating();
        } catch (SQLException e) {
            throw new MovieException("Failed to check if old and low rated movies", e);
        }
    }

    public boolean filePathExists(String filePath) { //it is one of 2 options how not to add file with the same path
        try {                                        // but it was handled in DB by adding UNIQUE constraint to file
            return movieDao.filePathExists(filePath);
        } catch (SQLException e) {
            throw new MovieException("Failed to check if file exists", e);
        }
    }

    public boolean canOpenMovie(String filePath) {
        return filePath != null && new File(filePath).exists();
    }

    public boolean isFormatCorrect(String filePath) {
        return filePath.endsWith(".mp4") || filePath.endsWith(".mpeg4");
    }
}


