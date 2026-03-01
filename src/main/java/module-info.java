module org.example.demo5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.demo5 to javafx.fxml;
    opens org.example.demo5.b_service to javafx.fxml;
    exports org.example.demo5;
    exports org.example.demo5.a_controller;
    opens org.example.demo5.a_controller to javafx.fxml;
}