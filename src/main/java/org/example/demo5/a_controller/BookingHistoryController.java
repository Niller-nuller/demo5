package org.example.demo5.a_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Booking;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BookingHistoryController {

    @FXML
    private TableView<Booking> completedBookingTable;
    @FXML
    private TableColumn<Booking, String> completedCustomerNameC;
    @FXML
    private TableColumn<Booking, String> completedTreatmentNameC;
    @FXML
    private TableColumn<Booking, String> completedTreatmentDurationC;
    @FXML
    private TableColumn<Booking, String> completedEmployeeNameC;
    @FXML
    private TableColumn<Booking, String> completedDueDateC;
    @FXML
    private TableView<Booking> canceledBookingTable;
    @FXML
    private TableColumn<Booking, String> canceledCustomerNameC;
    @FXML
    private TableColumn<Booking, String> canceledTreatmentNameC;
    @FXML
    private TableColumn<Booking, String> canceledTreatmentDurationC;
    @FXML
    private TableColumn<Booking, String> canceledEmployeeNameC;
    @FXML
    private TableColumn<Booking, String> canceledDueDateC;
    @FXML
    private Label TodayLabel;
    @FXML
    private Label feedbackLabel;
    @FXML
    private DatePicker bookingDatePick;
    private final ObservableList<Booking> canceledBookings = FXCollections.observableArrayList();
    private final ObservableList<Booking> completedBookings = FXCollections.observableArrayList();
    private BookingService bookingService;
    @FXML
    public void initialize(){
        bookingService = new BookingService();
        bookingDatePick.setValue(LocalDate.now());
        setCompletedBookingTable();
        setCanceledBookingTable();
        completedBookingTable.setItems(completedBookings);
        canceledBookingTable.setItems(canceledBookings);
        setupDatePickerListener();
        refreshTable(LocalDate.now());
        setTodayLabel();
    }
    private void setCompletedBookingTable( )  {
        completedCustomerNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCustomerName()));
        completedTreatmentNameC.setCellValueFactory(cell ->new javafx.beans.property.SimpleStringProperty(cell.getValue().getTreatmentName()));
        completedTreatmentDurationC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringTreatmentDuration()));
        completedEmployeeNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmployeeName()));
        completedDueDateC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringStartTime()));
    }
    private void setCanceledBookingTable( ) {
        canceledCustomerNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCustomerName()));
        canceledTreatmentNameC.setCellValueFactory(cell ->new javafx.beans.property.SimpleStringProperty(cell.getValue().getTreatmentName()));
        canceledTreatmentDurationC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringTreatmentDuration()));
        canceledEmployeeNameC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getEmployeeName()));
        canceledDueDateC.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getToStringStartTime()));
    }

    private void refreshTable(LocalDate bookingDate){
        feedbackLabel.setText("");
        try {
            List<Booking> completed = new ArrayList<>(bookingService.handleGetCompletedBooking(bookingDate));
            List<Booking> canceled = new ArrayList<>(bookingService.handleGetCancelledBooking(bookingDate));
            completedBookings.clear();
            canceledBookings.clear();
            completedBookings.addAll(completed);
            canceledBookings.addAll(canceled);
        } catch (RuntimeException e1) {
            feedbackLabel.setText("An error occurred while trying to refresh the booking history");
        } catch (SQLException e1) {
            feedbackLabel.setText("An error occurred while trying to connect to database");
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
        TodayLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")));
    }
}
