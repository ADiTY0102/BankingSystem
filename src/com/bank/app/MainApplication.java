package com.bank.app;

import com.bank.app.model.Customer;
import com.bank.app.model.SavingsAccount;
import com.bank.app.repository.BankRepository;
import com.bank.app.util.GenerateId;

public class MainApplication {

    public static void main(String[] args) {
    	
    	//Testing Module 1 and Module 2
    	
    /*  System.out.println("Mini Banking Transaction Processing System started.");
        BankRepository repository = new BankRepository();
        Customer customer = new Customer("AB001", "Aditya Binjagermath", "ABCDE1234F");

        SavingsAccount savings = new SavingsAccount("AB2202ILF45", 1000.0, 0.04);

        customer.addAccount(savings);

        savings.credit(5000.0);
        savings.debit(2000.0);

        System.out.println("Customer: " + customer.getName());
        System.out.println("Net worth: " + customer.calculateNetWorth());
        System.out.println("Has frozen accounts: " + customer.hasFrozeenAccounts());
        
     */  
        
    	//Testing Module 1 and Module 2 => Repository Implementation
        
         
	    System.out.println("Mini Banking Transaction Processing System started.");

        BankRepository repository = new BankRepository();

//        Customer customer = new Customer("AB22", "arpita baraskar", "ABCDE1234F");
//        SavingsAccount savings = new SavingsAccount("AB02", 2000.0, 0.04);

        
        Customer customer = new Customer(
                GenerateId.generateCustomerId(),
                "Aditya Binjagermath",
                "ABCDE1234F"
        );

        SavingsAccount savings = new SavingsAccount(
        		GenerateId.generateAccountId(),
                10000.0,
                0.04
        );
        
        
        customer.addAccount(savings);

        repository.saveCustomer(customer);
        repository.saveAccount(savings);

        savings.credit(50000.0);
        savings.debit(2000.0);

        repository.saveAccount(savings); // update stored account

        repository.findCustomersById(customer.getCustomerId()).ifPresent(c -> {
            System.out.println("Customer from repository: " + customer.getName());
            System.out.println("Net worth from repository: " + customer.calculateNetWorth());
        });

        repository.findAccountsByNumber(savings.getAccountNumber()).ifPresent(a -> {
            System.out.println("Account from repository balance: " + a.getBalance());
        });
    }
}
