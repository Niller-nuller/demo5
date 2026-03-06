package org.example.demo5.e_dal;

import org.example.demo5.I_Interface.IPersonRepository;
import org.example.demo5.c_model.Customer;
import org.example.demo5.c_model.Employee;
import org.example.demo5.d_dbconfig.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository implements IPersonRepository {

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

    public Customer getCustomer(Customer customer) throws SQLException {
        String SQL = "SELECT CustomerId, Name, Email, PhoneNumber FROM customer WHERE name = ? AND phoneNumber = ?";
        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)){
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPhoneNumber());
            try(ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    return createCustomerFromRS(rs);
                }
            } return getOrCreateCustomer(customer);
        }
    }
    public Customer getOrCreateCustomer(Customer customer) throws SQLException {
        String SQL = "INSERT INTO Customer (Name, Email, PhoneNumber) VALUES (?, ?, ?)";
        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL, PreparedStatement.RETURN_GENERATED_KEYS)){
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, customer.getPhoneNumber());
            ps.executeUpdate();
            try(ResultSet keys = ps.getGeneratedKeys()){
                if(keys.next()){
                    customer.setId(keys.getInt(1));
                }
                else {
                    throw new SQLException("Failed to create Customer, no Id could be obtained");
                }
            }return customer;
        }
    }
    private Customer createCustomerFromRS(ResultSet rs) throws SQLException {
        int id = rs.getInt("CustomerId");
        String customerName = rs.getString("Name");
        String customerEmail = rs.getString("Email");
        String customerPhoneNumber = rs.getString("PhoneNumber");
        return new Customer(id, customerName, customerEmail, customerPhoneNumber);
    }
}
