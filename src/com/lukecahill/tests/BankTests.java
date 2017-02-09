package com.lukecahill.tests;

import com.lukecahill.*;
import org.junit.Test;
import org.junit.Assert;
/**
 *
 * Created by Luke on 09/02/2017.
 *
 */
public class BankTests {

    private static final double DELTA = 1e-15;

    @Test
    public void correctCalculation() {
        Customer customer = new Customer();
        customer.setCustomerId(3);
        double result = customer.totalAmountInAllAccounts();

        Assert.assertEquals(15200.0d, result, DELTA);
    }

    @Test
    public void setCustomersName() {
        Customer customer = new Customer();
        boolean result = customer.setCustomerName("Luke");

        Assert.assertEquals(true, result);
    }

    @Test
    public void getCurrentAccountBalance() {
        CurrentAccount current = new CurrentAccount(3);
        double result = current.getBalance();

        Assert.assertEquals(200.0d, result, DELTA);
    }

    @Test
    public void getIsaAccountBalance() {
        IsaAccount account = new IsaAccount(3);
        double result = account.getBalance();

        Assert.assertEquals(15000.0d, result, DELTA);
    }

    @Test
    public void getSavingsBalance() {
        SavingsAccount account = new SavingsAccount(3);
        double result = account.getBalance();

        Assert.assertEquals(0.0d, result, DELTA);
    }
}
