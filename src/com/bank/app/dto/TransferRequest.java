package com.bank.app.dto;

public class TransferRequest {
	private String fromAccountNumber;
	private String toAccountNumber;
	private double amount;
	
	public TransferRequest(String fromAccountNumber, String toAccountNumber, double amount) {
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.amount = amount;
	}
	
	public String getFromAccountNumber() {
		return fromAccountNumber;
	}
	public String getToAccountNumber() {
		return toAccountNumber;
	}
	public double getAmount() {
		return amount;
	}
}
