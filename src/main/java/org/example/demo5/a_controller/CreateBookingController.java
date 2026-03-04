package org.example.demo5.a_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Employee;
import org.example.demo5.c_model.Treatment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class CreateBookingController {

    private BookingService bookingService;
    private SceneSwitcher sceneSwitcher;

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();
    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();

    @FXML private TextField customerNameField;
    @FXML private TextField customerPhoneField;
    @FXML private TextField customerEmailField;
    @FXML private DatePicker bookingDatePicker;
    @FXML private ComboBox<Employee> employeeComboBox;
    @FXML private ComboBox<Treatment> treatmentComboBox;
    @FXML private ComboBox<LocalTime> startTimeCombo;
    @FXML private ListView<String> availabilityList;
    @FXML private Label feedbackLabel;
    @FXML private Button createBookingButton;

    @FXML
    public void initialize(BookingService bookingService, SceneSwitcher sceneSwitcher) {
        this.sceneSwitcher = sceneSwitcher;
        this.bookingService = bookingService;
        bookingDatePicker.setValue(LocalDate.now());
        loadEmployees();
        loadTreatments();
        setupDateListener();
    }
    @FXML
    private void onCheckAvailability() {
        Employee selectedEmployee = employeeComboBox.getValue();
        Treatment selectedTreatment = treatmentComboBox.getValue();
        LocalDate date = bookingDatePicker.getValue();

        if (selectedEmployee == null || selectedTreatment == null || date == null) {
            setFeedbackLabel("Vælg medarbejder, behandling og dato først");
            return;
        }

        try {
            List<LocalTime> availableTimes = bookingService.handleGetAvailableTimes(selectedEmployee, date, selectedTreatment);
            startTimeCombo.setItems(FXCollections.observableArrayList(availableTimes));

            if (availableTimes.isEmpty()) {
                setFeedbackLabel("Ingen ledige tider for " + selectedEmployee.getName());
                createBookingButton.setDisable(true);
            } else {
                setFeedbackLabel(availableTimes.size() + " ledige tider fundet");
                createBookingButton.setDisable(false);
                availabilityList.getItems().clear();
                availabilityList.getItems().addAll(
                        availableTimes.stream()
                                .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")) + " (" + selectedTreatment.getDurationMinutes() + " min)")
                                .toList()
                );
            }
        } catch (Exception e) {
            setFeedbackLabel("Fejl: " + e.getMessage());
        }
    }
    @FXML
    private void onCreateBooking() {
        try {
            String customerName = customerNameField.getText().trim();
            String customerPhone = customerPhoneField.getText().trim();
            String customerEmail = customerEmailField.getText().trim();
            Employee employee = employeeComboBox.getValue();
            Treatment treatment = treatmentComboBox.getValue();
            LocalDate date = bookingDatePicker.getValue();
            LocalTime startTime = startTimeCombo.getValue();

            if (customerName.isEmpty() || customerPhone.isEmpty() || customerEmail.isEmpty() || employee == null ||
                    treatment == null || date == null || startTime == null) {
                setFeedbackLabel("Udfyld alle felter");
                return;
            }
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            bookingService.handleCreateABooking(customerName,customerPhone,customerEmail,employee,treatment,startDateTime);

            setFeedbackLabel("Booking oprettet for " + customerName + " kl. " + startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            clearForm();

        } catch (RuntimeException e) {
            setFeedbackLabel("Fejl ved oprettelse: Kunne ikke oprette forbindelse til database");
        }
    }
    @FXML
    private void onBackToBookings(ActionEvent event) {
        sceneSwitcher.switchToBookingView((Node) event.getSource());
    }
    private void setupDateListener() {
        bookingDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                startTimeCombo.getItems().clear();
                availabilityList.getItems().clear();
            }
        });
    }
    private void loadEmployees() {
        try {
            employees.clear();
            employees.addAll(bookingService.handleGetAllEmployees());
        } catch (RuntimeException e) {
            setFeedbackLabel("Fejl ved indlæsning af medarbejdere");
        }
    }
    private void loadTreatments() {
        try {
            treatments.clear();
            treatments.addAll(bookingService.handleGetAllTreatments());
        } catch (Exception e) {
            setFeedbackLabel("Fejl ved indlæsning af behandlinger");
        }
    }
    private void clearForm() {
        customerNameField.clear();
        customerPhoneField.clear();
        startTimeCombo.getItems().clear();
        availabilityList.getItems().clear();
        setFeedbackLabel("Booking oprettet!");
    }
    private void setFeedbackLabel(String text){
        feedbackLabel.setText(text);
    }
}
