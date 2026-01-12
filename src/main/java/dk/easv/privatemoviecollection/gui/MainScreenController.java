package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.FilterManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.gui.helpers.AlertHelper;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {
    @FXML
    private TableView<Category> tblCategories;
    @FXML
    private TableView<Movie> tblMovies;
    @FXML
    private TextField txtFilter;
    @FXML
    private TableColumn<Category, String> colCategory;
    @FXML
    private TableColumn<Movie, String> colTitle;
    @FXML
    private TableColumn<Movie, String> colImdbRating;
    @FXML
    private TableColumn<Movie, String> colMyRating;

    private FilteredList<Category> filteredCategories;
    private FilteredList<Movie> filteredMovies;

    private CategoryManager categoryManager;
    private MovieManager movieManager;
    private FilterManager filterManager;


    public void init(CategoryManager categoryManager, MovieManager movieManager, FilterManager filterManager) {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        this.filterManager = filterManager;

        // 1. changing the lists into observablelists
        ObservableList<Movie> movieObservableList = FXCollections.observableArrayList(movieManager.getAllMovies());
        ObservableList<Category> categoryObservableList = FXCollections.observableArrayList(categoryManager.getCategories());

        // 2. putting the observable lists into filtered lists
        filteredCategories = new FilteredList<>(categoryObservableList);
        filteredMovies = new FilteredList<>(movieObservableList);

        // 3. populating the tables with the filterable lists
        tblCategories.setItems(filteredCategories);
        tblMovies.setItems(filteredMovies);

        // 4. listener of the filter

        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            filterManager.filterLists(newValue, filteredCategories, filteredMovies);
        });
    }


    public void onClickNewCategory(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        
        AddCategoryController controller = fxmlLoader.getController();
        controller.init(categoryManager, this);
        stage.setTitle("Add Category");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteCategory(ActionEvent event) throws SQLException {
        if(getSelectedCategory() != null) {
            categoryManager.deleteCategory(getSelectedCategory().getId());
            loadCategories();
        }
        else return;
    }

    public void onClickAddMovie(ActionEvent event) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);

        AddMovieController controller = fxmlLoader.getController();
        controller.init(categoryManager, movieManager, this);
        stage.setTitle("Add Movie");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteMovie(ActionEvent event) throws SQLException {
        if(getSelectedMovie() != null) {
            movieManager.deleteMovie(getSelectedMovie().getId());
           loadMovies();
        }
        else return;
    }


    public void onClickEditMovie(ActionEvent event) {
    }

    public void loadCategories() {
            tblCategories.getItems().setAll(categoryManager.getCategories());
    }

    public void loadMovies() {
            tblMovies.getItems().setAll(movieManager.getAllMovies());
    }

    public Category getSelectedCategory() {
        return tblCategories.getSelectionModel().getSelectedItem();
    }

    public Movie getSelectedMovie()
    {
        return tblMovies.getSelectionModel().getSelectedItem();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCategory.setCellValueFactory( c -> new SimpleStringProperty(c.getValue().getName()));

        colTitle.setCellValueFactory(m -> new SimpleStringProperty(m.getValue().getTitle()));
        colImdbRating.setCellValueFactory(m -> {
            return new SimpleStringProperty(String.valueOf(m.getValue().getImdbRating()));
        });
        colMyRating.setCellValueFactory(m -> {
            return new SimpleStringProperty(String.valueOf(m.getValue().getMyRating()));
        });

        //added so that movies are display once a category is clicked
        tblCategories.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                loadMoviesForCategory(newValue.getId());
            }
        });

        //listener for filtering
        txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
            if(filterManager!=null && filteredCategories != null && filteredMovies != null) {
                filterManager.filterLists(newValue, filteredCategories, filteredMovies);
            }
        });

    }


    public void onClickOpenInApp(ActionEvent actionEvent) {
        Movie selectedMovie = tblMovies.getSelectionModel().getSelectedItem();

        if(selectedMovie == null || !movieManager.canOpenMovie(selectedMovie.getFileLink())){
            AlertHelper.showAlert("Choose a movie to open it");
        }

        try {
            movieManager.updateLastView(selectedMovie.getId());
            Desktop.getDesktop().open(new File(selectedMovie.getFileLink()));
        } catch (IOException e){
            AlertHelper.showAlert("Could not open the file");
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMoviesForCategory(int categoryId) {
        try {
            tblMovies.setItems(FXCollections.observableArrayList(categoryManager.getAllMoviesForCategory(categoryId)));
            }
        catch (SQLException e) {
            AlertHelper.showAlert("Could not load movies for Category");
            e.printStackTrace();
        }
    }

    public void showAllMovies(ActionEvent event) {
        tblMovies.getItems().clear();
        loadMovies();
    }

    public void runStartupChecks() throws SQLException {
        movieManager.shouldWarnAboutOldAndLowRatedMovies();
        AlertHelper.showAlert("You have movies with a personal rating under 6\n" +
                "that have not been opened in more than 2 years.");
    }
}