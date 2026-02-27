package org.example.demo5.e_dal;

import org.example.demo5.c_model.Operator;
import org.example.demo5.d_dbconfig.DbConnect;
import org.example.demo5.g_Exceptions.UserNameException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OperatorRepo {

    public boolean authenticateOperator(Operator operator) throws SQLException {
        String SQL = "SELECT password_hash FROM operators WHERE username = ?";
        try(Connection conn = DbConnect.getConnection()){
            PreparedStatement ps = conn.prepareStatement(SQL); {
                ps.setString(1,operator.getUsername());
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    String comparePass = rs.getString("password_hash");

                    return BCrypt.checkpw(operator.getPassword(), comparePass);
                }
                throw new UserNameException("The username does not exist");
            }
        }
    }
}
