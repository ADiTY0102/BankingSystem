package com.bank.app.dto;

import com.bank.app.enums.AccountTypes;

public class CreateAccountRequest {
	
	private String customerId;
	private AccountTypes accountType;
	private double initialDiposit;
	private double minimumBalance;
	private double interestRate;
	private double overdraftLimit;
	
	
	public CreateAccountRequest(String customerId,
			AccountTypes accountType,
			double initialDiposit,
			double minimumBalance,
			double interestRate,
			double overdraftLimit) {
		this.accountType = accountType;
		this.customerId = customerId;
		this.initialDiposit = initialDiposit;
		this.minimumBalance = minimumBalance;
		this.interestRate = interestRate;
		this.overdraftLimit = overdraftLimit;
	}
	
	
    public String getCustomerId() {
        return customerId;
    }
    public AccountTypes getAccountType() {
    	return accountType;
    }
    public double getInitialDiposit() {
    	return initialDiposit;
    }
    public double getMinimumBalance() {
    	return minimumBalance;
    }
    public double getInterestRate() {
    	return interestRate;
    }
    public double getOverdraftLimit() {
    	return overdraftLimit;
    }
}
