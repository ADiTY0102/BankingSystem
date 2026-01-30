package com.bank.app.model;

import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;

public class CurrentAccount extends Account{
	private double overDraftLimit;
	
	public CurrentAccount(
			String accountNumber,
			double minimumBalance,
			double overDraftLimit 
			) {
		super(accountNumber,AccountTypes.CURRENT,minimumBalance);
		this.overDraftLimit = overDraftLimit;
	}
	
	
	@Override
	public boolean canDebit(double amount) {
		return balance-amount >= -overDraftLimit;
	}
	
	public void applyCharges() {
		if(balance < 0) {
			double fee = 50.0;
			balance -= fee;
		}
	}
	
	@Override
	public void applyMonthlyRules() {
		applyCharges();
	}
	 public double getOverDraftLimit() {
		 return overDraftLimit;
	 }


	 public AccountStatus setStatus(AccountStatus status) {
		 return status;
	 }
}
