package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.bll.exceptions.CategoryException;
import dk.easv.privatemoviecollection.bll.exceptions.MovieException;
import dk.easv.privatemoviecollection.gui.helpers.AlertHelper;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddEditMovieController  {

    @FXML private TextField txtTitle;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtMyRating;
    @FXML private ListView<Category> lstAllCategories;
    @FXML private ListView<Category> lstChosenCategories;
    @FXML private Label lblFilePath;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private Movie movie;
    private MovieAddEditMode mode;


    private MovieManager movieManager;
    private CategoryManager categoryManager;
    private MainScreenController mainScreenController;

    public void initAdd(CategoryManager categoryManager,
                        MovieManager movieManager,
                        MainScreenController mainScreenController) {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        this.mainScreenController = mainScreenController;
        this.mode = MovieAddEditMode.ADD;
        this.movie = null;
        loadCategories();
        setupCategoryDoubleClick();
    }


    private void setupCategoryDoubleClick() {

        // Double-click in ALL → move to CHOSEN
        lstAllCategories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Category selected = lstAllCategories.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    lstAllCategories.getItems().remove(selected);
                    lstChosenCategories.getItems().add(selected);}
            }
        });

        // Double-click in CHOSEN → move back to ALL
        lstChosenCategories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Category selected = lstChosenCategories.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    lstChosenCategories.getItems().remove(selected);
                    lstAllCategories.getItems().add(selected);
                }
            }
        });
    }



    public void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void onClickSave(ActionEvent event) {


        String title = txtTitle.getText();
        String imdbText = txtIMDBRating.getText();
        String myRatingText = txtMyRating.getText();
        String filePath = lblFilePath.getText();

        // Validates required fields and showing alert to user
        List<String> missingFields = new ArrayList<>();
        if (title == null || title.isBlank()) missingFields.add("Title");
        if (imdbText == null || imdbText.isBlank()) missingFields.add("IMDB Rating");
        if (myRatingText == null || myRatingText.isBlank()) missingFields.add("My Rating");
        if (filePath == null || filePath.isBlank()) missingFields.add("File path");

        if (!missingFields.isEmpty()) {
            AlertHelper.showAlert(
                    "Please fill out all the required fields: " +
                            String.join(", ", missingFields)
            );
            return;
        }

        double imdbRating;
        double myRating;

        try {
            imdbRating = Double.parseDouble(imdbText);
            myRating = Double.parseDouble(myRatingText);
        } catch (NumberFormatException e) {
            AlertHelper.showAlert("Please enter valid numeric ratings");
            return;
        }

        // Safety check
        if (mode == MovieAddEditMode.EDIT && movie == null) {
            throw new IllegalStateException("EDIT mode active but movie is null");
        }

        // Add vs Edit
        try {
            if (mode == MovieAddEditMode.ADD) {
                Movie newMovie = movieManager.addMovie(title, imdbRating, myRating, filePath);

                // Add each chosen category we create the relationship to DB
                for (Category category : lstChosenCategories.getItems()) {
                    categoryManager.addMovieToCategory(newMovie.getId(), category.getId());
                }

            } else { // edit

                movie.setTitle(title);
                movie.setImdbRating(imdbRating);
                movie.setMyRating(myRating);
                movie.setFileLink(filePath);
                movieManager.updateMovie(movie);
                categoryManager.updateMovieCategories(
                        movie.getId(),
                        lstChosenCategories.getItems()
                );
            }
        }
        catch (IllegalArgumentException | MovieException | CategoryException e)
            { AlertHelper.showAlert(e.getMessage()); }

        mainScreenController.loadMovies();

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene().getWindow();
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

            }catch (IOException e){
                AlertHelper.showAlert("Could not copy file");
            }
        }
    }

    public void loadCategories() {
        lstAllCategories.getItems().clear();
        lstAllCategories.getItems().setAll(categoryManager.getCategories());

    }


    private void populateFields()  {
        txtTitle.setText(movie.getTitle());
        txtIMDBRating.setText(String.valueOf(movie.getImdbRating()));
        txtMyRating.setText(String.valueOf(movie.getMyRating()));
        lblFilePath.setText(movie.getFileLink());

            List<Category> movieCategories =
                    categoryManager.getCategoriesForMovie(movie.getId());
        lstChosenCategories.getItems().setAll(movieCategories);
        lstAllCategories.getItems().removeAll(movieCategories);
    }



    public void initEdit(CategoryManager categoryManager, MovieManager movieManager, MainScreenController mainScreenController,
                         Movie movie) {

        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        this.mainScreenController = mainScreenController;
        this.movie = movie;
        this.mode = MovieAddEditMode.EDIT;
        try {
            loadCategories();
            populateFields();
        }catch (CategoryException | MovieException e) {
            AlertHelper.showAlert(e.getMessage());
        }
        setupCategoryDoubleClick();
    }

}
