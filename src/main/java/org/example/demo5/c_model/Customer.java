package org.example.demo5.c_model;

public class Customer extends Person{

    public Customer(int id, String name, String email, String phoneNumber){
        super(id,name,email,phoneNumber);
    }
    public Customer(String name, String email, String phoneNumber){
        super(name,email,phoneNumber);
    }
}
