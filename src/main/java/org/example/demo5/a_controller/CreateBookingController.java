package org.example.demo5.a_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.demo5.b_service.BookingService;
import org.example.demo5.c_model.Customer;
import org.example.demo5.c_model.Employee;
import org.example.demo5.c_model.Treatment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CreateBookingController {
    private final BookingService service = new BookingService();
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
    public void initialize() {
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
            feedbackLabel.setText("Vælg medarbejder, behandling og dato først");
            return;
        }

        try {
            List<LocalTime> availableTimes = service.handleGetAvailableTimes(selectedEmployee, date, selectedTreatment);
            startTimeCombo.setItems(FXCollections.observableArrayList(availableTimes));

            if (availableTimes.isEmpty()) {
                feedbackLabel.setText("Ingen ledige tider for " + selectedEmployee.getName());
                createBookingButton.setDisable(true);
            } else {
                feedbackLabel.setText(availableTimes.size() + " ledige tider fundet");
                createBookingButton.setDisable(false);
                availabilityList.getItems().clear();
                availabilityList.getItems().addAll(
                        availableTimes.stream()
                                .map(time -> time.format(DateTimeFormatter.ofPattern("HH:mm")) + " (" + selectedTreatment.getDurationMinutes() + " min)")
                                .toList()
                );
            }
        } catch (Exception e) {
            feedbackLabel.setText("Fejl: " + e.getMessage());
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
                feedbackLabel.setText("Udfyld alle felter");
                return;
            }
            LocalDateTime startDateTime = LocalDateTime.of(date, startTime);
            service.handleCreateABooking(customerName,customerPhone,customerEmail,employee,treatment,startDateTime);

            feedbackLabel.setText("Booking oprettet for " + customerName + " kl. " + startTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            clearForm();

        } catch (Exception e) {
            feedbackLabel.setText("Fejl ved oprettelse: " + e.getMessage());
        }
    }
    @FXML
    private void onBackToBookings() {
        // Switch to booking view (implement navigation)
    }
    private void setFeedbackLabel(String text){
        feedbackLabel.setText(text);
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
            employees.addAll(service.handleGetAllEmployees());
        } catch (RuntimeException e) {
            feedbackLabel.setText("Fejl ved indlæsning af medarbejdere");
        }
    }
    private void loadTreatments() {
        try {
            treatments.clear();
            treatments.addAll(service.handleGetAllTreatments());
        } catch (Exception e) {
            feedbackLabel.setText("Fejl ved indlæsning af behandlinger");
        }
    }
    private void clearForm() {
        customerNameField.clear();
        customerPhoneField.clear();
        startTimeCombo.getItems().clear();
        availabilityList.getItems().clear();
        feedbackLabel.setText("Booking oprettet!");
    }
}
