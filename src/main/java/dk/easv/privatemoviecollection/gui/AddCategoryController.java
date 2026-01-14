package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.exceptions.CategoryException;
import dk.easv.privatemoviecollection.gui.helpers.AlertHelper;
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
    private MainScreenController mainScreenController;

    public void init(CategoryManager categoryManager, MainScreenController mainScreenController) {
        this.categoryManager = categoryManager;
        this.mainScreenController = mainScreenController;
    }

    @FXML
    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onClickAddCategory(ActionEvent event) {
        String category = txtNewCategory.getText();
        if (category.isEmpty()) {
            AlertHelper.showAlert("Please enter a category name");
        }
        else {

            try {
                categoryManager.addCategory(category);
                mainScreenController.loadCategories();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();

            } catch (CategoryException e) {
                AlertHelper.showAlert(e.getMessage());
            }
        }
    }
}
