package com.bank.app;

import com.bank.app.model.Customer;
import com.bank.app.model.SavingsAccount;

public class MainApplication {

    public static void main(String[] args) {
        System.out.println("Mini Banking Transaction Processing System started.");

        Customer customer = new Customer("AB001", "Aditya Binjagermath", "ABCDE1234F");

        SavingsAccount savings = new SavingsAccount("AB2202ILF45", 1000.0, 0.04);

        customer.addAccount(savings);

        savings.credit(5000.0);
        savings.debit(2000.0);

        System.out.println("Customer: " + customer.getName());
        System.out.println("Net worth: " + customer.calculateNetWorth());
        System.out.println("Has frozen accounts: " + customer.hasFrozeenAccounts());
    }
}
