package com.bank.app.service;

import java.util.*;
import java.util.stream.Collectors;

import com.bank.app.enums.AccountTypes;
import com.bank.app.model.Account;
import com.bank.app.model.Customer;
import com.bank.app.model.Transaction;
import com.bank.app.repository.AccountDao;
import com.bank.app.repository.CustomerDao;
import com.bank.app.repository.TransactionDao;
import com.bank.app.security.SessionContext;

public class ReportingService {

    private final AccountDao accountDao;
    private final CustomerDao customerDao;
    private final TransactionDao transactionDao;

    public ReportingService(AccountDao accountDao,
                            CustomerDao customerDao,
                            TransactionDao transactionDao) {
        this.accountDao = accountDao;
        this.customerDao = customerDao;
        this.transactionDao = transactionDao;
    }

    // Reporting Top Accounts by Balance
    public List<Account> getTopAccountsByBalance(int count) {
    	SessionContext.requireAdmin();
        // if AccountDao has a findTopByBalance, use it directly
        List<Account> all = accountDao.findAll();
        return all.stream()
                .sorted(Comparator.comparingDouble(Account::getBalance).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    // Reporting Failed Transactions 
    public List<Transaction> getFailedTransactions() {
    	SessionContext.requireAdmin();
        // if TransactionDao has findFailedTransactions, you can just call that
        List<Transaction> all = transactionDao.findAll(); // or transactionDao.findFailedTransactions()
        return all.stream()
                .filter(t -> !t.isSuccess())
                .collect(Collectors.toList());
    }

    // Reporting Grouped Accounts by Types
    public Map<AccountTypes, List<Account>> grpupAccountsByType() {
        List<Account> all = accountDao.findAll();
        return all.stream()
                .collect(Collectors.groupingBy(Account::getAccountType));
    }

    // Reporting Customer via net worth
    public Map<Customer, Double> generateCustomerNetWorthReport() {
    	SessionContext.requireAdmin();
        Map<Customer, Double> report = new LinkedHashMap<>();

        List<Customer> customers = customerDao.findAll();

        for (Customer customer : customers) {

            double netWorth = accountDao
                    .findByCustomerId(customer.getCustomerId())
                    .stream()
                    .mapToDouble(Account::getBalance)
                    .sum();

            report.put(customer, netWorth);
        }

        return report;
    }

}
