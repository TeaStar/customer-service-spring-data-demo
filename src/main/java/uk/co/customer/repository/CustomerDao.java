package uk.co.customer.repository;

import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerDao {

    @Autowired
    Connection conn;

    public void updateCustomer(String niNumber, String surname) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("update CUSTOMER set NI_NUMBER = " + niNumber + " where SURNAME = '" + surname + "'");
        rs.close();
        stmt.close();
        conn.close();
    }


}
