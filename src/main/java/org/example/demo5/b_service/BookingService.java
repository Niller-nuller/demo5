package org.example.demo5.b_service;

import org.example.demo5.c_model.*;
import org.example.demo5.e_dal.BookingRepository;
import org.example.demo5.e_dal.PersonRepository;
import org.example.demo5.e_dal.TreatmentRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingService {

    private final BookingRepository bookingRepo; //Temp replace with Dependency injection.
    private final PersonRepository personRepo;
    private final TreatmentRepository treatmentRepo;

    public BookingService(BookingRepository bookingRepo,PersonRepository personRepo, TreatmentRepository treatmentRepo){
        this.bookingRepo = bookingRepo;
        this.personRepo = personRepo;
        this.treatmentRepo = treatmentRepo;
    }
    public List<Booking> handleGetPendingBookings(LocalDate date){
        try {
            return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Booked, date);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public List<Booking> handleGetCompletedBooking(LocalDate date){
        try {
            return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Completed, date);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public List<Booking> handleGetCancelledBooking(LocalDate date){
        try {
            return bookingRepo.getBookingListBasedOnStatus(BookingStatus.Cancelled, date);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database..");
        }
    }
    public void handleCancelBooking(Booking booking){
        try {
            bookingRepo.chancelBooking(booking);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public void handleChangeBookingStatusToCompleted(Booking booking){
        try{
            bookingRepo.changeBookingToCompleted(booking);
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public void handleCreateABooking(String customerName,String customerPhoneNumber,String customerEmail, Employee employee, Treatment treatment, LocalDateTime dateTime){
        try{
            Customer customer = new Customer(customerName,customerEmail,customerPhoneNumber);
            customer = personRepo.getCustomer(customer);
            bookingRepo.createABooking(customer, employee, treatment, dateTime);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public List<Employee> handleGetAllEmployees(){
        try{
            return personRepo.getAllEmployees();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public List<Treatment> handleGetAllTreatments(){
        try{
            return treatmentRepo.getTreatments();
        }catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
    public List<LocalTime> handleGetAvailableTimes(Employee employee,LocalDate date, Treatment treatment){
        try {
            return bookingRepo.getAvailableTimes(employee, date, treatment);
        }catch (SQLException e){
            System.out.println("SQLException: " + e.getMessage());//PRETEND IT WRITES TO A LOG!!
            throw new RuntimeException("Could not connect to database.");
        }
    }
}
