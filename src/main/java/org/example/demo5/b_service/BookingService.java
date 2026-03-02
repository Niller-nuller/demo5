package org.example.demo5.b_service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.demo5.c_model.Booking;
import org.example.demo5.c_model.BookingStatus;
import org.example.demo5.c_model.Operator;
import org.example.demo5.c_model.Treatment;
import org.example.demo5.e_dal.BookingRepository;
import org.example.demo5.g_Exceptions.PasswordException;
import org.example.demo5.g_Exceptions.UserNameException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class BookingService {
    BookingRepository bookingRepo = new BookingRepository(); //Temp replace with Dependency injection.

    public List<Booking> handleGetPendingBookings(LocalDate date) throws SQLException {
        try {
            return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Booked, date);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
        }
        return null;
    }
    public List<Booking> handleGetCompletedBooking(LocalDate date) throws SQLException {
        try {
            return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Completed, date);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
        }
        return null;
    }
    public List<Booking> handleGetCancelledBooking(LocalDate date) throws SQLException {
        try {
            return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Cancelled, date);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
        }
        return null;
    }
    public void cancelBooking(Booking booking) throws SQLException {
        try {
            bookingRepo.chancelBooking(booking);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
        }
    }
}
