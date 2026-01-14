package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.FilterManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.bll.exceptions.CategoryException;
import dk.easv.privatemoviecollection.bll.exceptions.MovieException;
import dk.easv.privatemoviecollection.gui.helpers.AlertHelper;
import dk.easv.privatemoviecollection.model.Category;
import dk.easv.privatemoviecollection.model.Movie;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML private TableView<Category> tblCategories;
    @FXML private TableView<Movie> tblMovies;
    @FXML private TextField txtFilter;
    @FXML private TableColumn<Category, String> colCategory;
    @FXML private TableColumn<Movie, String> colTitle;
    @FXML private TableColumn<Movie, String> colImdbRating;
    @FXML private TableColumn<Movie, String> colMyRating;

    private ObservableList<Category> categoryObservableList;
    private ObservableList<Movie> movieObservableList;

    private FilteredList<Category> filteredCategories;
    private FilteredList<Movie> filteredMovies;

    private SortedList<Movie> sortedMovies;
    private SortedList<Category> sortedCategories;

    private CategoryManager categoryManager;
    private MovieManager movieManager;
    private FilterManager filterManager;


    public void init(CategoryManager categoryManager, MovieManager movieManager, FilterManager filterManager) throws SQLException {
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

    public void onClickDeleteCategory(ActionEvent event) {
        Category selected = getSelectedCategory();
        if(selected!= null) {
            try {
                categoryManager.deleteCategory(selected.getId());
                loadCategories();
            }catch (CategoryException e){
                AlertHelper.showAlert(e.getMessage());
            }
        }
        else AlertHelper.showAlert("Please select a category");
    }

    public void onClickAddMovie(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddEditMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);

        AddEditMovieController controller = fxmlLoader.getController();
        controller.initAdd(categoryManager, movieManager, this);
        stage.setTitle("Add Movie");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteMovie(ActionEvent event)  {
        Movie selected = getSelectedMovie();
        if(selected != null) {
            try{
            movieManager.deleteMovie(selected.getId());
           loadMovies();
            }catch (MovieException e){
                AlertHelper.showAlert(e.getMessage());
            }
        }else AlertHelper.showAlert("Please select a movie");
    }


    public void onClickEditMovie(ActionEvent event)  throws IOException, SQLException {

        Movie selectedMovie = getSelectedMovie();

        if (selectedMovie == null) {Alert alert = new Alert(Alert.AlertType.WARNING,
                    "Please select a movie to edit"); alert.showAndWait();
            return;
        }

        Stage stage = new Stage(); FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(
                "/dk/easv/privatemoviecollection/gui/AddEditMovie.fxml") );
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        AddEditMovieController controller = fxmlLoader.getController();

        // EDIT MODE -on progress XD
        controller.initEdit(
                categoryManager,
                movieManager,
                this,
                selectedMovie
        );

        stage.setTitle("Edit Movie");
        stage.setScene(scene);
        stage.show();
    }



    public void loadCategories() {
        tblCategories.setItems(
                FXCollections.observableArrayList(
                        categoryManager.getCategories()
                )
        );
    }



    public void loadMovies()  {
        try{
            tblMovies.setItems(
                    FXCollections.observableArrayList(
                            movieManager.getAllMovies()
                    )
            );
        }catch (MovieException e){
            AlertHelper.showAlert(e.getMessage());
        }
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

        if(selectedMovie == null) {
            AlertHelper.showAlert("Select a movie to open");
            return;
        }

        if(!movieManager.canOpenMovie(selectedMovie.getFileLink())){
            AlertHelper.showAlert("File does not exist as in the path:\n" + selectedMovie.getFileLink());
            return;
        }

        try {
            movieManager.updateLastView(selectedMovie.getId());
            Desktop.getDesktop().open(new File(selectedMovie.getFileLink()));
        } catch (IOException e){
            AlertHelper.showAlert("Could not open the movie");
        } catch (RuntimeException e) {
            AlertHelper.showAlert("Could not update last view date");
        }
    }

    private void loadMoviesForCategory(int categoryId) {
        try {
            tblMovies.setItems(FXCollections.observableArrayList(categoryManager.getAllMoviesForCategory(categoryId)));
            }
        catch (RuntimeException e) {
            AlertHelper.showAlert("Could not load movies for the selected category");
        }
    }

    public void showAllMovies(ActionEvent event) {
        try{
            loadMovies();
        }catch (RuntimeException e) {
            AlertHelper.showAlert(e.getMessage()); }
    }

    public void runStartupChecks()  {
        try {
            if (movieManager.shouldWarnAboutOldAndLowRatedMovies()) {
                AlertHelper.showAlert("You have movies with a personal rating under 6\n" +
                        "that have not been opened in more than 2 years.");
            }
        } catch (MovieException e) {
            AlertHelper.showAlert("Could not run startup checks");
            e.printStackTrace();
        }
    }
}