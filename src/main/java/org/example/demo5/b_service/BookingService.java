package org.example.demo5.b_service;

import org.example.demo5.c_model.Booking;
import org.example.demo5.c_model.BookingStatus;
import org.example.demo5.c_model.Operator;
import org.example.demo5.e_dal.BookingRepository;
import org.example.demo5.g_Exceptions.PasswordException;
import org.example.demo5.g_Exceptions.UserNameException;

import java.util.List;

public class BookingService {
    BookingRepository bookingRepo = new BookingRepository();
    public List<Booking> handleGetDefaultBookings(){

        return bookingRepo.getBookingListBasedOnStatus()
    }
}
