package com.bank.app.dto;

import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.TransactionType;

public class TransactionRequest {
	private String accountNumber;
	private TransactionType transactionType;
	private double amount;
	
	
	public TransactionRequest(String accountNumber, TransactionType transactionType, double amount) {
		this.accountNumber = accountNumber;
//		this.accountType = accountType;
		this.transactionType = transactionType;
		this.amount = amount;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public TransactionType getTransactionType() {
		return transactionType;
	}
	
	public double getAmount() {
		return amount;
	}
}
