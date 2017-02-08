package com.lukecahill;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Luke on 08/02/2017.
 */
public class IsaAccount extends BaseBankAccount {

    public IsaAccount() {
        System.out.println("Created ISA");
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

        try(PrintWriter writer = new PrintWriter("isa_account_balance.txt")) {
            writer.write("The current balance of the account is: " + balance);
            System.out.println("Saved to \"isa_account_balance.txt\".");
        } catch(IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
