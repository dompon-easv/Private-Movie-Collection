package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.bll.exceptions.CategoryException;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.be.Category;
import dk.easv.privatemoviecollection.be.Movie;
import java.sql.SQLException;
import java.util.List;

public class CategoryManager {

    private ICategoryDao categoryDao;

    public CategoryManager(ICategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public void addCategory(String name) {

        if(name == null || name.trim().isEmpty()) {
            throw new CategoryException("Category name cannot be empty");
        }

        String trimmedName = name.trim();

        try {
            boolean exists = categoryDao.getAllCategories().stream().anyMatch(c -> c.getName().equalsIgnoreCase(trimmedName));
            if(exists) {
                throw new CategoryException("The category " + trimmedName + " already exists");}

            categoryDao.addCategory(new Category(trimmedName));
        } catch (SQLException e) {
            throw new CategoryException("Could not add category", e);
        }
    }

    public void deleteCategory(int id) {
        try{
            categoryDao.deleteCategory(id);
        }catch (SQLException e){
            throw new CategoryException(e.getMessage());
        }
    }

    public void updateMovieCategories(int movieId, List<Category> categories) {
        List<Integer> categoryIds = categories.stream()
                .map(Category::getId)
                .toList();
        try {
            categoryDao.updateCategoriesForMovie(movieId, categoryIds);
        }catch (SQLException e) {
            throw new CategoryException("Cannot update categories in movie",e);
        }
    }

    public List<Category> getCategories() {
        try {
            return categoryDao.getAllCategories();
        }catch (SQLException e) {
            throw new CategoryException("Could not get categories", e);
        }
    }

    public void addMovieToCategory(int movieId, int categoryId){
        try{
            categoryDao.addMovieToCategory(movieId, categoryId);
        }catch (SQLException e){
            throw new CategoryException(e.getMessage());
        }
    }

    public List<Movie> getAllMoviesForCategory(int categoryId) {
        try {
            return categoryDao.getAllMoviesForCategory(categoryId);
        }catch (SQLException e) {
            throw new CategoryException("Failed to get all movies for category",e);
        }
    }

    public List<Category> getCategoriesForMovie(int movieId)  {
        try {
            return categoryDao.getCategoriesForMovie(movieId);
        }catch (SQLException e) {
            throw new CategoryException("Failed to get all movies for category",e);
        }
    }
}
