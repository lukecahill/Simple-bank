package com.lukecahill;

import java.io.IOException;
import java.io.PrintWriter;
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
    protected int currentAccountId;

    String[] accountOptions = {
        "1 - Show balance",
        "2 - Deposit money",
        "3 - Withdraw money",
        "4 - Print balance",
        "5 - Show account details"
    };

    public void showOptions() {
        for(String option : accountOptions) {
            System.out.println(option);
        }
    }

    protected void printBalance(String filename) {

        try(PrintWriter writer = new PrintWriter(filename)) {
            writer.write("The current balance of the account is: " + balance);
            System.out.println("Saved to \"" + filename + "\".");
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected void showAboutAccount() {
        System.out.println("Name of account: " + this.name);
        System.out.println("Account description: " + this.description);
    }

    protected void withdraw() {

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

    protected void deposit() {
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

    protected void showBalance() {

        System.out.println("Showing balance for: " + this.name);
        System.out.println(this.balance);
    }

    protected abstract void load();

}
