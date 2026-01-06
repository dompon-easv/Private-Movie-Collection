package dk.easv.privatemoviecollection.dal.daoInterface;

import dk.easv.privatemoviecollection.model.Category;

import java.sql.SQLException;

public interface ICategoryDao {
    void addCategory(Category category) throws SQLException;
}
