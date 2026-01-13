package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CategoryManager {

    private ICategoryDao categoryDao;

    public CategoryManager(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public Category addCategory(String name) throws SQLException {
        Category category = new Category(name);

        categoryDao.addCategory(category);

        return category;
    }

    public List<Category> getCategories() throws SQLException {
    return categoryDao.getAllCategories();
    }

   public void deleteCategory(int id) throws SQLException {
        categoryDao.deleteCategory(id);
   }

    public void addMovieToCategory(int movieId, int categoryId) throws SQLException {
        categoryDao.addMovieToCategory(movieId, categoryId);
    }

    public List<Movie> getAllMoviesForCategory(int categoryId) throws SQLException {
        return categoryDao.getAllMoviesForCategory(categoryId);
   }
    public List<Category> getCategoriesForMovie(int movieId) throws SQLException {
        return categoryDao.getCategoriesForMovie(movieId);
    }
    public void updateMovieCategories(int movieId,
                                      List<Category> categories)
            throws SQLException { categoryDao.deleteCategoriesForMovie(movieId);

        for (Category category : categories) {
            categoryDao.addCategoryToMovie(movieId, category.getId());
        }
    }




    // adding category instance
    //deleting
    //getting
}
