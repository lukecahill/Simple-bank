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
        super.printBalance("isa_account_balance.txt");
    }
}
