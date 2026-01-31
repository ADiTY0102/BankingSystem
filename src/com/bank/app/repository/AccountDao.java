package com.bank.app.repository;

import com.bank.app.model.Account;
import java.util.List;
import java.util.Optional;

public interface AccountDao {
    void save(Account account, String customerId);
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(String customerId);
    List<Account> findTopByBalance(int limit);
    void update(Account account);
	List<Account> findAll();
}
