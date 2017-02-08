package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

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

    private String bankName = "Lukes Bank";

    public void open() {
        boolean opened = false;
        int customerId;
        String customerPassword;

        do {
            customerId = getCustomerId();
            customerPassword = getCustomerPassword();
            if (checkCustomerId(customerId, customerPassword)) {
                System.out.println("Welcome to " + this.bankName);
                opened = true;
            } else {
                System.out.println("Could not find a customer with those details. Try again.");
            }
        } while(!opened);

        getCustomerDetails(customerId);

        do {
            customer.showCustomerOptions();
        } while(true);
    }

    private int getCustomerId() {
        System.out.print("Enter your customer ID: ");
        int customerId;

        try {
            customerId = input.nextInt();
        } catch(NumberFormatException e) {
            System.exit(0);
            return 0;
        }

        return customerId;
    }

    private String getCustomerPassword() {
        System.out.print("Enter password: ");
        String customerPassword;
        customerPassword = input.next();

        return customerPassword;
    }

    private boolean checkCustomerId(int customerId, String customerPassword) {

        try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT COUNT(CustomerId) " +
                                "FROM customers " +
                                "WHERE CustomerId = ? " +
                                "AND CustomerPassword = ? " +
                                "LIMIT 1",
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            pstmt.setInt(1, customerId);
            pstmt.setString(2, customerPassword);
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
                        "SELECT CustomerId, CustomerName, CustomerPassword FROM customers WHERE CustomerId = ? LIMIT 1",
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                customer.setCustomerName(rs.getString("CustomerName"));
                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setCustomerPassword(rs.getString("CustomerPassword"));
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }
}
