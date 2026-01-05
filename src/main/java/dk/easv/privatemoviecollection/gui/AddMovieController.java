package dk.easv.privatemoviecollection.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddMovieController {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtIMDBRating;
    @FXML
    private TextField txtMyRating;
    @FXML
    private ListView lstAllCategories;
    @FXML
    private ListView lstChosenCategories;

    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onClickSave(ActionEvent event) {
    }
}
