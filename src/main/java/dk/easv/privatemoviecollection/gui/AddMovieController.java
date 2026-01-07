package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddMovieController {

    @FXML
    private TextField txtTitle;
    @FXML
    private TextField txtIMDBRating;
    @FXML
    private TextField txtMyRating;
    @FXML
    private ListView<Category> lstAllCategories;
    @FXML
    private ListView lstChosenCategories;
    @FXML
    private Label lblFilePath;

    private MovieManager movieManager;
    private CategoryManager categoryManager;

    public void init(CategoryManager categoryManager, MovieManager movieManager) throws SQLException {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        loadCategories();

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

    public void loadCategories() throws SQLException {
        lstAllCategories.getItems().clear();
        try{
            lstAllCategories.getItems().setAll(categoryManager.getCategories());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
