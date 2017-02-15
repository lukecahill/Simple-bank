package com.lukecahill;

import com.lukecahill.database.DBType;
import com.lukecahill.database.DBUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private static Scanner input;
    private static BufferedReader inputReader;
    private static Log log;

    private IsaAccount isaAccount;
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;

    public Customer() {
        inputReader = new BufferedReader(new InputStreamReader(System.in));
        input = new Scanner(System.in);
        log = LogFactory.getLog(Bank.class);
    }

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
        options.forEach(x -> System.out.println(x));

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
                double total = this.totalAmountInAllAccounts();
                System.out.println("The total in all accounts is: " + total + "\n");
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
            in = in.toLowerCase();

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
            String item = "";

            try {
                item = inputReader.readLine();
            } catch (IOException e) {
                System.out.println("Could not get user input. Please try again.");
                log.error(e.getMessage());
                return;
            }

            inputs[i] = item;
        }

        if(isIsa) {
            this.createIsaAccount(inputs);
            isaAccount = new IsaAccount(this.getCustomerId());
        } else {
            this.createSavingsAccount(inputs);
            savingsAccount = new SavingsAccount(this.getCustomerId());
        }
    }

    private void createIsaAccount(String[] inputs) {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO isaaccounts(IsaAccountId, CustomerId," +
                            "IsaAccountBalance, IsaAccountName, IsaAccountDescription)" +
                            "VALUES(NULL, ?, ?, ?, ?)")
        ) {
            pstmt.setInt(1, this.customerId);
            pstmt.setString(3, inputs[0]);
            pstmt.setString(4, inputs[1]);

            double amountToDeposit = 0.0d;
            try {
                amountToDeposit = Double.parseDouble(inputs[2]);
                if(amountToDeposit < 0) {
                    log.error("Amount to deposit must be greater than 0");
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("An invalid amount was entered.");
            }

            pstmt.setDouble(2, amountToDeposit);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
    }

    private void createSavingsAccount(String[] inputs) {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO savingsaccounts(SavingsAccountId, CustomerId," +
                            "SavingsAccountBalance, SavingsAccountName, SavingsAccountDescription)" +
                            "VALUES(NULL, ?, ?, ?, ?)")
        ) {
            pstmt.setInt(1, this.customerId);
            pstmt.setString(3, inputs[0]);
            pstmt.setString(4, inputs[1]);

            double amountToDeposit = 0.0d;
            try {
                amountToDeposit = Double.parseDouble(inputs[2]);
                if(amountToDeposit < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("An invalid amount was entered.");
            }

            pstmt.setDouble(2, amountToDeposit);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
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
            in = in.toLowerCase();

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

    public double totalAmountInAllAccounts() {
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

        return total;
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

            newPassword = EncryptPasswords.encryptPassword(newPassword);

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
        System.out.print("Enter a new name: ");
        String name = "";
        try {
            name = inputReader.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        this.setCustomerName(name);
        this.updateNameDatabase(name);
    }

    private void updateNameDatabase(String newName) {
        try(
            Connection conn = DBUtil.getConnection(DBType.MYSQL_DB);
            PreparedStatement pstmt = conn.prepareStatement(
                    "UPDATE customers SET CustomerName = ? WHERE CustomerId = ? LIMIT 1",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY)
        ) {
            pstmt.setString(1, newName);
            pstmt.setInt(2, this.customerId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            DBUtil.showErrorMessage(e);
        }
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
        isaAccount = null;
        currentAccount = null;
        savingsAccount = null;

        if(inputReader != null) {
            try {
                inputReader.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }

        if(input != null) {
            input.close();
        }

        System.exit(0);
    }

    public void setCustomerPassword(String customerPassword) {
        this.customerPassword = customerPassword;
    }
    public String getCustomerPassword() {
        return customerPassword;
    }
}
