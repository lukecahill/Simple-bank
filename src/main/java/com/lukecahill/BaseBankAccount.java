package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.io.IOException;
import java.io.PrintWriter;
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
public abstract class BaseBankAccount {
    protected final Scanner input = new Scanner(System.in);

    protected String name;
    protected String description;
    protected double balance;
    protected int customerId;
    protected int bankAccountId;
    protected boolean leaveAccount = false;

    String[] accountOptions = {
        "1 - Show balance",
        "2 - Deposit money",
        "3 - Withdraw money",
        "4 - Print balance",
        "5 - Show account details",
        "6 - Calculate balance with interest after a year",
        "b - Go back",
        "q - Quit"
    };

    public void showOptions() {
        for(String option : accountOptions) {
            System.out.println(option);
        }
    }

    protected void printBalance(String filename) {

        try(PrintWriter writer = new PrintWriter(filename)) {
            writer.write("The current balance of the account is: " + balance);
            System.out.println("Saved to \"" + filename + "\".\n");
        } catch(IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    protected void showAboutAccount() {
        System.out.println("Name of account: " + this.name);
        System.out.println("Account description: " + this.description + "\n");
    }

    protected void withdraw(String accountType) {

        System.out.print("Enter an amount to withdraw: ");
        double amount = input.nextDouble();

        if(amount > this.balance) {
            System.out.println("Withdrawal amount is greater than the current balance\n");
            return;
        }

        this.balance -= amount;
        System.out.println("Amount withdrawn! New balance is: " + this.balance);

        if("current".equalsIgnoreCase(accountType)) {
            this.updateCurrentBalance();
        } else if("isa".equalsIgnoreCase(accountType)) {
            this.updateIsaBalance();
        } else if("savings".equalsIgnoreCase(accountType)) {
            this.updateSavingsBalance();
        }
    }

    private void updateSavingsBalance() {
        try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE savingsaccounts SET CurrentSavingsBalance = ? WHERE SavingsAccountId = ? LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setDouble(1, this.balance);
            pstmt.setInt(2, this.bankAccountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    private void updateIsaBalance() {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE isaaccounts SET CurrentSavingsBalance = ? WHERE IsaAccountId = ? LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setDouble(1, this.balance);
            pstmt.setInt(2, this.bankAccountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    private void updateCurrentBalance() {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE currentaccounts SET CurrentAccountBalance = ? WHERE CurrentAccountId = ? LIMIT 1")
        ) {
            pstmt.setDouble(1, this.balance);
            pstmt.setInt(2, this.bankAccountId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    protected void deposit(String accountType) {
        System.out.print("Enter an amount to deposit: ");
        double amount = input.nextDouble();

        if(amount < 0) {
            System.err.println("Amount is less than 0");
            return;
        }

        this.balance += amount;
        System.out.println("Amount deposited! New balance is: " + this.balance + "\n");

        if("current".equalsIgnoreCase(accountType)) {
            this.updateCurrentBalance();
        } else if("isa".equalsIgnoreCase(accountType)) {
            this.updateIsaBalance();
        } else if("savings".equalsIgnoreCase(accountType)) {
            this.updateSavingsBalance();
        }
    }

    protected void showBalance() {

        System.out.println("Showing balance for: " + this.name + "\n");
        System.out.println(this.balance + "\n");
    }

    protected abstract void load();
    protected abstract void calculateInterest();

    public double getBalance() {
        return this.balance;
    }

}
