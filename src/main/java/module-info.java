module dk.easv.privatemoviecollection {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.graphics;
    requires java.sql;
    requires com.microsoft.sqlserver.jdbc;
    requires java.naming;
   // requires dk.easv.privatemoviecollection;
    // requires dk.easv.privatemoviecollection;


    opens dk.easv.privatemoviecollection.gui to javafx.fxml;
    exports dk.easv.privatemoviecollection;
}