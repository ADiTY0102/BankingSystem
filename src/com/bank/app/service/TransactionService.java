package com.bank.app.service;

import java.time.LocalDate;
import java.util.List;

import com.bank.app.dto.TransactionRequest;
import com.bank.app.enums.TransactionType;
import com.bank.app.model.Account;
import com.bank.app.model.Transaction;
import com.bank.app.repository.AccountDao;
import com.bank.app.repository.TransactionDao;

public class TransactionService {

    private final TransactionDao transactionDao;
    private final AccountDao accountDao;

    public TransactionService(TransactionDao transactionDao, AccountDao accountDao) {
        this.transactionDao = transactionDao;
        this.accountDao = accountDao;
    }

    public Transaction processTransaction(TransactionRequest request, Account account) {
        double amount = request.getAmount();
        TransactionType type = request.getTransactionType();

        Transaction tx;

        if (amount <= 0) {
            tx = Transaction.failure(type, amount, "Amount must be greater than 0");
            transactionDao.save(tx, account.getAccountNumber());
            return tx;
        }

        if (type == TransactionType.DEBIT) {
            if (!account.canDebit(amount)) {
                tx = Transaction.failure(type, amount, "Insufficient balance");
                transactionDao.save(tx, account.getAccountNumber());
                return tx;
            }
            account.debit(amount);
            tx = Transaction.success(type, amount);
        } else {
            account.credit(amount);
            tx = Transaction.success(type, amount);
        }

        accountDao.update(account);
        transactionDao.save(tx, account.getAccountNumber());

        return tx;
    }

    public void reverseTransaction(String transactionId, Account account) {
        Transaction original = transactionDao.findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found: " + transactionId));

        double amount = original.getAmount();
        Transaction reversal;

        if (original.isDebit()) {
            account.credit(amount);
            reversal = Transaction.success(TransactionType.REVERSAL, amount);
        } else if (original.isCredit()) {
            if (!account.canDebit(amount)) {
                reversal = Transaction.failure(TransactionType.REVERSAL, amount, "Insufficient funds for reversal");
            } else {
                account.debit(amount);
                reversal = Transaction.success(TransactionType.REVERSAL, amount);
            }
        } else {
            throw new IllegalArgumentException("Only credit/debit can be reversed");
        }

        transactionDao.save(reversal, account.getAccountNumber());
        accountDao.update(account);
    }

    public boolean validateDailyLimit(Account account, double amount) {
        double dailyLimit = 100000.0;
        LocalDate today = java.time.LocalDate.now();

        List<Transaction> allTx = transactionDao.findByAccount(account.getAccountNumber());

        double todaysDebits = allTx.stream()
                .filter(Transaction::isDebit)
                .filter(tx -> tx.getTimestamp().toLocalDate().isEqual(today))
                .mapToDouble(Transaction::getAmount)
                .sum();

        return todaysDebits + amount <= dailyLimit;
    }
}
