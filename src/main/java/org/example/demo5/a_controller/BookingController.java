package org.example.demo5.a_controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Booking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BookingController {

    private BookingService bookingService;
    private SceneSwitcher sceneSwitcher;

    @FXML private TableView<Booking> bookingTable;
    @FXML private TableColumn<Booking, String> customerNameC;
    @FXML private TableColumn<Booking, String> treatmentNameC;
    @FXML private TableColumn<Booking, String> treatmentDurationC;
    @FXML private TableColumn<Booking, String> employeeNameC;
    @FXML private TableColumn<Booking, String> dueDateC;
    @FXML private Label TodayLabel;
    @FXML private Label feedbackLabel;
    @FXML private DatePicker bookingDatePick;

    private final ObservableList<Booking> bookings = FXCollections.observableArrayList();

    @FXML
    public void initialize(BookingService bookingService, SceneSwitcher sceneSwitcher){
        this.sceneSwitcher = sceneSwitcher;
        this.bookingService = bookingService;
        bookingDatePick.setValue(LocalDate.now());
        setupDatePickerListener();
        setBookingTableValues();
        bookingTable.setItems(bookings);
        refreshTable(LocalDate.now());
        setTodayLabel();
    }
    @FXML
    private void onCancelButtonClick(ActionEvent event){
        try{
            Booking selected = bookingTable.getSelectionModel().getSelectedItem();
            checkTableObject(selected);
            setFeedbackLabel("");
            bookingService.handleCancelBooking(selected);
            setFeedbackLabel("Booking er blevet aflyst");
            refreshTable(bookingDatePick.getValue());
        } catch (RuntimeException e){
            setFeedbackLabel("An error has occurred when trying to connect to the server");
        }
    }
    @FXML
    public void onClickSwitchToCreateNewBooking(ActionEvent event){
        sceneSwitcher.switchToCreateBookingView((Node) event.getSource());
    }
    @FXML
    public void onClickSwitchToBookingHistory(ActionEvent event){
        sceneSwitcher.switchToBookingHistoryView((Node) event.getSource());
    }
    @FXML
    public void onClickRefresh(ActionEvent event){
        refreshTable(bookingDatePick.getValue());
    }

    private void refreshTable(LocalDate date) {
        bookings.clear();
        try {
            List<Booking> booked = bookingService.handleGetPendingBookings(date);
            bookings.clear();  // Prevents NPE
            if (booked != null) {
                bookings.addAll(booked);  // Safer than setAll()
                setFeedbackLabel(booked.isEmpty() ? "Ingen bookinger" : booked.size() + " fundet");
            }
        } catch (RuntimeException e) {
            bookings.clear();
           setFeedbackLabel("Fejl: Kunne ikke skabe forbindelse til database");
        }
    }
    private void setBookingTableValues() {
        customerNameC.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCustomerName()));

        treatmentNameC.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getTreatmentName()));

        treatmentDurationC.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getToStringTreatmentDuration()));

        employeeNameC.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getEmployeeName()));

        dueDateC.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getToStringStartTime()));
    }

    private void setupDatePickerListener(){ // AI genereaet kode.
        bookingDatePick.valueProperty().addListener((obs,oldDate, newDate) -> {
            if (newDate != null)
            {
                refreshTable(newDate);
            }
        });
    }
    private void checkTableObject(Booking booking){
        if(booking == null){
            feedbackLabel.setText("Vælg Booking du gerne vil aflyse");
            throw new IllegalArgumentException();
        }
    }
    private void setTodayLabel(){
        TodayLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yy hh/mm")));
    }
    public void setFeedbackLabel(String text){
        feedbackLabel.setText(text);
    }


}
