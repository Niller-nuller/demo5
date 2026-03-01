package org.example.demo5.a_controller;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Booking;
import org.example.demo5.c_model.BookingStatus;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class BookingController {
    private BookingService service;
    @FXML
    private TableView<Booking> bookingTable;
    @FXML
    private TableColumn<Booking, String> customerNameC;
    @FXML
    private TableColumn<Booking, String> treatmentNameC;
    @FXML
    private TableColumn<Booking, String> treatmentDurationC;
    @FXML
    private TableColumn<Booking, String> employeeNameC;
    @FXML
    private TableColumn<Booking, String> dueDateC;
    @FXML
    private Label TodayLabel;

    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        service = new BookingService();
//        refreshTable();
        setBookingTableValues();
        setTodayLabel();
    }

    private void refreshTable(){
        try{
            List<Booking> booked = service.handleGetPendingBookings();
            bookings.setAll(booked);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setBookingTableValues(){
        customerNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCustomerName()));
        treatmentNameC.setCellValueFactory(cell ->new javafx.beans.property.SimpleStringProperty(cell.getValue().getTreatmentName()));
        treatmentDurationC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringTreatmentDuration()));
        employeeNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmployeeName()));
        dueDateC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringStartTime()));
    }
    private void setTodayLabel(){
        TodayLabel.setText(LocalDateTime.now().toString());
    }


}
