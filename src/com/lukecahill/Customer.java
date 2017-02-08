package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class Customer {

    private int customerId;
    private String customerName;

    private String customerPassword;

    private static Scanner input = new Scanner(System.in);
    private IsaAccount isaAccount;
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;

    public void showCustomerOptions() {
        System.out.println("Showing options for: " + this.getCustomerName());
        System.out.println("Choose an option:");
        System.out.println("1 - Show current account options");
        System.out.println("2 - Show ISA account options");
        System.out.println("3 - Show Savings account options");
        System.out.println("4 - Change name");
        System.out.println("5 - Change password");
        System.out.println("q - Quit");

        String choice = input.next();
        choice = choice.toLowerCase();

        switch(choice) {
            case "1":
                if(currentAccount == null) {
                    currentAccount = new CurrentAccount(this.getCustomerId());
                }
                currentAccount.showOptions();
                break;
            case "2":
                if(isaAccount == null) {
                    isaAccount = new IsaAccount(this.getCustomerId());
                }
                isaAccount.showOptions();
                break;
            case "3":
                if(savingsAccount == null) {
                    savingsAccount = new SavingsAccount(this.getCustomerId());
                }
                savingsAccount.showOptions();
                break;
            case "4":
                this.changeName();
            case "5":
                this.checkChangePassword();
            case "q":
                this.quitBank();
                break;
            default:
                System.out.println("That option does not exist. Please retry.");
                break;
        }
    }

    private void checkChangePassword() {
        boolean changed = false;
        do {
            System.out.print("Enter your old password: ");
            String newPassword = input.next();

            if (newPassword.equalsIgnoreCase(this.getCustomerPassword()) || newPassword.isEmpty()) {
                System.out.println("Invalid password, or password is the same as the last used.");
                continue;
            }

            this.changePassword(newPassword);
            changed = true;
        } while(!changed);
    }

    private void changePassword(String password) {
        try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE customers SET CustomerPassword = ? WHERE CustomerId = ?",
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setString(1, password);
            pstmt.setInt(2, this.customerId);
            pstmt.execute();

            this.setCustomerPassword(password);

        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    private void changeName() {
        String name = input.next();
        this.setCustomerName(name);

        // will then need to save this in the database
    }

    public int getCustomerId() {
        return this.customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return this.customerName;
    }
    public boolean setCustomerName(String name) {
        if(name.isEmpty() || name.equalsIgnoreCase("")) {
            return false;
        }

        this.customerName = name;
        return true;
    }

    public void quitBank() {
        System.exit(0);
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }
    public String getCustomerPassword() {
        return customerPassword;
    }
}
