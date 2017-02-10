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
public class IsaAccount extends BaseBankAccount {

    private final double INTEREST = 0.035d; // 3.5% - I wish it was this good...
    private boolean exists = true;

    public IsaAccount(int customerId) {
        this.customerId = customerId;
        System.out.println("Created ISA");
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
                    "SELECT IsaAccountId, IsaAccountBalance, " +
                            "CustomerId, IsaAccountName, IsaAccountDescription " +
                            "FROM isaaccounts " +
                            "WHERE CustomerId = ? " +
                            "LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                System.out.println(rs.getDouble(1));
                System.out.println(rs.getInt(2));
                System.out.println(rs.getInt(3));
                System.out.println(rs.getString(4));
                System.out.println(rs.getString(5));
                this.balance = rs.getDouble("IsaAccountBalance");
                this.bankAccountId = rs.getInt("IsaAccountId");
                this.customerId = rs.getInt("CustomerId");
                this.name = rs.getString("IsaAccountName");
                this.description = rs.getString("IsaAccountDescription");
            }

            if(this.bankAccountId == 0) {
                exists = false;
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    protected void printBalance() {
        super.printBalance("isa_account_balance.txt");
    }

    private void withdraw() {
        super.withdraw("isa");
    }

    private void deposit() {
        super.deposit("isa");
    }

    public void calculateInterest() {
        double newInterest = (this.balance * INTEREST) + this.balance;
        System.out.println(newInterest);
    }

    public boolean exists() {
        return exists;
    }
}
