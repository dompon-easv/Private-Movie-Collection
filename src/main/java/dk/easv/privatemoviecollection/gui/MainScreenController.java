package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.model.Category;
import javafx.application.Platform;
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
    private TableView tblMovies;
    @FXML
    private TextField txtFilter;
    @FXML
    private TableColumn<Category, String> colCategory;

    private CategoryManager categoryManager;
    private MovieManager movieManager;

    /*public void initialize() throws SQLException {
        colCategory.setCellValueFactory( new PropertyValueFactory<>("name"));
    }*/


    public void init(CategoryManager categoryManager, MovieManager movieManager) throws SQLException {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
                loadCategories();

    }


    public void onClickNewCategory(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        
        AddCategoryController controller = fxmlLoader.getController();
        controller.setCategoryManager(this.categoryManager);
        stage.setTitle("Add Category");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteCategory(ActionEvent event) {
    }

    public void onClickAddMovie(ActionEvent event) throws IOException, SQLException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        AddMovieController controller = fxmlLoader.getController();
        controller.init(categoryManager, movieManager);
        stage.setTitle("Add Movie");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteMovie(ActionEvent event) {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colCategory.setCellValueFactory( c -> new SimpleStringProperty(c.getValue().getName()));
    }
}
