package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Luke on 08/02/2017.
 */
public class IsaAccount extends BaseBankAccount {

    public IsaAccount() {
        System.out.println("Created ISA");
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
            case 5:
                this.showAboutAccount();
                break;
            default:
                System.out.println("Option not found. Please try again.");
                break;
        }
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
                        ResultSet.CONCUR_READ_ONLY);
        ) {
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                this.balance = rs.getDouble("IsaAccountBalance");
                this.currentAccountId = rs.getInt("IsaAccountId");
                this.customerId = rs.getInt("CustomerId");
                this.name = rs.getString("IsaAccountName");
                this.description = rs.getString("IsaAccountDescription");
            }
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    protected void printBalance() {
        super.printBalance("isa_account_balance.txt");
    }
}
