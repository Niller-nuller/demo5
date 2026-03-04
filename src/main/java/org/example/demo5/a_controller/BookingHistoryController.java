package org.example.demo5.a_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Booking;
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

    private SceneSwitcher sceneSwitcher;
    private BookingService bookingService;


    public void setup(BookingService bookingService, SceneSwitcher sceneSwitcher){
        this.sceneSwitcher = sceneSwitcher;
        this.bookingService = bookingService;
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
        setFeedbackLabel("");
        try {
            List<Booking> completed = new ArrayList<>(bookingService.handleGetCompletedBooking(bookingDate));
            List<Booking> canceled = new ArrayList<>(bookingService.handleGetCancelledBooking(bookingDate));
            completedBookings.clear();
            canceledBookings.clear();
            completedBookings.addAll(completed);
            canceledBookings.addAll(canceled);
        } catch (RuntimeException e) {
            setFeedbackLabel("An error occurred while trying to refresh the booking history");
        }
    }
    @FXML
    private void onClickChangeStatus(ActionEvent event){
        Booking selected = null;
        try{
        if (canceledBookingTable.getSelectionModel().getSelectedItem() != null) {
            selected = canceledBookingTable.getSelectionModel().getSelectedItem();
            checkTableObject(selected);
            setFeedbackLabel("");
            bookingService.handleCancelBooking(selected);
            setFeedbackLabel("Booking er blevet færdig gjort");
        } else if (completedBookingTable.getSelectionModel().getSelectedItem() != null) {
            selected = completedBookingTable.getSelectionModel().getSelectedItem();
            checkTableObject(selected);
            setFeedbackLabel("");
            bookingService.handleChangeBookingStatusToCompleted(selected);
            setFeedbackLabel("Booking er blevet aflyst");
        }
        }catch (RuntimeException e){
            setFeedbackLabel("An error occurred while trying to refresh the booking history");
        }
        if (selected == null) {
            setFeedbackLabel("Vælg en booking du gerne vil ændre");
        }
    }
    @FXML
    public void onClickSwitchToCreateNewBooking(ActionEvent event){
        sceneSwitcher.switchToCreateBookingView((Node) event.getSource());
    }
    @FXML
    public void onClickSwitchToActiveBookings(ActionEvent event){
        sceneSwitcher.switchToBookingView((Node) event.getSource());
    }
    @FXML
    public void onClickRefresh(ActionEvent event){
        refreshTable(LocalDate.now());
    }
    private void checkTableObject(Booking booking){
        if(booking == null){
            setFeedbackLabel("Vælg Booking du gerne vil aflyse");
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
        TodayLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yy")));
    }
    private void setFeedbackLabel(String text){
        feedbackLabel.setText(text);
    }
}
