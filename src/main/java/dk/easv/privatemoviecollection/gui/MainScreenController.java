package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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

    private CategoryManager categoryManager;
    private MovieManager movieManager;


    public void init(CategoryManager categoryManager, MovieManager movieManager) throws SQLException {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
                loadCategories();
                loadMovies();
    }


    public void onClickNewCategory(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(false);
        
        AddCategoryController controller = fxmlLoader.getController();
        controller.setCategoryManager(this.categoryManager);
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
        controller.init(categoryManager, movieManager);
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

    public void loadCategories() throws SQLException {
        tblCategories.getItems().clear();
        try{
            tblCategories.getItems().setAll(categoryManager.getCategories());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadMovies() throws SQLException {
        tblMovies.getItems().clear();
        try {
            tblMovies.getItems().setAll(movieManager.getAllMovies());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Category getSelectedCategory() throws SQLException {
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
    }


    public void onClickOpenInApp(ActionEvent actionEvent) throws IOException {
        Movie selectedMovie = (Movie) tblMovies.getSelectionModel().getSelectedItem();
        movieManager.openMovieInApp(selectedMovie.getFileLink());
    }
}
