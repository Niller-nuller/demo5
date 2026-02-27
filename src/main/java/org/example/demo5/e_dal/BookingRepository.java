package org.example.demo5.e_dal;

import org.example.demo5.c_model.*;
import org.example.demo5.d_dbconfig.DbConnect;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public List<Booking> getBookingList() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                SELECT c.name AS customerName, t.name AS treatmentName, t.durantion_minutes,
                e.name AS employeeName, b.start_time
                FROM bookings
                LEFT JOIN customers c ON b.customer_id = c.id
                LEFT JOIN employees e ON b.employee_id = e.id
                LEFT JOIN treatments t ON b.treatment_id = t.id
                ORDER BY b.start_time""";

        try(Connection conn = DbConnect.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Booking booking = createBookingFromRS(rs);
                bookings.add(booking);
            }
        }return bookings;
    }

    public List<Booking> getBookingListBasedOnStatus(BookingStatus status) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                SELECT c.name AS customerName, t.name AS treatmentName, t.durantion_minutes,
                e.name AS employeeName, b.start_time
                FROM bookings
                LEFT JOIN customers c ON b.customer_id = c.id
                LEFT JOIN employees e ON b.employee_id = e.id
                LEFT JOIN treatments t ON b.treatment_id = t.id
                WHERE b.stats = ?
                ORDER BY b.start_time""";

        try(Connection conn = DbConnect.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,status.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Booking booking = createBookingFromRS(rs);
                bookings.add(booking);
            }
        }return bookings;
    }
    public List<Booking> getDefaultBookingList() throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                SELECT c.name AS customerName, t.name AS treatmentName, t.durantion_minutes,
                e.name AS employeeName, b.start_time
                FROM bookings
                LEFT JOIN customers c ON b.customer_id = c.id
                LEFT JOIN employees e ON b.employee_id = e.id
                LEFT JOIN treatments t ON b.treatment_id = t.id
                WHERE b.stats = ?
                ORDER BY b.start_time""";

        try(Connection conn = DbConnect.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setString(1,.toString());
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                Booking booking = createBookingFromRS(rs);
                bookings.add(booking);
            }
        }return bookings;
    }
    private Booking createBookingFromRS(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String customerName = rs.getString("customerName");
        String treatmentName = rs.getString("treatmentName");
        int treatmentDuration = rs.getInt("duration_minutes");
        String employeeName = rs.getString("employee_name");
        LocalDateTime startTime = rs.getTimestamp("start_time").toLocalDateTime();
        BookingStatus status = BookingStatus.valueOf(rs.getString("status"));
        return new Booking(id,customerName,treatmentName,treatmentDuration,employeeName,startTime,status);
    }

    public void createABooking(Customer customer, Employee employee, Treatment treatment, LocalDateTime startTime) throws SQLException {
        int duration = treatment.getDurationMinutes();
        LocalDateTime endTime = startTime.plusMinutes(duration);
        String SQL = "INSERT INTO bookings (customer_id, Worker_id, treatment_id, start_time, end_time) VALUES (?,?,?,?)";
        try(Connection conn = DbConnect.getConnection()){
            try(PreparedStatement ps = conn.prepareStatement(SQL);){
                ps.setInt(1,customer.getId());
                ps.setInt(2, employee.getId());
                ps.setInt(3,treatment.getTreatmentId());
                ps.setTimestamp(4,Timestamp.valueOf(startTime));
                ps.setTimestamp(5,Timestamp.valueOf(endTime));
                ps.executeQuery();
            }
        }
    }
}
