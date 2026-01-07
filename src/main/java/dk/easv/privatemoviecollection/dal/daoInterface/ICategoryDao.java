package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDao {
    void addCategory(Category category) throws SQLException;
    List<Category> getAllCategories() throws SQLException;
}
