package org.example.demo5.b_service;

import org.example.demo5.c_model.Operator;
import org.example.demo5.g_Exceptions.PasswordException;
import org.example.demo5.g_Exceptions.UserNameException;

public class LoginService {

    //---------------------------Login in-----------------------------------------------------------------------------------

    public Boolean serviceLogin(String username, String password){
        validateOperator(username, password);
        Operator operator = createOperator(username,password);
        return true;
    }
    private Operator createOperator(String username, String password){
        return new Operator(username, password);
    }
    private void validateOperator(String username, String password){
        if(username.isBlank()){
            throw new UserNameException("The username field is empty");
        }
        if(password.isBlank()){
            throw new PasswordException("No password field is empty");
        }
    }
}
