package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.gui.helpers.AlertHelper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AddMovieController implements Initializable {

    @FXML private TextField txtTitle;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtMyRating;
    @FXML private ListView<Category> lstAllCategories;
    @FXML private ListView<Category> lstChosenCategories;
    @FXML private Label lblFilePath;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
    public void setMovieList(ObservableList<Movie> movieList) {this.movieList = movieList;}

    private MovieManager movieManager;
    private CategoryManager categoryManager;
    private MainScreenController mainScreenController;

    public void init(CategoryManager categoryManager, MovieManager movieManager, MainScreenController mainScreenController) {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        this.mainScreenController = mainScreenController;
        loadCategories();
        handleDoubleClick();

    }

    private void handleDoubleClick() {
            lstAllCategories.setItems(FXCollections.observableArrayList(categoryManager.getCategories()));

        lstAllCategories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Category selected = lstAllCategories.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    selectCategory();
                }
            }
        });

    }


    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onClickSave(ActionEvent event) throws SQLException {
        String title = txtTitle.getText();
        String imdbText = txtIMDBRating.getText(); //need to use String then change it to double
        String myRatingText = txtMyRating.getText();
        String filePath = lblFilePath.getText();

        if(!validateInputs(title, imdbText,myRatingText, filePath)) return;

        double imdbRating;
        double myRating;

        try{
            imdbRating = Double.parseDouble(imdbText);
            myRating = Double.parseDouble(myRatingText);
        } catch (NumberFormatException e) {
            AlertHelper.showAlert("Please enter a valid rating");
            return;
        }
        try {
            Movie newMovie = movieManager.addMovie(title, imdbRating, myRating, filePath);
            assignCategories(newMovie);
        }catch (IllegalArgumentException e){
            AlertHelper.showAlert((e.getMessage()));
        }

        closeStage(event);

    }

    private boolean validateInputs(String title, String imdbRating, String myRating, String filePath) {
        //Check if textfields are empty
        List<String> missingFields = new ArrayList<>();
        if(title.isEmpty()) missingFields.add("Title");
        if(imdbRating.isEmpty()) missingFields.add("IMDB Rating");
        if(myRating.isEmpty()) missingFields.add("My Rating");
        if(filePath.isEmpty()) missingFields.add("File path");

        if(!missingFields.isEmpty()) {
            AlertHelper.showAlert("Please fill out all the required fields " + String.join (", ",  missingFields));
            return false;
        }
        return true;
    }

    private void assignCategories(Movie movie){
        ObservableList<Category> selectedCategories = lstAllCategories.getSelectionModel().getSelectedItems();
        if(selectedCategories != null && !selectedCategories.isEmpty()) {
            for (Category category : selectedCategories) {
                categoryManager.addMovieToCategory(movie.getId(), category.getId());
            }
            mainScreenController.loadMovies();
        }
    }

    private void closeStage(ActionEvent event) {
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
                if(relativePath.endsWith(".mp4") || relativePath.endsWith(".mpeg4")) {
                lblFilePath.setText(relativePath);}
                else { lblFilePath.setText("Incorrect file format!");}

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void loadCategories() {
        lstAllCategories.getItems().clear();
        lstAllCategories.getItems().setAll(categoryManager.getCategories());

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       lstAllCategories.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }


    public void selectCategory() {
        Category selected = lstAllCategories.getSelectionModel().getSelectedItem();
        ObservableList<Category> selectedCategories = lstChosenCategories.getItems();
        if (!selectedCategories.contains(selected)) {
            selectedCategories.add(selected);
            lstAllCategories.getItems().remove(selected);
        }

    }
}
