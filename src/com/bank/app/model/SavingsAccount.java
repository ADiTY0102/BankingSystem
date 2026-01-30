package com.bank.app.model;

import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;

public class SavingsAccount extends Account {

    @SuppressWarnings("unused")
	private AccountStatus status;
	private double interestRate; 

    public SavingsAccount(String accountNumber,
                          double minimumBalance,
                          double interestRate, AccountStatus status
                          ) {
        super(accountNumber, AccountTypes.SAVINGS, minimumBalance);
        this.interestRate = interestRate;
        this.status = status;
    }

    public void applyInterest() {
        double interest = balance * interestRate;  
        credit(interest);                          
    }

    @Override
    public void applyMonthlyRules() {
        applyInterest(); 
    }

    public double getInterestRate() {
        return interestRate;
    }
    
    public double getMinimumBalance() {
    	return minimumBalance;
    }
	public AccountStatus setStatus(AccountStatus status) {
		return status;
		
	}
}

