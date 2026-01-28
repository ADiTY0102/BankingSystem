package com.bank.app.service;

import java.util.*;
import java.util.stream.Collectors;

import com.bank.app.enums.AccountTypes;
import com.bank.app.model.Account;
import com.bank.app.model.Customer;
import com.bank.app.model.Transaction;
import com.bank.app.repository.BankRepository;

public class ReportingService {
	
	private final BankRepository repository;
	
	public ReportingService(BankRepository repository) {
		this.repository = repository;
	}
	
	//Reporting Top Accounts by Balance
	public List <Account> getTopAccountsByBalance(int count){
		return repository.getAllAccounts().stream()
				.sorted(Comparator.comparingDouble(Account :: getBalance).reversed())
				.limit(count)
				.collect(Collectors.toList());
	}
	
	//Reporting Failed Transactions 
	public List<Transaction> getFailedTransactions(){
		return repository.getAllTransactions().stream()
				.filter(t -> !t.isSuccess())
				.collect(Collectors.toList());
	}
	
	
	//Reporting Grouped Accounts by Types
	public Map<AccountTypes,List<Account>> grpupAccountsByType(){
		return repository.getAllAccounts().stream()
				.collect(Collectors.groupingBy(Account :: getAccountType));
	}
	
	

    //Reporting Customer via net worth
    public Map<Customer, Double> generateCustomerNetWorthReport() {
        return repository.getAllCustomers().stream()
                .collect(Collectors.toMap(
                        customer -> customer,
                        Customer::calculateNetWorth
                ));
    }
	
}
