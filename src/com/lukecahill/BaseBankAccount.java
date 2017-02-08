package com.lukecahill;

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

    protected abstract void load();
    protected abstract void deposit();
    protected abstract void withdraw();
    protected abstract void showBalance();
    protected abstract void printBalance();

}
