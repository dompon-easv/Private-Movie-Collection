module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;


    opens dk.easv.privatemoviecollection.gui to javafx.fxml;
    exports dk.easv.privatemoviecollection;
}