package com.bank.app.service;

import java.util.List;

import com.bank.app.dto.TransactionRequest;
import com.bank.app.enums.TransactionType;
import com.bank.app.model.Account;
import com.bank.app.model.Transaction;
import com.bank.app.repository.BankRepository;

public class TransactionService {

    private final BankRepository repository;

    public TransactionService(BankRepository repository) {
        this.repository = repository;
    }

    public Transaction processTransaction(TransactionRequest request, Account account) {
        double amount = request.getAmount();
        TransactionType type = request.getTransactionType();

        if (amount <= 0) {
            Transaction tx = Transaction.failure(type, amount, "Amount must be Greater than 0");
            repository.saveTransaction(tx);
            return tx;
        }

        if (type == TransactionType.CREDIT) {
            account.credit(amount);
        } else if (type == TransactionType.DEBIT) {
            if (!validateDailyLimit(account, amount)) {
                Transaction tx = Transaction.failure(type, amount, "Daily limit exceeded");
                repository.saveTransaction(tx);
                return tx;
            }
            account.debit(amount);
        }

        List<Transaction> lastTx = account.getLastNTransactions(1);
        Transaction tx = lastTx.isEmpty() ? null : lastTx.get(0);

        if (tx != null) {
            repository.saveTransaction(tx);
        }

        repository.saveAccount(account);

        return tx;
    }

    public void reverseTransaction(String transactionId, Account account) {
        Transaction original = repository.getAllTransactions().stream()
                .filter(t -> t.getTransactionId().equals(transactionId))
                .findFirst()
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

        repository.saveTransaction(reversal);
        repository.saveAccount(account);
    }

    public boolean validateDailyLimit(Account account, double amount) {
        double dailyLimit = 10000.0;

        double todaysDebits = repository.getAllTransactions().stream()
                .filter(Transaction::isDebit)
                .mapToDouble(Transaction::getAmount)
                .sum();
        
        return todaysDebits + amount <= dailyLimit;
    }
}
