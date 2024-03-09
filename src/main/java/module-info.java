module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jasperreports;
    requires itextpdf;

    opens org.example to javafx.fxml,javafx.base;
    opens org.example.modelo to javafx.fxml,javafx.base;
    exports org.example;
}
