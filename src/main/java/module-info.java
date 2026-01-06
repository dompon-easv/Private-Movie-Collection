module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;


    opens dk.easv.privatemoviecollection.gui to javafx.fxml;
    exports dk.easv.privatemoviecollection;
}