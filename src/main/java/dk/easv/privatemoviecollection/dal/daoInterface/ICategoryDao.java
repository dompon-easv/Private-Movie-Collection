package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Category;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public interface ICategoryDao {
    void addCategory(Category category) throws SQLException;
    ObservableList<Category> getAllCategories() throws SQLException;

    void deleteCategory(int id);
}
