package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.model.Category;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainScreenController {
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

    public void initialize() {
    colCategory.setCellValueFactory( new PropertyValueFactory<>("category"));
        }


    public void init(CategoryManager categoryManager, MovieManager movieManager) throws SQLException {
        this.categoryManager = categoryManager;
        this.movieManager = movieManager;
        Platform.runLater(() -> {
            try {
                loadCategories();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
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

    public void onClickAddMovie(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        AddMovieController controller = fxmlLoader.getController();
        controller.setMovieManager(this.movieManager);
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
        categoryManager.getCategories().forEach(c -> System.out.println(c.getName()));

    }
}
