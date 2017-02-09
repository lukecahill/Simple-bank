package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    private EncryptPasswords passwordEncrypt = new EncryptPasswords();
    private IsaAccount isaAccount;
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;

    public void showCustomerOptions() {
        // just used for an example of collections.
        List<String> options = new ArrayList<>();
        options.add("Showing options for: " + this.getCustomerName());
        options.add("Choose an option:");
        options.add("1 - Show current account options");
        options.add("2 - Show ISA account options");
        options.add("3 - Show Savings account options");
        options.add("4 - Show total from all accounts");
        options.add("5 - Change name");
        options.add("6 - Change password");
        options.add("q - quit");

        // just for an example of using lambda
        options.forEach((x) -> System.out.println(x));

        getCustomerOptionChoice();
    }

    private void getCustomerOptionChoice() {
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
                this.isaAccountOptions();
                break;
            case "3":
                this.savingsAccountOptions();
                break;
            case "4":
                this.totalAmountInAllAccounts();
                break;
            case "5":
                this.changeName();
                break;
            case "6":
                this.checkChangePassword();
                break;
            case "q":
                this.quitBank();
                break;
            default:
                System.out.println("That option does not exist. Please retry.");
                break;
        }
    }

    private void savingsAccountOptions() {
        if(savingsAccount == null) {
            savingsAccount = new SavingsAccount(this.getCustomerId());
        }

        if(!savingsAccount.exists()) {
            System.out.println("Savings account does not currently exist!\n");
            System.out.println("Would you like to create an account? Y/N");

            String in = input.next();
            switch(in) {
                case "y":
                    this.createAccountOptions(false);
                    break;
                case "n":
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
        savingsAccount.showOptions();
    }

    public void createAccountOptions(boolean isIsa) {
        String[] inputs = new String[3];
        String[] questions = {
                "Enter an account name: ",
                "Enter an account description: ",
                "Enter an amount to deposit: "
        };

        for(int i = 0; i < questions.length; i++) {
            System.out.print(questions[i]);
            String item = null;

            try {
                item = inputReader.readLine();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }

            inputs[i] = item;
        }

        this.createAccount(isIsa, inputs);

        if(isIsa) {
            isaAccount = new IsaAccount(this.getCustomerId());
        } else {
            savingsAccount = new SavingsAccount(this.getCustomerId());
        }
    }

    private void createAccount(boolean isIsa, String[] inputs) {
        if(isIsa) {
            try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO isaaccounts(IsaAccountId, CustomerId," +
                                "IsaAccountBalance, IsaAccountName, IsaAccountDescription)" +
                                "VALUES(NULL, ?, ?, ?, ?)")
            ) {
                pstmt.setInt(1, this.customerId);
                double amountToDeposit = Double.parseDouble(inputs[2]);
                pstmt.setDouble(2, amountToDeposit);
                pstmt.setString(3, inputs[0]);
                pstmt.setString(4, inputs[1]);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                DBUtil.showErrorMessage(e);
            }
        } else {
            try(
                Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
                PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO savingsaccounts(SavingsAccountId, CustomerId," +
                                "SavingsAccountBalance, SavingsAccountName, SavingsAccountDescription)" +
                                "VALUES(NULL, ?, ?, ?, ?)")
            ) {
                pstmt.setInt(1, this.customerId);
                double amountToDeposit = Double.parseDouble(inputs[2]);
                pstmt.setDouble(2, amountToDeposit);
                pstmt.setString(3, inputs[0]);
                pstmt.setString(4, inputs[1]);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                DBUtil.showErrorMessage(e);
            }
        }
    }

    private void isaAccountOptions() {
        if(isaAccount == null) {
            isaAccount = new IsaAccount(this.getCustomerId());
        }

        if(!isaAccount.exists()) {
            System.out.println("ISA account does not currently exist!\n");
            System.out.println("Would you like to create an account? Y/N");

            String in = input.next();
            switch(in) {
                case "y":
                    this.createAccountOptions(true);
                    break;
                case "n":
                    return;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
        isaAccount.showOptions();
    }

    private void totalAmountInAllAccounts() {
        if(isaAccount == null) { isaAccount = new IsaAccount(this.getCustomerId()); }
        if(savingsAccount == null) { savingsAccount = new SavingsAccount(this.getCustomerId()); }
        if(currentAccount == null) { currentAccount = new CurrentAccount(this.getCustomerId()); }

        double[] listOfAccountAmounts = {
                isaAccount.balance,
                savingsAccount.balance,
                currentAccount.balance
        };

        double total = 0;

        for(double item : listOfAccountAmounts) {
            total += item;
        }

        System.out.println("The total in all accounts is: " + total + "\n");
    }

    private void checkChangePassword() {
        boolean changed = false;
        do {
            System.out.print("Enter your old password: ");
            String newPassword = input.next();

            if (newPassword.isEmpty()) {
                System.out.println("Invalid password.");
                continue;
            }

            newPassword = passwordEncrypt.encryptPassword(newPassword);

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
