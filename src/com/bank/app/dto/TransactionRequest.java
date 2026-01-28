package com.bank.app.dto;

import com.bank.app.enums.AccountTypes;

public class TransactionRequest {
	private String accountNumber;
	private AccountTypes accountType;
	private double amount;
	
	
	public TransactionRequest(String accountNumber, AccountTypes accountType, double amount) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.amount = amount;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public AccountTypes getAccountType() {
		return accountType;
	}
	
	public double getAmount() {
		return amount;
	}
}
