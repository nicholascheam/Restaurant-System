module com.example.restaurantsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.restaurantsystem to javafx.fxml;
    exports com.example.restaurantsystem;
}