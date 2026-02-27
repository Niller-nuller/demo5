package org.example.demo5.c_model;

import java.time.LocalDateTime;

public class Booking {
    private int id;
    private String customerName;
    private String treatmentName;
    private int treatmentDuration;
    private String employeeName;
    private LocalDateTime startTime;
    private BookingStatus status;

    public Booking(int id, String customerName,String treatmentName, int treatmentDuration,String employeeName, LocalDateTime startTime, BookingStatus status){
        this.id = id;
        this.customerName =customerName;
        this.treatmentName = treatmentName;
        this.treatmentDuration = treatmentDuration;
        this.employeeName = employeeName;
        this.startTime = startTime;
        this.status = status;
    }
}
