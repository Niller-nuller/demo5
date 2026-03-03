package org.example.demo5.e_dal;

import org.example.demo5.c_model.*;
import org.example.demo5.d_dbconfig.DbConnect;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public List<Booking> getBookingListBasedOnStatus(BookingStatus status, LocalDate date) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                SELECT
                b.BookingId,
                c.Name AS customerName,
                t.Name AS treatmentName,
                t.duration AS treatmentDuration,
                e.Name AS employeeName,
                b.Start_Time,
                b.Status
                FROM bookings b
                LEFT JOIN customers c ON b.CustomerId = c.id
                LEFT JOIN employees e ON b.EmployeeId = e.id \s
                LEFT JOIN treatments t ON b.TreatmentId = t.id
                WHERE b.Status = ?
                AND date(Start_Time) = ?
                ORDER BY b.Start_Time""";
        try (Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setString(1, status.toString());
            ps.setDate(2, java.sql.Date.valueOf(date));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Booking booking = createBookingFromRS(rs);
                bookings.add(booking);
            }
        }
        return bookings;
    }

    private Booking createBookingFromRS(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String customerName = rs.getString("CustomerName");
        String treatmentName = rs.getString("TreatmentName");
        int treatmentDuration = rs.getInt("Duration_minutes");
        String employeeName = rs.getString("Employee_name");
        LocalDateTime startTime = rs.getTimestamp("Start_Time").toLocalDateTime();
        BookingStatus status = BookingStatus.valueOf(rs.getString("Status"));
        return new Booking(id, customerName, treatmentName, treatmentDuration, employeeName, startTime, status);
    }

    public void createABooking(Customer customer, Employee employee, Treatment treatment, LocalDateTime startTime) throws SQLException {
        int duration = treatment.getDurationMinutes();
        LocalDateTime endTime = startTime.plusMinutes(duration);
        String SQL = "INSERT INTO Booking (CustomerId, EmployeeId, TreatmentId, StartTime, EndTime) VALUES (?,?,?,?,?)";
        try (Connection conn = DbConnect.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(SQL)) {
                ps.setInt(1, customer.getId());
                ps.setInt(2, employee.getId());  // Note: matches your table column name
                ps.setInt(3, treatment.getTreatmentId());
                ps.setTimestamp(4, Timestamp.valueOf(startTime));
                ps.setTimestamp(5, Timestamp.valueOf(endTime));
                ps.executeUpdate();
            }
        }
    }
        public void chancelBooking(Booking booking) throws SQLException {
        String SQL = "UPDATE bookings SET status = 'Cancelled' WHERE id = ?";
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, booking.getId());
            ps.executeQuery();
        }
    }

    public void changeBookingToCompleted(Booking booking) throws SQLException {
        String SQL = "UPDATE bookings SET status = 'Completed' WHERE id = ?";
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, booking.getId());
            ps.executeQuery();
        }
    }
}