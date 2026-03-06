package org.example.demo5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.demo5.I_Interface.IBookingRepository;
import org.example.demo5.I_Interface.IBookingService;
import org.example.demo5.I_Interface.IPersonRepository;
import org.example.demo5.I_Interface.ITreatmentRepository;
import org.example.demo5.a_controller.BookingController;
import org.example.demo5.a_controller.SceneSwitcher;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.e_dal.BookingRepository;
import org.example.demo5.e_dal.PersonRepository;
import org.example.demo5.e_dal.TreatmentRepository;

import java.io.IOException;

public class HelloApplication extends Application {

    public void start(Stage stage) throws IOException {

        IBookingRepository bookingRepo = new BookingRepository();
        IPersonRepository personRepo = new PersonRepository();
        ITreatmentRepository treatmentRepo = new TreatmentRepository();
        IBookingService bookingService = new BookingService(bookingRepo, personRepo,treatmentRepo);
        SceneSwitcher sceneSwitcher = new SceneSwitcher(bookingService);
        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("booking-view.fxml"));
        Scene scene = new Scene(loader.load(), 1100, 800);
        BookingController controller = loader.getController();
        controller.setup(bookingService,sceneSwitcher);
        stage.setScene(scene);
        stage.show();
    }
}
