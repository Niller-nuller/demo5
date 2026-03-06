package org.example.demo5.I_Interface;

import org.example.demo5.c_model.Treatment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface ITreatmentRepository {

    List<Treatment> getTreatments() throws SQLException;

}
