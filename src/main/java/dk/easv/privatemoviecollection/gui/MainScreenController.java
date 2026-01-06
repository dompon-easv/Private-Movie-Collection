package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MainScreenController {
    @FXML
    private TableView tblCategories;
    @FXML
    private TableView tblMovies;
    @FXML
    private TextField txtFilter;

    private CategoryManager categoryManager;
    private MovieManager movieManager;

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

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;

    }

    public void setMovieManager(MovieManager movieManager) {
        this.movieManager = movieManager;
    }
}
