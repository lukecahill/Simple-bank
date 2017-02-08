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
        super.printBalance("isa_account_balance.txt");
    }
}
