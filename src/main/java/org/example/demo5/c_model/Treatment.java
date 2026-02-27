package org.example.demo5.c_model;

public class Treatment {
    private int id;
    private String name;
    private int durationMinutes;
    private double price;

    public Treatment(int id, String name, int durationMinutes, double price){
        this.id = id;
        this.name = name;
        this.durationMinutes = durationMinutes;
        this.price = price;
    }
    public int getTreatmentId(){
        return id;
    }
    public int getDurationMinutes(){
        return durationMinutes;
    }
}
