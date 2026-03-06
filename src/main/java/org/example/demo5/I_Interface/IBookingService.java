package org.example.demo5.I_Interface;

import org.example.demo5.c_model.Booking;
import org.example.demo5.c_model.Employee;
import org.example.demo5.c_model.Treatment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IBookingService {

    List<Booking> handleGetPendingBookings(LocalDate date);

    List<Booking> handleGetCompletedBookings(LocalDate date);

    List<Booking> handleGetCancelledBookings(LocalDate date);

    void handleCancelBooking(Booking booking);

    void handleChangeBookingStatusToCompleted(Booking booking);

    void handleCreateBooking(String customerName, String customerPhone, String customerEmail,
                             Employee employee, Treatment treatment, LocalDateTime dateTime);

    List<LocalTime> handleGetAvailableTimes(Employee employee, LocalDate date, Treatment treatment);

    List<Employee> handleGetAllEmployees();

    List<Treatment> handleGetAllTreatments();

}
