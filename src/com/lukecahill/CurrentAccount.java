package com.lukecahill;


import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.sql.*;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class CurrentAccount extends BaseBankAccount {

    private final int customerId;
    private final Scanner input = new Scanner(System.in);

    private int currentAccountId;
    private double balance;
    private boolean loaded = false;

    public CurrentAccount(int customerId) {
        this.customerId = customerId;
        load();
        System.out.println("Created current account");
    }

    @Override
    public void showOptions() {
        super.showOptions();

        int option = input.nextInt();
        switch (option) {
            case 1:
                this.showBalance();
                break;
            case 2:
                this.deposit();
                break;
            case 3:
                this.withdraw();
                break;
            case 4:
                this.printBalance();
                break;
            default:
                System.out.println("Option not found. Please try again.");
                break;
        }
    }

    public void load() {
        try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT CurrentAccountId, CurrentAccountBalance FROM currentaccounts WHERE CustomerId = ? LIMIT 1",
                        ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                this.balance = rs.getDouble("CurrentAccountBalance");
                this.currentAccountId = rs.getInt("CurrentAccountId");
                this.loaded = true;
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    public void showBalance() {
        System.out.println(this.balance);
    }

    public void withdraw() {
        System.out.println("Enter an amount to deposit: ");
        double amount = input.nextDouble();

        if(amount > this.balance) {
            System.out.println("Withdrawal amount is greater than the current balance");
            return;
        }

        this.balance -= amount;
        System.out.println("Amount withdrawn! New balance is: " + this.balance);
        //want to then update the database
    }

    public void deposit() {
        System.out.println("Enter an amount to deposit: ");
        double amount = input.nextDouble();

        if(amount < 0) {
            System.out.println("Amount is less than 0");
            return;
        }

        this.balance += amount;
        System.out.println("Amount deposited! New balance is: " + this.balance);
        // want to then update the database
    }

    public void printBalance() {
    }
}
