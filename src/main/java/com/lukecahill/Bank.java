package main.java.com.lukecahill;

import main.java.com.lukecahill.database.DBType;
import main.java.com.lukecahill.database.DBUtil;

import org.apache.log4j.Logger;

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

    private static Customer customer;
    private static BufferedReader inputReader;
    private static Logger log;

    private String bankName = "Simple Bank";

    public Bank() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        customer = new Customer();
        log = Logger.getLogger(Bank.class);
    }

    public void open() {
        boolean opened = false;
        int customerId;
        String customerPassword;
        System.out.println("Welcome to " + this.bankName);

        do {
            customerId = getCustomerId();

            if(customerId <= 0) {
                System.err.println("Please enter a valid ID");
                continue; // skip the rest of the loop
            }

            customerPassword = getCustomerPassword();
            customerPassword = EncryptPasswords.encryptPassword(customerPassword);
            if (checkCustomerId(customerId, customerPassword)) {
                opened = true;
            } else {
                System.err.println("Could not find a customer with those details. Try again.");
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
            log.warn("Could not format the users customer ID. User may have entered a letter or symbol.");
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
            log.error("Could not get input." + e.getMessage());
            System.out.println("Failed to get password. Please try again.");
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
            log.error(e.getMessage());
            DBUtil.showErrorMessage(e);
        }
        return false;
    }
}
