package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddMovieController implements Initializable {

    @FXML private TextField txtTitle;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtMyRating;
    @FXML private ListView<Category> lstAllCategories;
    @FXML private ListView lstChosenCategories;
    @FXML private Label lblFilePath;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
    public void setMovieList(ObservableList<Movie> movieList) {this.movieList = movieList;}

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
            alert.showAndWait();
            return;
        }

        Movie newMovie = movieManager.addMovie(title, imdbRating,myRating,filePath);
        ObservableList<Category> selectedCategories = lstAllCategories.getSelectionModel().getSelectedItems();
        if(selectedCategories != null && !selectedCategories.isEmpty()) {
            for (Category category : selectedCategories) {
                categoryManager.addMovieToCategory(newMovie.getId(), category.getId());
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onClickBrowse(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a file");
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fc.showOpenDialog(stage);
        if (selectedFile != null) {
            try {
                // Destination folder for project
                String baseFolder = "src/main/resources/Movies/";
                File targetFolder = new File(baseFolder);
                if (!targetFolder.exists()) targetFolder.mkdirs(); // create a folder if is missing

                //copy file into resources folder
                File destFile = new File(targetFolder, selectedFile.getName());
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                String relativePath = baseFolder + selectedFile.getName();
                lblFilePath.setText(relativePath);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadCategories() {
        lstAllCategories.getItems().clear();
        try{
            lstAllCategories.getItems().setAll(categoryManager.getCategories());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lstAllCategories.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
}
