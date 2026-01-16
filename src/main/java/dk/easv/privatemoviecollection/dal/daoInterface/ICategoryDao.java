package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDao {
    void addCategory(Category category) throws SQLException;
    void deleteCategory(int id) throws SQLException;
    void updateCategoriesForMovie(int movieId, List<Integer> categoryIds) throws SQLException;
    List<Category> getAllCategories() throws SQLException;
    void addMovieToCategory(int movieId, int categoryId) throws SQLException;
    List<Movie> getAllMoviesForCategory(int categoryId) throws SQLException;
    List<Category> getCategoriesForMovie(int movieId) throws SQLException;
}
