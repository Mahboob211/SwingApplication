package com.mycompany.app;

import java.sql.Connection;
import java.sql.*;


public class DatabaseConnection {
     private Connection connection;
     
     
     public DatabaseConnection() throws SQLException{
        String url = "jdbc:mysql://localhost:3306/customer_management"; 
        String user = "root"; 
        String password = ""; 
        connection = DriverManager.getConnection(url, user, password);
     }
     
     public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }
    public Connection getConnection() {
        return connection;
    }
}
