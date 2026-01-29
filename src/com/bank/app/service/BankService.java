package com.bank.app.service;


import java.util.Optional;

import com.bank.app.dto.AccountStatusChangeRequest;
import com.bank.app.dto.CreateAccountRequest;
import com.bank.app.dto.TransactionRequest;
import com.bank.app.dto.TransferRequest;
import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.TransactionType;
import com.bank.app.model.Account;
import com.bank.app.model.CurrentAccount;
import com.bank.app.model.Customer;
import com.bank.app.model.SavingsAccount;
import com.bank.app.repository.BankRepository;
import com.bank.app.util.GenerateId;

public class BankService {

	   private final BankRepository repository;
	    private final TransactionService transactionService;

	    public BankService(BankRepository repository, TransactionService transactionService) {
	        this.repository = repository;
	        this.transactionService = transactionService;
	    }

	//  <I> Creating Customers
	public Customer createCustomer(String name, String pan) {
		String customerId = GenerateId.generateCustomerId();
		Customer customer = new Customer(customerId, name, pan);
		repository.saveCustomer(customer);
		return customer;
	}



	//  <II> Create Account => CreateAccountRequest
	public Account createAccount(CreateAccountRequest request) {

		Optional<Customer> optionalCustomer = repository.findCustomersById(request.getCustomerId());
		if(optionalCustomer.isEmpty()) {
			throw new IllegalArgumentException("Customer Not Found" + request.getCustomerId());
		}

		Customer customer = optionalCustomer.get();

		String accountNumber = GenerateId.generateAccountId();
		Account account;

		if(request.getAccountType() == AccountTypes.SAVINGS) {
			account = new SavingsAccount(accountNumber, 
					request.getMinimumBalance(), 
					request.getInterestRate()); 
		}else if(request.getAccountType() == AccountTypes.CURRENT){
			account = new CurrentAccount( accountNumber,
					request.getMinimumBalance(),
					request.getOverdraftLimit()
					);	
		}else {
			throw new IllegalArgumentException("Unsupported Account Type: "+ request.getAccountType());
		}




		//initial deposit agar hai tho!

		if(request.getInitialDiposit() > 0 ) {
			account.credit(request.getInitialDiposit());
		}

		//Saving Everything...!!
		customer.addAccount(account);
		repository.saveAccount(account);
		repository.saveCustomer(customer);


		return account;

	}



	//  (III) Changing Account Status..!!

	public void changeAccountStatus(AccountStatusChangeRequest request) {
		Account account = repository.findAccountsByNumber(request.getAccountNumber())
				.orElseThrow(()-> 
				new IllegalArgumentException("Account Not Found: "+request.getAccountNumber())
						);

		AccountStatus newStatus = request.getAccountStatus();
		switch (newStatus) {
		case ACTIVE -> account.unFreezeAccount();
		case FROZEN -> account.freezeAccount();
		case CLOSED -> account.freezeAccount();
		}
		repository.saveAccount(account);
	}
	
	
	
	
	
	//  (IV) Transfer Funds..!!

	public void transferFunds(TransferRequest request) {
		Account from = repository.findAccountsByNumber(request.getFromAccountNumber())
				.orElseThrow(() -> new IllegalArgumentException(
						"From account not found: " + request.getFromAccountNumber())
						);

		Account to = repository.findAccountsByNumber(request.getToAccountNumber())
				.orElseThrow(() -> new IllegalArgumentException(
						"To account not found: " + request.getToAccountNumber())
						);

		double amount = request.getAmount();

		if (!from.canDebit(amount)) {
			System.out.println("Transfer failed: insufficient funds.");
			return;
		}

		TransactionRequest debitReq = new TransactionRequest(
				from.getAccountNumber(),
				TransactionType.DEBIT,
				amount
				);

		TransactionRequest creditReq = new TransactionRequest(
				to.getAccountNumber(),
				TransactionType.CREDIT,
				amount
				);

		transactionService.processTransaction(debitReq, from);
		transactionService.processTransaction(creditReq, to);
	}



	//  (V) Applying Monthly Rules and Policies!!

	public void applyMonthlyProcess() {
		for(Account account : repository.getAllAccounts()) {
			account.applyMonthlyRules();
			repository.saveAccount(account);
		}
	}

	// Accessing Repository from Bank Service
	
	public BankRepository getRepository() {
	    return repository;
	}


}
