package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.bll.exceptions.CategoryException;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
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

    public List<Category> getCategories() {
        try {
            return categoryDao.getAllCategories();
        }catch (SQLException e) {
            e.printStackTrace();
            return List.of();
        }
    }

   public void deleteCategory(int id) throws SQLException {
        categoryDao.deleteCategory(id);
   }

    public void addMovieToCategory(int movieId, int categoryId) throws SQLException {
        categoryDao.addMovieToCategory(movieId, categoryId);
    }

    public List<Movie> getAllMoviesForCategory(int categoryId) {
        try {
            return categoryDao.getAllMoviesForCategory(categoryId);
        }catch (SQLException e) {
            throw new CategoryException("Failed to get all movies for category",e);
        }
   }

    // adding category instance
    //deleting
    //getting
}
