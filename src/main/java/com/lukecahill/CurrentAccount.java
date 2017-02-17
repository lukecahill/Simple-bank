package main.java.com.lukecahill;


import main.java.com.lukecahill.database.DBType;
import main.java.com.lukecahill.database.DBUtil;

import java.sql.*;
import java.text.DecimalFormat;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class CurrentAccount extends BaseBankAccount {

    private final double INTEREST = 0.025d; // this interest rate is terrible :^)

    public CurrentAccount(int customerId) {
        this.customerId = customerId;
        load();
        System.out.println("Created current account");
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

    public void load() {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT CurrentAccountId, CurrentAccountBalance, " +
                            "CustomerId, CurrentAccountName, CurrentAccountDescription " +
                            "FROM currentaccounts " +
                            "WHERE CustomerId = ? " +
                            "LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                this.balance = rs.getDouble("CurrentAccountBalance");
                this.bankAccountId = rs.getInt("CurrentAccountId");
                this.customerId = rs.getInt("CustomerId");
                this.name = rs.getString("CurrentAccountName");
                this.description = rs.getString("CurrentAccountDescription");
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    public void printBalance() {
        super.printBalance("current_account_balance.txt");
    }

    private void withdraw() {
        super.withdraw("current");
    }

    private void deposit() {
        super.deposit("current");
    }

    public void calculateInterest() {
        double newInterest = (this.balance * INTEREST) + this.balance;

        DecimalFormat decimalFormat = new DecimalFormat("###.##");
        System.out.println(decimalFormat.format(newInterest));
    }
}
