package com.lukecahill;

import java.util.Scanner;

/**
 *
 * Created by Luke on 08/02/2017.
 *
 */
public class Customer {

    private int CustomerId;
    private String name;

    private static Scanner input = new Scanner(System.in);
    private IsaAccount isaAccount;
    private CurrentAccount currentAccount;
    private SavingsAccount savingsAccount;

    public void showCustomerOptions() {
        System.out.println("Choose an option:");
        System.out.println("1 - Show current account options");
        System.out.println("2 - Show ISA account options");
        System.out.println("3 - Show Savings account options");
        System.out.println("q - Quit:");

        String choice = input.next();

        switch(choice) {
            case "1":
                if(currentAccount == null) {
                    currentAccount = new CurrentAccount(getCustomerId());
                }
                currentAccount.showOptions();
                break;
            case "2":
                if(isaAccount == null) {
                    isaAccount = new IsaAccount();
                }
                isaAccount.showOptions();
                break;
            case "3":
                if(savingsAccount == null) {
                    savingsAccount = new SavingsAccount();
                }
                savingsAccount.showOptions();
                break;
            case "q":
                quitBank();
                break;
            default:
                System.out.println("That option does not exist. Please retry.");
                break;
        }
    }

    public int getCustomerId() {
        return CustomerId;
    }
    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getName() {
        return name;
    }
    public boolean setName(String name) {
        if(name.isEmpty() || name.equalsIgnoreCase("")) {
            return false;
        }

        this.name = name;
        return true;
    }

    public void quitBank() {
        System.exit(0);
    }

}
