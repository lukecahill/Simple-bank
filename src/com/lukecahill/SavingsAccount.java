package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */

public class SavingsAccount extends BaseBankAccount {

    private final double INTEREST = 0.40d;
    private boolean exists = true;

    public SavingsAccount(int customerId) {
        System.out.println("Created savings account");
        this.customerId = customerId;
        load();
    }

    @Override
    public void showOptions() {
        do {
            super.showOptions();

            String option = input.next();
            option = option.toLowerCase();
            switch (option) {
                case "1":
                    this.showBalance();
                    break;
                case "2":
                    this.deposit();
                    break;
                case "3":
                    this.withdraw();
                    break;
                case "4":
                    this.printBalance();
                    break;
                case "5":
                    this.showAboutAccount();
                    break;
                case "6":
                    this.calculateInterest();
                    break;
                case "b":
                    this.leaveAccount = true;
                    break;
                case "q":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option not found. Please try again.");
                    break;
            }
        } while(!leaveAccount);
    }

    protected void load() {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT SavingsAccountId, SavingsAccountBalance, " +
                            "CustomerId, SavingsAccountName, SavingsAccountDescription " +
                            "FROM savingsaccounts " +
                            "WHERE CustomerId = ? " +
                            "LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                this.balance = rs.getDouble("SavingsAccountBalance");
                this.bankAccountId = rs.getInt("SavingsAccountId");
                this.customerId = rs.getInt("CustomerId");
                this.name = rs.getString("SavingsAccountName");
                this.description = rs.getString("SavingsAccountDescription");
            }

            if(this.bankAccountId == 0) {
                exists = false;
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    public boolean exists() {
        return exists;
    }

    private void withdraw() {
        super.withdraw("savings");
    }

    private void deposit() {
        super.deposit("savings");
    }

    protected void printBalance() {
        super.printBalance("savings_account_balance.txt");
    }

    public void calculateInterest() {
        double newInterest = (this.balance * INTEREST) + this.balance;
        System.out.println(newInterest);
    }
}
