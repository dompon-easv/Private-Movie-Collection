package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

    // adding category instance
    //deleting
    //getting
}
