package com.bank.app.dto;

import com.bank.app.enums.AccountStatus;

public class AccountStatusChangeRequest {
	private String accountNumber;
	private AccountStatus newStatus;
	
	
	//Constructor
	public AccountStatusChangeRequest(String accountNumber, AccountStatus newStatus) {
		this.accountNumber = accountNumber;
		this.newStatus = newStatus;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	public AccountStatus getAccountStatus() {
		return newStatus;
	}
}
