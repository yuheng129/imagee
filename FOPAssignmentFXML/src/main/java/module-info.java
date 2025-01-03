module com.mycompany.fopassignmentfxml {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires javafx.graphics;

    opens com.mycompany.fopassignmentfxml to javafx.fxml;
    exports com.mycompany.fopassignmentfxml;
}
