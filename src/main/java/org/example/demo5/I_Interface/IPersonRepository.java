package org.example.demo5.I_Interface;

import org.example.demo5.c_model.Customer;
import org.example.demo5.c_model.Employee;

import java.sql.SQLException;
import java.util.List;

public interface IPersonRepository {

    List<Employee> getAllEmployees() throws SQLException;

    Customer getOrCreateCustomer(Customer customer) throws SQLException;

    Customer getCustomer(Customer customer) throws SQLException;

}
