module com.example.var11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;


    opens com.example.var11 to javafx.fxml;
    exports com.example.var11;
}