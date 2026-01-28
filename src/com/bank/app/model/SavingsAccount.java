package com.bank.app.model;

import com.bank.app.enums.AccountTypes;

public class SavingsAccount extends Account {

    private double interestRate; 

    public SavingsAccount(String accountNumber,
                          double minimumBalance,
                          double interestRate) {
        super(accountNumber, AccountTypes.SAVINGS, minimumBalance);
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

    public double getInterestRate() {
        return interestRate;
    }
}

