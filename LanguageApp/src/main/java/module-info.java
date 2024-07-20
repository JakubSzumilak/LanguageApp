module languageapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    opens languageapp to javafx.fxml;
    exports languageapp;
}
