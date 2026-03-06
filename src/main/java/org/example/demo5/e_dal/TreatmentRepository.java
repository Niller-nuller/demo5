package org.example.demo5.e_dal;

import org.example.demo5.I_Interface.ITreatmentRepository;
import org.example.demo5.c_model.Treatment;
import org.example.demo5.d_dbconfig.DbConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TreatmentRepository implements ITreatmentRepository {

    public List<Treatment> getTreatments() throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        String SQL = "SELECT TreatmentId, Name, DurationMinutes, Price FROM treatment";
        try(Connection conn = DbConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Treatment treatment = createTreatmentsFromRS(rs);
                treatments.add(treatment);
            }
        }
        return treatments;
    }
    private Treatment createTreatmentsFromRS(ResultSet rs) throws SQLException {
        int id = rs.getInt("TreatmentId");
        String name = rs.getString("Name");
        int durationMinutes = rs.getInt("DurationMinutes");
        double price = rs.getDouble("Price");
        return new Treatment(id, name, durationMinutes, price);
    }
}
