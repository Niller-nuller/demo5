package org.example.demo5.c_model;

public class Operator {
    private int id;
    private String username;
    private String password;
    private Role role;

    public Operator(int id, String username, String password, Role role){
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
    }
    public Operator(String username,String password){
        this.username = username;
        this.password = password;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
