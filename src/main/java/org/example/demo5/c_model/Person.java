package org.example.demo5.c_model;

public class Person {
    private int id;
    private final String name;
    private final String email;
    private final String phoneNumber;

    public Person(int id, String name, String email, String phoneNumber){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public Person(String name, String email, String phoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public int getId(){return id;}
    public void setId(int id){this.id = id;}
    public String getName(){return name;}
    public String getEmail(){return email;}
    public String getPhoneNumber(){return phoneNumber;}
}
