package org.example.demo5.a_controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import org.example.demo5.c_model.Employee;
import org.example.demo5.c_model.Treatment;

public class CreateBookingController {

    private final ObservableList<Employee> employees = FXCollections.observableArrayList();
    private final ObservableList<Treatment> treatments = FXCollections.observableArrayList();

    @FXML
    private Label todayLabel;
    @FXML
    private ComboBox<Employee> employeeComboBox;
    @FXML
    private ComboBox<Treatment> treatmentComboBox;
}
