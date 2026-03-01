package org.example.demo5.c_model;

import org.example.demo5.b_service.BookingService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    public int getId(){return id;}
    public String getCustomerName(){
        return customerName;
    }
    public String getTreatmentName(){
        return treatmentName;
    }
    public int getTreatmentDuration(){
        return treatmentDuration;
    }
    public String getToStringTreatmentDuration(){
        return String.valueOf(treatmentDuration);
    }
    public String getEmployeeName(){
        return employeeName;
    }
    public LocalDateTime getStartTime(){
        return startTime;
    }
    public String getToStringStartTime(){
        return startTime.format(DateTimeFormatter.ofPattern("dd/mm/yy hh/mm"));
    }
    public BookingStatus getStatus(){
        return status;
    }
}
