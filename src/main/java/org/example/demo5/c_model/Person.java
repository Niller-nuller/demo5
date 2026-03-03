package org.example.demo5.c_model;

public class Person {
    private int id;
    private String name;
    private String email;
    private String phoneNumber;

    public Person(int id, String name, String email, String phoneNumber){
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    public int getId(){
        return id;
    }
    public String getName(){return name;}
}
