package org.example.demo5.e_dal;

import org.example.demo5.c_model.Customer;
import org.example.demo5.c_model.Employee;
import org.example.demo5.d_dbconfig.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public List<Employee> getAllEmployees() throws SQLException {
        List<Employee> employees = new ArrayList<>();
        String SQL = "SELECT EmployeeId, Name, Email, PhoneNumber FROM employee";
        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Employee employee = getEmployee(rs);
                employees.add(employee);
            }
        }
        return employees;
    }
    private Employee getEmployee(ResultSet rs) throws SQLException {
        int employeeId = rs.getInt("EmployeeId");
        String name = rs.getString("Name");
        String email = rs.getString("Email");
        String phoneNumber = rs.getString("PhoneNumber");
        return new Employee(employeeId, name, email, phoneNumber);
    }
    public Customer getCustomer(String name, String phoneNumber, String email) throws SQLException {
        String SQL = "SELECT * FROM customer WHERE name = ? AND phoneNumber = ?";
        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, name);
            ps.setString(2, phoneNumber);
            int rowsAffected = ps.executeUpdate();
            if(rowsAffected == 0){
                return createCustomer(name, phoneNumber, email);
            }
            Customer customer =
        }
    }
    public Customer createCustomer(String name, String phoneNumber, String email) throws SQLException {
        String SQL = "INSERT INTO customer VALUES (?, ?, ?)";
        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, name);
            ps.setString(2, phoneNumber);
            ps.setString(3, email);


        }
    }
    public Customer createCustomerFromRS
}
