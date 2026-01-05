module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;


    opens dk.easv.privatemoviecollection to javafx.fxml;
    exports dk.easv.privatemoviecollection;
}