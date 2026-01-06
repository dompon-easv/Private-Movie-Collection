package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;

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
    @FXML
    private Label lblFilePath;

    private MovieManager movieManager;

    public void setMovieManager(MovieManager movieManager) {
        this.movieManager =  movieManager;
    }

    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onClickSave(ActionEvent event) throws SQLException {
        String title = txtTitle.getText();
        double imdbRating = Double.parseDouble(txtIMDBRating.getText());
        double myRating = Double.parseDouble(txtMyRating.getText());
        String filePath = lblFilePath.getText();

        if(filePath.isEmpty()) {
            Alert.AlertType alertType = Alert.AlertType.ERROR;
            Alert alert = new Alert(alertType);
            alert.setTitle("Error");
        }
        else{

            Movie newMovie = movieManager.addMovie(title, imdbRating,myRating,filePath);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
        }
    }

    public void onClickBrowse(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a file");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fc.showOpenDialog(stage);
        if (selectedFile != null) {
            lblFilePath.setText(selectedFile.getAbsolutePath());
        }
    }
}
