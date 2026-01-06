package dk.easv.privatemoviecollection.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {
    @FXML
    private TextField txtNewCategory;

    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onClickAddCategory(ActionEvent event) {
        String category = txtNewCategory.getText();
    }
}
