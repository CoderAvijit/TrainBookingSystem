package com.project.TrainBookingSystem.dao;

import java.sql.*;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DatabaseHelper {

    // JDBC connection parameters
    private static final String jdbcUrl = "jdbc:mysql://localhost:3306/trainbookingsystem";
    private static final String username = "root";
    private static final String password = "Admin";

    // Method to execute SQL function with parameters
    public Object executeFunction(String functionName, List<Object> params) {
        Connection conn = null;
        CallableStatement stmt = null;
        Object result = null;

        try {
            // Establish JDBC connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);

            // Prepare the SQL call with parameters
            StringBuilder sql = new StringBuilder("SELECT ");
            sql.append(functionName).append("(");
            for (int i = 0; i < params.size(); i++) {
                if (i > 0) sql.append(",");
                sql.append("?");
            }
            sql.append(")");

            // Create CallableStatement and set parameters
            stmt = conn.prepareCall(sql.toString());
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            // Execute the function call
            boolean hasResultSet = stmt.execute();
            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                if (rs.next()) {
                    // Retrieve the result (assuming it's a JSON string)
                    result = rs.getObject(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in reverse order of their creation
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
