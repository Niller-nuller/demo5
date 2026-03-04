package org.example.demo5.a_controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo5.HelloApplication;
import org.example.demo5.b_service.BookingService;

import java.io.IOException;

public class SceneSwitcher {

    private final BookingService bookingService;

    public SceneSwitcher(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public void switchToBookingView(Node source) {
        switchScene(source, "booking-view.fxml");
    }

    public void switchToBookingHistoryView(Node source) {
        switchScene(source, "bookinghistory-view.fxml");
    }

    public void switchToCreateBookingView(Node source) {
        switchScene(source, "createbooking-view.fxml");
    }

    private void switchScene(Node source, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource(fxmlFile));
            Scene scene = new Scene(loader.load());

            Object controller = loader.getController();
            if (controller instanceof BookingController bc) {
                bc.initialize(bookingService, this);
            } else if (controller instanceof BookingHistoryController bhc) {
                bhc.initialize(bookingService, this);
            } else if (controller instanceof CreateBookingController cbc) {
                cbc.initialize(bookingService, this);
            }

            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load scene: " + fxmlFile, e);
        }
    }
}
