package com.bank.app.repository;

import com.bank.app.model.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionDao {
    void save(Transaction transaction, String accountNumber);
    List<Transaction> findByAccount(String accountNumber);
    List<Transaction> findFailedTransactions();
    Optional<Transaction> findById(String transactionId);
	List<Transaction> findAll();
}
