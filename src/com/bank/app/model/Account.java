package com.bank.app.model;

import java.io.ObjectInputFilter.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.TransactionType;

public abstract class Account {
	protected String accountNumber;
	protected AccountTypes accountType;
	protected AccountStatus status;
	protected double balance;
	protected double minimumBalance;
	protected List<Transaction> transactions = new ArrayList<>();
	
	protected Account(String accountNumber, AccountTypes accountType, double minimumBalance) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.minimumBalance = minimumBalance;
		this.status = AccountStatus.ACTIVE;
		this.balance = 0.00;
	}
	
	
	public void credit(double amount) {
		if(status != AccountStatus.ACTIVE) {
			return;
		}
		balance += amount;
		
		Transaction t = new Transaction(
				null,
				TransactionType.CREDIT,
				amount,
				java.time.LocalDateTime.now(),true,null,null
				);
		transactions.add(t);
	}
	
	public boolean canDebit(double amount) {
		return balance-amount >= minimumBalance;
	}
	
	public void debit(double amount) {
		if(status != AccountStatus.ACTIVE) {
			return;
		}
		if(!canDebit(amount)) {
			Transaction t = new Transaction(
					null,
					TransactionType.DEBIT,
					amount,
					java.time.LocalDateTime.now(),
					false,
					"Insufficient Bank Balance",
					null
					);
			transactions.add(t);
			return;
		}
		balance -= amount;
		Transaction t = new Transaction(
				null,
				TransactionType.DEBIT, 
				amount, java.time.LocalDateTime.now(), 
				true, 
				null, 
				null
				);
		transactions.add(t);
	}
	
	public void freezeAccount() {
		this.status = AccountStatus.FROZEN;
	}
	
	public void unFreezeAccount() {
		this.status = AccountStatus.ACTIVE;
	}
	
	public double getActiveBalance() {
		return balance;
	}
	
	public List <Transaction> getLastNTransactions(int count){
		int size = transactions.size();
		if(count >= size) {
			return Collections.unmodifiableList(transactions);
		}
		return Collections.unmodifiableList(
					transactions.subList(size-count, size)
				);
	}
	
	public abstract void applyMonthlyRules();


	public String getAccountNumber() {
		return accountNumber;
	}


	public AccountTypes getAccountType() {
		return accountType;
	}


	public double getBalance() {
		return balance;
	}

	public AccountStatus getStuts() {
		return status;
	}
	
}
