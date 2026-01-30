package com.bank.app.repository;

import java.util.*;

import com.bank.app.model.Account;
import com.bank.app.model.Customer;
import com.bank.app.model.Transaction;


public interface BankRepository {
	
	//Customer
	void saveCustomer(Customer customer);
	Optional<Customer> getCustomersById(String customerId);
	
	//Account
	void saveAccount(Account account);
	Optional<Account> getAccountsByNumber(String accountNumber);
	List<Account> getAccountsByCustomerId(String customerId);
	List<Account> getAllAccounts();
	
	//Transaction
	void saveTransaction(Transaction transaction);
	List<Transaction> getTransactionsByAccount(String accountNumber);
	List<Transaction> getAllTransactions();
}

/*
private Map<String,Customer> customersById = new HashMap<>();
private Map<String,Account> accountByNumber = new HashMap<>();
private List<Transaction> allTransactions = new ArrayList<>();


//customer operations//

public void saveCustomer(Customer customer) {
	if(customer == null) return;
	customersById.put(customer.getCustomerId(), customer);
}

public Optional<Customer>findCustomersById(String customerId){
	if(customerId == null) {
		return Optional.empty();
	}
	return Optional.ofNullable(customersById.get(customerId));
}

public Collection<Customer> getAllCustomers(){
	return Collections.unmodifiableCollection(customersById.values());
}


//Account Operations//
public void saveAccount(Account account) {
	if(account == null) return;
	accountByNumber.put(account.getAccountNumber(), account);
}

public Optional<Account> findAccountsByNumber(String accountNumber){
	if(accountNumber == null) {
		return Optional.empty();
	}
	return Optional.ofNullable((accountByNumber.get(accountNumber)));
}

public Collection<Account> getAllAccounts(){
	return Collections.unmodifiableCollection(accountByNumber.values());
}

//Transaction Operations // 
public void saveTransaction(Transaction transactions) {
	if(transactions == null) return;
	allTransactions.add(transactions);
}
public List<Transaction> getAllTransactions(){
	return Collections.unmodifiableList(allTransactions);
}
*/