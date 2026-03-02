package org.example.demo5.a_controller;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Booking;
import org.example.demo5.c_model.BookingStatus;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @FXML
    private Label feedbackLabel;
    @FXML
    private DatePicker bookingDatePick;
    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize(){
        service = new BookingService();
        bookingDatePick.setValue(LocalDate.now());
        setupDatePickerListener();
        refreshTable(LocalDate.now());
        setBookingTableValues();
        setTodayLabel();
    }

    private void refreshTable(LocalDate date){
        try{
            List<Booking> booked = service.handleGetPendingBookings(date);
            bookings.setAll(booked);
        } catch (SQLException e) {
            feedbackLabel.setText("An error has occurred when trying to connect to database.");
        }
    }
    private void setBookingTableValues(){
        customerNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCustomerName()));
        treatmentNameC.setCellValueFactory(cell ->new javafx.beans.property.SimpleStringProperty(cell.getValue().getTreatmentName()));
        treatmentDurationC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringTreatmentDuration()));
        employeeNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmployeeName()));
        dueDateC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringStartTime()));
    }
    private void onCancelButtonClick(ActionEvent event){
        try{
            Booking selected = bookingTable.getSelectionModel().getSelectedItem();
            checkTableObject(selected);
            feedbackLabel.setText("");
            service.cancelBooking(selected);
            feedbackLabel.setText("Booking er blevet aflyst");
        } catch (SQLException e){
            setFeedbackLabel("An error has occurred when trying to connect to the server");
        }

    }
    private void checkTableObject(Booking booking){
        if(booking == null){
            feedbackLabel.setText("Vælg Booking du gerne vil aflyse");
            throw new IllegalArgumentException();
        }
    }
    private void setupDatePickerListener(){ // AI genereaet kode.
        bookingDatePick.valueProperty().addListener((obs,oldDate, newDate) -> {
            if (newDate != null)
            {
                refreshTable(newDate);
            }
        });
    }
    private void setTodayLabel(){
        TodayLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh/mm")));
    }
    public void setFeedbackLabel(String text){
        feedbackLabel.setText(text);
    }


}
