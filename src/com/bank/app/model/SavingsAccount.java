package com.bank.app.model;

import com.bank.app.enums.AccountTypes;

public class SavingsAccount extends Account{
	
	private double interestRate;
	
	public SavingsAccount(String accountNumber, double interestRate, double minimumBalanace) {
		super(accountNumber,AccountTypes.SAVINGS,minimumBalanace);
		this.interestRate = interestRate;
	}
	
	public void applyInterest() {
		double interest = balance * interestRate;
		credit(interest);
	}
	
	@Override
	public void applyMonthlyRules() {
		applyInterest();
	}
	
	public double getInterest() {
		return interestRate;
	}
}
