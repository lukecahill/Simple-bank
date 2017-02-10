package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class Bank {

    private static Customer customer = new Customer();
    private static EncryptPasswords passwordEncrypt = new EncryptPasswords();
    private static BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    private String bankName = "Lukes Bank";

    public void open() {
        boolean opened = false;
        int customerId;
        String customerPassword;
        System.out.println("Welcome to " + this.bankName);

        do {
            customerId = getCustomerId();

            if(customerId <= 0) {
                System.out.println("Please enter a valid ID");
                continue; // skip the rest of the loop
            }

            customerPassword = getCustomerPassword();
            customerPassword = passwordEncrypt.encryptPassword(customerPassword);
            if (checkCustomerId(customerId, customerPassword)) {
                opened = true;
            } else {
                System.out.println("Could not find a customer with those details. Try again.");
            }
        } while(!opened);

        do {
            customer.showCustomerOptions();
        } while(true);
    }

    private int getCustomerId() {
        System.out.print("Enter your customer ID: ");
        String inputCustomerId;
        int customerId;

        try {
            inputCustomerId = inputReader.readLine();
        } catch(Exception e) {
            return 0;
        }

        try {
            customerId = Integer.parseInt(inputCustomerId);
        } catch(NumberFormatException e) {
            return 0;
        }

        return customerId;
    }

    private String getCustomerPassword() {
        System.out.print("Enter password: ");
        String customerPassword;

        try {
            customerPassword = inputReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

        return customerPassword;
    }

    private boolean checkCustomerId(int customerId, String customerPassword) {

        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement statement = conn.prepareStatement(
                    "SELECT COUNT(CustomerId), CustomerId, CustomerName, CustomerPassword " +
                            "FROM customers " +
                            "WHERE CustomerId = ? " +
                            "AND CustomerPassword = ? " +
                            "LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            statement.setInt(1, customerId);
            statement.setString(2, customerPassword);
            ResultSet rs = statement.executeQuery();

            if(rs.next()) { // if there is one item.
                int count = rs.getInt(1);
                if(count > 1 || count == 0) {
                    return false; // should be redundant really.
                }
                customer.setCustomerId(rs.getInt("CustomerId"));
                customer.setCustomerName(rs.getString("CustomerName"));
                customer.setCustomerPassword(rs.getString("CustomerPassword"));
                System.out.println("ID found.");
                return true;
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
        return false;
    }
}
