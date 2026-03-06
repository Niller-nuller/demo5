package org.example.demo5.I_Interface;

import org.example.demo5.c_model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface IBookingRepository {

    List<Booking> getBookingListBasedOnStatus(BookingStatus status, LocalDate date) throws SQLException;

    void createABooking(Customer customer, Employee employee, Treatment treatment, LocalDateTime startTime) throws SQLException;

    void cancelBooking(Booking booking) throws SQLException;

    void changeBookingToCompleted(Booking booking) throws SQLException;

    boolean isTimeSlotAvailable(Employee employee, LocalDateTime start, LocalDateTime end) throws SQLException;

    List<LocalTime> getAvailableTimes(Employee employee, LocalDate date, Treatment treatment) throws SQLException;

}
