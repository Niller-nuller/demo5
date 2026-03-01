package org.example.demo5.b_service;

import org.example.demo5.c_model.Booking;
import org.example.demo5.c_model.BookingStatus;
import org.example.demo5.c_model.Operator;
import org.example.demo5.c_model.Treatment;
import org.example.demo5.e_dal.BookingRepository;
import org.example.demo5.g_Exceptions.PasswordException;
import org.example.demo5.g_Exceptions.UserNameException;

import java.sql.SQLException;
import java.util.List;

public class BookingService {

    BookingRepository bookingRepo = new BookingRepository(); //Temp replace with Dependency injection.

    public List<Booking> handleGetRawBookings() throws SQLException {
        return bookingRepo.getRawBookingList();
    }

    public List<Booking> handleGetPendingBookings() throws SQLException {
        return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Booked);
    }

    public List<Booking> handleGetBookingHistory() throws SQLException {
        return bookingRepo.getBookingHistoryList(BookingStatus.Booked);
    }

}
