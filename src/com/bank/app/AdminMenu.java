package com.bank.app;

import java.util.Scanner;

import com.bank.app.service.BankService;
import com.bank.app.service.ReportingService;
import com.bank.app.util.ConsoleUI;

public class AdminMenu {

    public static void show(Scanner scanner,
                            BankService bankService,
                            ReportingService reportingService) {

        boolean running = true;

        while (running) {
            ConsoleUI.printHeader("Banking System ADMIN");
            System.out.println("1. Create customer");
            System.out.println("2. Create account");
            System.out.println("3. Change account status");
            System.out.println("4. Show customer net worth report");
            System.out.println("5. Show top accounts by balance");
            System.out.println("6. Show failed transactions");
            System.out.println("7. Apply monthly processing");
            System.out.println("0. Logout");
            ConsoleUI.printSeparator();
            System.out.print("Choose option: ");

            int choice = Integer.parseInt(scanner.nextLine());
            ConsoleUI.printSeparator();

            try {
                switch (choice) {
                    case 1 -> MenuActions.createCustomer(scanner, bankService);
                    case 2 -> MenuActions.createAccount(scanner, bankService);
                    case 3 -> MenuActions.changeAccountStatus(scanner, bankService);
                    case 4 -> MenuActions.showCustomerNetWorth(reportingService);
                    case 5 -> MenuActions.showTopAccounts(scanner, reportingService);
                    case 6 -> MenuActions.showFailedTransactions(reportingService);
                    case 7 -> bankService.applyMonthlyProcess();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
