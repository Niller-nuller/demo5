package org.example.demo5.e_dal;

import org.example.demo5.c_model.*;
import org.example.demo5.d_dbconfig.DbConnect;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepository {

    public List<Booking> getBookingListBasedOnStatus(BookingStatus status, LocalDate date) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String SQL = """
                SELECT b.BookingId, c.Name AS customerName, t.Name AS treatmentName,
                t.DurationMinutes AS treatmentDuration, e.Name AS employeeName,
                b.StartTime, b.Status
                FROM Booking b          -- Changed from 'bookings'
                LEFT JOIN Customer c ON b.CustomerId = c.CustomerId     -- Changed casing
                LEFT JOIN Employee e ON b.EmployeeId = e.EmployeeId     -- Changed casing \s
                LEFT JOIN Treatment t ON b.TreatmentId = t.TreatmentId  -- Changed casing
                WHERE b.Status = ? AND DATE(b.StartTime) = ?
                ORDER BY b.StartTime""";
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
        int id = rs.getInt("BookingId");           // Match SQL alias
        String customerName = rs.getString("customerName");
        String treatmentName = rs.getString("treatmentName");
        int treatmentDuration = rs.getInt("treatmentDuration");  // Match SQL alias
        String employeeName = rs.getString("employeeName");
        LocalDateTime startTime = rs.getTimestamp("StartTime").toLocalDateTime();
        BookingStatus status = BookingStatus.valueOf(rs.getString("Status"));
        return new Booking(id, customerName, treatmentName, treatmentDuration, employeeName, startTime, status);
    }
    public void createABooking(Customer customer, Employee employee, Treatment treatment, LocalDateTime startTime) throws SQLException {
        int duration = treatment.getDurationMinutes();
        LocalDateTime endTime = startTime.plusMinutes(duration);
        String SQL = "INSERT INTO booking (CustomerId, EmployeeId, TreatmentId, StartTime, EndTime, Status) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DbConnect.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(SQL)) {
                ps.setInt(1, customer.getId());
                ps.setInt(2, employee.getId());  // Note: matches your table column name
                ps.setInt(3, treatment.getTreatmentId());
                ps.setTimestamp(4, Timestamp.valueOf(startTime));
                ps.setTimestamp(5, Timestamp.valueOf(endTime));
                ps.setString(6, String.valueOf(BookingStatus.Booked));
                ps.executeUpdate();
            }
        }
    }
        public void chancelBooking(Booking booking) throws SQLException {
        String SQL = "UPDATE Booking SET Status = 'Cancelled' WHERE BookingId = ?";
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, booking.getId());
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0) {
                throw new SQLException("Booking with id " + booking.getId() + " has not been found");
            }
        }
    }

    public void changeBookingToCompleted(Booking booking) throws SQLException {
        String SQL = "UPDATE booking SET Status = 'Completed' WHERE BookingId = ?";
        try (Connection conn = DbConnect.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SQL);
            ps.setInt(1, booking.getId());
            ps.executeUpdate();
        }
    }
    public boolean isTimeSlotAvailable(Employee employee, LocalDateTime start, LocalDateTime end) throws SQLException {
        String SQL = """
        SELECT COUNT(*)\s
        FROM Booking b\s
        WHERE b.EmployeeId = ?\s
        AND b.Status != 'Cancelled'
        AND (
            (b.StartTime < ? AND b.EndTime > ?) OR
            (b.StartTime < ? AND b.EndTime > ?) OR
            (b.StartTime >= ? AND b.EndTime <= ?)
        )
   \s""";

        try (Connection conn = DbConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL)) {
            ps.setInt(1, employee.getId());
            ps.setTimestamp(2, Timestamp.valueOf(end));
            ps.setTimestamp(3, Timestamp.valueOf(start));
            ps.setTimestamp(4, Timestamp.valueOf(end));
            ps.setTimestamp(5, Timestamp.valueOf(start));
            ps.setTimestamp(6, Timestamp.valueOf(start));
            ps.setTimestamp(7, Timestamp.valueOf(end));
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1) == 0;
        }
    }
    public List<LocalTime> getAvailableTimes(Employee employee, LocalDate date, Treatment treatment) throws SQLException {
        List<LocalTime> availableTimes = new ArrayList<>();

        // Working hours: 9:00 - 17:00 (8 hours = 32 x 15-min slots)
        LocalTime startHour = LocalTime.of(9, 0);
        LocalTime endHour = LocalTime.of(17, 0);

        // Generate all possible 15-min slots that fit duration
        LocalTime currentSlot = startHour;
        while (currentSlot.plusMinutes(treatment.getDurationMinutes()).isBefore(endHour.plusSeconds(1)) || currentSlot.plusMinutes(treatment.getDurationMinutes()).equals(endHour)) {

            LocalDateTime slotStart = LocalDateTime.of(date, currentSlot);
            LocalDateTime slotEnd = slotStart.plusMinutes(treatment.getDurationMinutes());

            // Check if this slot is conflict-free using your existing method
            if (isTimeSlotAvailable(employee, slotStart, slotEnd)) {
                availableTimes.add(currentSlot);
            }

            // Next 15-min slot
            currentSlot = currentSlot.plusMinutes(15);
        }

        return availableTimes;
    }

}
