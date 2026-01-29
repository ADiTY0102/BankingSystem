package com.bank.app;

import java.util.Scanner;

import com.bank.app.repository.BankRepository;
import com.bank.app.service.BankService;
import com.bank.app.service.ReportingService;
import com.bank.app.service.TransactionService;

public class MainApplication {

    public static void main(String[] args) {
    	
        BankRepository repository = new BankRepository();
        TransactionService transactionService = new TransactionService(repository);
        BankService bankService = new BankService(repository, transactionService);
        ReportingService reportingService = new ReportingService(repository);

        Scanner scanner = new Scanner(System.in);

        boolean running = true;
        while (running) {
        	System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          Banking System              ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.println("1. Create customer");
            System.out.println("2. Create account");
            System.out.println("3. Credit account");
            System.out.println("4. Debit account");
            System.out.println("5. Transfer funds");
            System.out.println("6. Show customer net worth report");
            System.out.println("7. Show top accounts by balance");
            System.out.println("8. Show failed transactions");
            System.out.println("9. Apply monthly processing");
            System.out.println("0. Exit");
            System.out.println("===================================");
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            
            System.out.println("===================================");
            try {
                switch (choice) {
                    case 1 -> MenuActions.createCustomer(scanner, bankService);
                    case 2 -> MenuActions.createAccount(scanner, bankService);
                    case 3 -> MenuActions.creditAccount(scanner, bankService, transactionService);
                    case 4 -> MenuActions.debitAccount(scanner, bankService, transactionService);
                    case 5 -> MenuActions.transferFunds(scanner, bankService);
                    case 6 -> MenuActions.showCustomerNetWorth(reportingService);
                    case 7 -> MenuActions.showTopAccounts(scanner, reportingService);
                    case 8 -> MenuActions.showFailedTransactions(reportingService);
                    case 9 -> bankService.applyMonthlyProcess();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
        
        System.out.println("Exiting application.");
    }
}





























//
////Testing Module 1 and Module 2
//
///*  System.out.println("Mini Banking Transaction Processing System started.");
//BankRepository repository = new BankRepository();
//Customer customer = new Customer("AB001", "Aditya Binjagermath", "ABCDE1234F");
//
//SavingsAccount savings = new SavingsAccount("AB2202ILF45", 1000.0, 0.04);
//
//customer.addAccount(savings);
//
//savings.credit(5000.0);
//savings.debit(2000.0);
//
//System.out.println("Customer: " + customer.getName());
//System.out.println("Net worth: " + customer.calculateNetWorth());
//System.out.println("Has frozen accounts: " + customer.hasFrozeenAccounts());
//
//*/  
//
///*
////Testing Module 1 and Module 2 => Repository Implementation
//
// 
//System.out.println("Mini Banking Transaction Processing System started.");
//
//BankRepository repository = new BankRepository();
//
////Customer customer = new Customer("AB22", "arpita baraskar", "ABCDE1234F");
////SavingsAccount savings = new SavingsAccount("AB02", 2000.0, 0.04);
////Testing module 3 with Dto, utils and Updations in Account and Transactions
//
//Customer customer = new Customer(
//        GenerateId.generateCustomerId(),
//        "Aditya Binjagermath",
//        "ABCDE1234F"
//);
//
//SavingsAccount savings = new SavingsAccount(
//		GenerateId.generateAccountId(),
//        10000.0,
//        0.04
//);
//
//
//customer.addAccount(savings);
//
//repository.saveCustomer(customer);
//repository.saveAccount(savings);
//
//savings.credit(50000.0);
//savings.debit(2000.0);
//
//repository.saveAccount(savings); //
//
//repository.findCustomersById(customer.getCustomerId()).ifPresent(c -> {
//    System.out.println("Customer from repository: " + customer.getName());
//    System.out.println("Net worth from repository: " + customer.calculateNetWorth());
//});
//
//repository.findAccountsByNumber(savings.getAccountNumber()).ifPresent(a -> {
//    System.out.println("Account from repository balance: " + a.getBalance());
//});
//*/
//
//
////Service Module Testing 
//
////BankRepository repository = new BankRepository();
////BankService bankService = new BankService(repository);
///*       
//// 1) Create customer from service
//Customer customer = bankService.createCustomer(
//        "Aditya Binjagermath",
//        "ABCDE1234F"
//);
//
//// 2) Prepare account creation request
//CreateAccountRequest request = new CreateAccountRequest(
//        customer.getCustomerId(),
//        AccountTypes.SAVINGS,
//        50000.0,   // initialDeposit
//        10000.0,   // minimumBalance
//        0.04,      // interestRate 
//        0.0        // overdraftLimit 
//);
//
//// 3) Create account through service
//Account account = bankService.createAccount(request);
//
//System.out.println("Created customer ID: " + customer.getCustomerId());
//System.out.println("Created account number: " + account.getAccountNumber());
//System.out.println("Account balance: " + account.getBalance());
//
//// 4) Apply monthly processing (for savings, adds interest)
//bankService.applyMonthlyProcess();
//System.out.println("Balance after monthly processing: " + account.getBalance());
//
//
//*/
//
//// Module 5 Banking Services with transaction Service Testing
//BankRepository repository = new BankRepository();
//TransactionService transactionService = new TransactionService(repository);
//BankService bankService = new BankService(repository, transactionService);
//
//Customer c1 = bankService.createCustomer("Mr. Aditya Virendra", "PAN1111");
//Customer c2 = bankService.createCustomer("Miss. Arpita baraskar", "PAN2222");
//
//CreateAccountRequest r1 = new CreateAccountRequest(
//        c1.getCustomerId(),
//        AccountTypes.SAVINGS,
//        50000.0,
//        10000.0,
//        0.04,
//        0.0
//);
//Account acc1 = bankService.createAccount(r1);
//
//CreateAccountRequest r2 = new CreateAccountRequest(
//        c2.getCustomerId(),
//        AccountTypes.SAVINGS,
//        10000.0,
//        5000.0,
//        0.04,
//        0.0
//);
//Account acc2 = bankService.createAccount(r2);
//
//TransferRequest tr = new TransferRequest(
//        acc1.getAccountNumber(),
//        acc2.getAccountNumber(),
//        5000.0
//);
//bankService.transferFunds(tr);
//
//System.out.println("Aditya's bank balance after transfer: " + acc1.getBalance());
//System.out.println("Arpita's bank balance after transfer: " + acc2.getBalance());
//
//}
//}
