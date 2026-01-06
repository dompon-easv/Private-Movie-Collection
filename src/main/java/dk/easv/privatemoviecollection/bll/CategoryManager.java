package dk.easv.privatemoviecollection.bll;

import dk.easv.privatemoviecollection.dal.CategoryDao;
import dk.easv.privatemoviecollection.model.Category;

import java.sql.SQLException;

public class CategoryManager {

    private CategoryDao categoryDao;

    public CategoryManager(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public Category addCategory(String name) throws SQLException {
        Category category = new Category(name);

        categoryDao.addCategory(category);

        return category;
    }

    // adding category instance
    //deleting
    //getting
}
