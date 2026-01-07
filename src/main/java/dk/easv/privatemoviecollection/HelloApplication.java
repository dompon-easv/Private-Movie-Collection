package dk.easv.privatemoviecollection;

import dk.easv.privatemoviecollection.bll.CategoryManager;
import dk.easv.privatemoviecollection.bll.MovieManager;
import dk.easv.privatemoviecollection.dal.dao.CategoryDao;
import dk.easv.privatemoviecollection.dal.ConnectionManager;
import dk.easv.privatemoviecollection.dal.dao.MovieDao;
import dk.easv.privatemoviecollection.dal.daoInterface.ICategoryDao;
import dk.easv.privatemoviecollection.gui.MainScreenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException, SQLException {

        ConnectionManager connectionManager = new ConnectionManager();

        CategoryDao categoryDao = new CategoryDao(connectionManager);
        MovieDao movieDao = new MovieDao(connectionManager);

        CategoryManager categoryManager = new CategoryManager(categoryDao);
        MovieManager movieManager = new MovieManager(movieDao);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/dk/easv/privatemoviecollection/gui/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        MainScreenController controller = fxmlLoader.getController();
        controller.init(categoryManager, movieManager);
        stage.setTitle("Movie Collection");
        stage.setScene(scene);
        stage.show();
    }
}
