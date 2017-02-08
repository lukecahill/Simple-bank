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

    protected void showBalance() {

    }

    protected void deposit() {

    }

    protected void withdraw() {

    }

    protected void load() {

    }

    protected void printBalance() {
        super.printBalance("savings_account_balance.txt");
    }

}
