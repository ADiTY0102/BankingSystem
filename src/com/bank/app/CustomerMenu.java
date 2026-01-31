package com.bank.app;

//package com.bank.app;

import java.util.Scanner;

import com.bank.app.service.BankService;
import com.bank.app.service.TransactionService;
import com.bank.app.util.ConsoleUI;

public class CustomerMenu {

    public static void show(Scanner scanner,
                            BankService bankService,
                            TransactionService transactionService) {

        boolean running = true;

        while (running) {
            ConsoleUI.printHeader("Banking System CUSTOMER");
            System.out.println("1. Credit account");
            System.out.println("2. Debit account");
            System.out.println("3. Transfer funds");
            System.out.println("0. Logout");
            ConsoleUI.printSeparator();
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            ConsoleUI.printSeparator();

            try {
                switch (choice) {
                    case 1 -> MenuActions.creditAccount(scanner, bankService, transactionService);
                    case 2 -> MenuActions.debitAccount(scanner, bankService, transactionService);
                    case 3 -> MenuActions.transferFunds(scanner, bankService);
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
