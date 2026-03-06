package org.example.demo5.a_controller;

import org.example.demo5.c_model.Operator;
import org.example.demo5.c_model.Role;

import java.util.HashMap;
import java.util.Map;

public class Code {

    public static Map<Role, Operator> listOfOperators = new HashMap<Role, Operator>();

    static{
        new Operator(1,"Sife","passbord",Role.Admin);
    }
}
