package com.bank.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;


public class Customer {
	private String customerId;
	private String name;
	private String pan;

	private List<Account> accounts = new ArrayList<>();
	
	public Customer(String customerId, String name, String pan) {
		this.customerId = customerId;
		this.name = name;
		this.pan = pan;
	}
	
	
	
	public void addAccount(Account account) {
		if(account != null) {
			accounts.add(account);
		}
	}
	
	public void removeAccount(String accountNumber) {
		if(accountNumber == null) return;
		accounts.removeIf(acc -> accountNumber.equals(acc.getAccountNumber()));
	}
	
	
	//extra feature//
	public double calculateNetWorth() {
		return accounts.stream().mapToDouble(Account::getBalance).sum();
	}
	
	
	
	public List<Account> getAccountsByType(AccountTypes type){
		return accounts.stream().filter(acc -> acc.getAccountType() == type)
				.collect(Collectors.toList());
	}
	
	public boolean hasFrozeenAccounts() {
		return accounts.stream().anyMatch(acc -> acc.getStatus() == AccountStatus.FROZEN);
	}
	
	public List<Account> getAccounts(){
		return new ArrayList<>(accounts);
	}

	
	
	//Getters
	public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getPan() {
        return pan;
    }

}
