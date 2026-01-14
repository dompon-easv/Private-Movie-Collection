package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDao {
    void addCategory(Category category) throws SQLException;
    List<Category> getAllCategories() throws SQLException;
    List<Movie> getAllMoviesForCategory(int categoryId) throws SQLException;
    void deleteCategory(int id) throws SQLException;
    void addMovieToCategory(int movieId, int categoryId) throws SQLException;
    List<Category> getCategoriesForMovie(int movieId) throws SQLException;
    void deleteCategoriesForMovie(int movieId) throws SQLException;
    void addCategoryToMovie(int movieId, int categoryId) throws SQLException;
}
