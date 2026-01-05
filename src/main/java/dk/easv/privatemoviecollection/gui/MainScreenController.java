package dk.easv.privatemoviecollection.gui;

import dk.easv.privatemoviecollection.HelloApplication;
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

    public void onClickNewCategory(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddCategory.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movie Collection");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteCategory(ActionEvent event) {
    }

    public void onClickAddMovie(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/AddMovie.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Movie Collection");
        stage.setScene(scene);
        stage.show();
    }

    public void onClickDeleteMovie(ActionEvent event) {
    }

    public void onClickEditMovie(ActionEvent event) {
    }
}
