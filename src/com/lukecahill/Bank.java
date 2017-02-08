package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;
import com.sun.javaws.exceptions.InvalidArgumentException;

import java.sql.*;
import java.util.Scanner;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class Bank {

    Customer customer = new Customer();
    private static Scanner input = new Scanner(System.in);

    private int bankId = 1;
    private String name = "Lukes Bank";

    public void open() {
        boolean opened = false;
        int customerId;
        do {
            customerId = getCustomerId();
            if (checkCustomerId(customerId)) {
                opened = true;
            } else {
                System.out.println("Could not find a customer with that ID. Try again.");
            }
        } while(!opened);

        getCustomerDetails(customerId);

        do {
            customer.showCustomerOptions();
        } while(true);
    }

    private int getCustomerId() {
        System.out.println("Enter your customer ID: ");
        int customerId;

        try {
            customerId = input.nextInt();
        } catch(NumberFormatException e) {
            System.exit(0);
            return 0;
        }

        return customerId;
    }

    private boolean checkCustomerId(int customerId) {

        try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT COUNT(CustomerId) FROM customers WHERE CustomerId = ? LIMIT 1",
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                int count = rs.getInt(1);
                if(count > 1 || count == 0) {
                    return false;
                }
                System.out.println("ID found.");
                return true;
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
        return false;
    }

    private void getCustomerDetails(int customerId) {
        try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT CustomerId, CustomerName FROM customers WHERE CustomerId = ? LIMIT 1",
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                customer.setName(rs.getString("CustomerName"));
                customer.setCustomerId(rs.getInt("CustomerId"));
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }
}
