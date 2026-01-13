package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
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
import java.util.List;
import java.util.ResourceBundle;

public class AddEditMovieController implements Initializable {

    @FXML private TextField txtTitle;
    @FXML private TextField txtIMDBRating;
    @FXML private TextField txtMyRating;
    @FXML private ListView<Category> lstAllCategories;
    @FXML private ListView<Category> lstChosenCategories;
    @FXML private Label lblFilePath;

    private ObservableList<Movie> movieList = FXCollections.observableArrayList();
    private Movie movie;
    private MovieAddEditMode mode;

    public void setMovieList(ObservableList<Movie> movieList) {this.movieList = movieList;}

    private MovieManager movieManager;
    private CategoryManager categoryManager;
    private MainScreenController mainScreenController;

    public void init(CategoryManager categoryManager, MovieManager movieManager, MainScreenController mainScreenController) throws SQLException {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        this.mainScreenController = mainScreenController;
        loadCategories();
        setupCategoryDoubleClick();

    }

    private void setupCategoryDoubleClick() {

        // Double-click in ALL → move to CHOSEN
        lstAllCategories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Category selected =
                        lstAllCategories.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    lstAllCategories.getItems().remove(selected);
                    lstChosenCategories.getItems().add(selected);
                }
            }
        });

        // Double-click in CHOSEN → move back to ALL
        lstChosenCategories.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Category selected =
                        lstChosenCategories.getSelectionModel().getSelectedItem();
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

    public void onClickSave(ActionEvent event) throws SQLException {

        String title = txtTitle.getText();
        double imdbRating = Double.parseDouble(txtIMDBRating.getText());
        double myRating = Double.parseDouble(txtMyRating.getText());
        String filePath = lblFilePath.getText();

        if (filePath == null || filePath.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("File path is required");
            alert.showAndWait();
            return;}

        if (mode == MovieAddEditMode.ADD) {Movie newMovie = movieManager.addMovie( title, imdbRating, myRating, filePath );
            categoryManager.updateMovieCategories(newMovie.getId(),
                    lstChosenCategories.getItems() );

        } else { // eidt/update motherfuckers

            movie.setTitle(title);
            movie.setImdbRating(imdbRating);
            movie.setMyRating(myRating);
            movie.setFileLink(filePath);

            movieManager.updateMovie(movie);
            categoryManager.updateMovieCategories(movie.getId(), lstChosenCategories.getItems() );
        }
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

    private void populateFields() throws SQLException {
        txtTitle.setText(movie.getTitle());
        txtIMDBRating.setText(String.valueOf(movie.getImdbRating()));
        txtMyRating.setText(String.valueOf(movie.getMyRating()));
        lblFilePath.setText(movie.getFileLink());
        List<Category> movieCategories =
                categoryManager.getCategoriesForMovie(movie.getId()); // maybeworks
        lstChosenCategories.getItems().setAll(movieCategories);
        lstAllCategories.getItems().removeAll(movieCategories);
    }

// should we delete this?
    public void selectCategory() {
        Category selected = lstAllCategories.getSelectionModel().getSelectedItem();
        ObservableList<Category> selectedCategories = lstChosenCategories.getItems();
        if (!selectedCategories.contains(selected)) {
            selectedCategories.add(selected);
            lstAllCategories.getItems().remove(selected);
        }

    }

    public void initEdit(CategoryManager categoryManager, MovieManager movieManager, MainScreenController mainScreenController,
                         Movie movie) throws SQLException {

        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        this.mainScreenController = mainScreenController;
        this.movie = movie;
        this.mode = MovieAddEditMode.EDIT;
       loadCategories();
        populateFields();
        setupCategoryDoubleClick();

    }


}
