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
    protected String Description;
    protected double balance;
    protected int customerId;
    protected int currentAccountId;

    String[] accountOptions = {
        "1 - Show balance",
        "2 - Deposit money",
        "3 - Withdraw money",
        "4 - Print balance"
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

    protected abstract void load();
    protected abstract void deposit();
    protected abstract void withdraw();
    protected abstract void showBalance();

}
