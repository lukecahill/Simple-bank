package com.lukecahill;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public abstract class BaseBankAccount {
    protected String name;
    protected String Description;

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
