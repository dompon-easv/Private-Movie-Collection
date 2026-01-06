package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.dal.CategoryDao;
import dk.easv.privatemoviecollection.model.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddCategoryController {
    @FXML
    private TextField txtNewCategory;

    private CategoryManager categoryManager;

    public AddCategoryController() {
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }
    @FXML
    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickAddCategory(ActionEvent event) throws SQLException {
        String category = txtNewCategory.getText();
        if (category.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
        }
        else {

            try {
                Category newCategory = categoryManager.addCategory(category);
                txtNewCategory.clear();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }
}
