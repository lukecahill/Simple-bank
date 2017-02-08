package com.lukecahill;

import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */

public class SavingsAccount extends BaseBankAccount {

    public SavingsAccount() {
        System.out.println("Created savings account");
    }

    protected void showBalance() {

    }

    protected void deposit() {

    }

    protected void withdraw() {

    }

    protected void load() {

    }

    protected void printBalance() {

        try(PrintWriter writer = new PrintWriter("current_account_balance.txt")) {
            writer.write("The current balance of the account is: " + balance);
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
