package com.bank.app;

import java.util.Scanner;

import com.bank.app.dto.CreateAccountRequest;
import com.bank.app.dto.TransactionRequest;
import com.bank.app.dto.TransferRequest;
import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.TransactionType;
import com.bank.app.model.Account;
import com.bank.app.model.Customer;
import com.bank.app.model.Transaction;
import com.bank.app.service.BankService;
import com.bank.app.service.ReportingService;
import com.bank.app.service.TransactionService;
import com.bank.app.util.DateUtil;

public class MenuActions {

	public static void createCustomer(Scanner scanner, BankService bankService) {
		System.out.print("Enter customer name: ");
		String name = scanner.nextLine();
		System.out.print("Enter PAN: ");
		String pan = scanner.nextLine();

		Customer customer = bankService.createCustomer(name, pan);
		System.out.println(DateUtil.now().toLocalDate()+" "+"Customer created with ID: " + customer.getCustomerId());
	}

	public static void createAccount(Scanner scanner, BankService bankService) {
		System.out.print("Enter customer ID: ");
		String customerId = scanner.nextLine();

		System.out.print("Account type (1: SAVINGS, 2: CURRENT): ");
		int typeChoice = Integer.parseInt(scanner.nextLine());
		AccountTypes type = (typeChoice == 1) ? AccountTypes.SAVINGS : AccountTypes.CURRENT;

		System.out.print("Initial deposit: ");
		double initialDeposit = Double.parseDouble(scanner.nextLine());

		System.out.print("Minimum balance: ");
		double minBalance = Double.parseDouble(scanner.nextLine());

		double interestRate = 0.0;
		double overdraftLimit = 0.0;

		if (type == AccountTypes.SAVINGS) {
			System.out.print("Interest rate (e.g., 0.04 for 4%): ");
			interestRate = Double.parseDouble(scanner.nextLine());
		} else {
			System.out.print("Overdraft limit: ");
			overdraftLimit = Double.parseDouble(scanner.nextLine());
		}

		CreateAccountRequest req = new CreateAccountRequest(
				customerId,
				type,
				initialDeposit,
				minBalance,
				interestRate,
				overdraftLimit
				);

		Account account = bankService.createAccount(req);
		System.out.println(DateUtil.now().toLocalDate()+" "+"Account created with number: " + account.getAccountNumber());
	}

	public static void creditAccount(Scanner scanner,
			BankService bankService,
			TransactionService transactionService) {
		System.out.print("Enter account number: ");
		String accNo = scanner.nextLine();
		System.out.print("Amount to credit: ");
		double amount = Double.parseDouble(scanner.nextLine());

		Account account = bankService
				.findAccountByNumber(accNo)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));

		TransactionRequest req = new TransactionRequest(
				accNo,
				TransactionType.CREDIT,
				amount
				);

		Transaction tx = transactionService.processTransaction(req, account);
		System.out.println("Credited at: " + DateUtil.now().toLocalDate() +
				" With status: " + (tx.isSuccess() ? "SUCCESS" : "FAILED"));
	}

	public static void debitAccount(Scanner scanner,
			BankService bankService,
			TransactionService transactionService) {
		System.out.print("Enter account number: ");
		String accNo = scanner.nextLine();
		System.out.print("Amount to debit: ");
		double amount = Double.parseDouble(scanner.nextLine());

		Account account = bankService
				.findAccountByNumber(accNo)
				.orElseThrow(() -> new IllegalArgumentException("Account not found"));

		TransactionRequest req = new TransactionRequest(
				accNo,
				TransactionType.DEBIT,
				amount
				);

		Transaction tx = transactionService.processTransaction(req, account);
		System.out.println("Debited at: " + DateUtil.now().toLocalDate() +
				" With Status: " + (tx.isSuccess() ? "SUCCESS" : "FAILED"));
	}

	public static void transferFunds(Scanner scanner, BankService bankService) {
		System.out.print("From account number: ");
		String from = scanner.nextLine();
		System.out.print("To account number: ");
		String to = scanner.nextLine();
		System.out.print("Amount: ");
		double amount = Double.parseDouble(scanner.nextLine());

		TransferRequest tr = new TransferRequest(from, to, amount);
		bankService.transferFunds(tr);
		System.out.println("Transfer attempted. Check balances.");
	}

	public static void showCustomerNetWorth(ReportingService reportingService) {
		reportingService.generateCustomerNetWorthReport().forEach((customer, netWorth) ->
		System.out.println(customer.getCustomerId() + " - " +
				customer.getName() + " : " + netWorth)
				);
	}

	public static void showTopAccounts(Scanner scanner, ReportingService reportingService) {
		System.out.print("How many accounts to show? ");
		int n = Integer.parseInt(scanner.nextLine());

		reportingService.getTopAccountsByBalance(n).forEach(a ->
		System.out.println(a.getAccountNumber() + " -> " + a.getBalance())
				);
	}

	public static void showFailedTransactions(ReportingService reportingService) {
		if(reportingService.getFailedTransactions()== null) {
			System.out.println("No Failed Transactions!");
		}
		reportingService.getFailedTransactions().forEach(t ->
		System.out.println(t.getTransactionId() + " " +
				t.getTransactionType() + " " +
				t.getAmount() + " " +
				t.getFailureReason())
				);
	}
}
