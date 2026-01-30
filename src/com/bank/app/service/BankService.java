package com.bank.app.service;

import java.util.List;
import java.util.Optional;

import com.bank.app.dto.AccountStatusChangeRequest;
import com.bank.app.dto.CreateAccountRequest;
import com.bank.app.dto.TransactionRequest;
import com.bank.app.dto.TransferRequest;
import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.TransactionType;
import com.bank.app.model.Account;
import com.bank.app.model.CurrentAccount;
import com.bank.app.model.Customer;
import com.bank.app.model.SavingsAccount;
import com.bank.app.repository.AccountDao;
import com.bank.app.repository.CustomerDao;
import com.bank.app.repository.TransactionDao;
import com.bank.app.util.GenerateId;

public class BankService {

    private final CustomerDao customerDao;
    private final AccountDao accountDao;
//    private final TransactionDao transactionDao;
    private final TransactionService transactionService;

    public BankService(CustomerDao customerDao,
                       AccountDao accountDao,
                       TransactionDao transactionDao,
                       TransactionService transactionService) {
        this.customerDao = customerDao;
        this.accountDao = accountDao;
//        this.transactionDao = transactionDao;
        this.transactionService = transactionService;
    }

    // <I> Creating Customers
    public Customer createCustomer(String name, String pan) {
        String customerId = GenerateId.generateCustomerId();
        Customer customer = new Customer(customerId, name, pan);

        // persist customer
        customerDao.save(customer);

        return customer;
    }

    // <II> Create Account => CreateAccountRequest
    public Account createAccount(CreateAccountRequest request) {

        Optional<Customer> optionalCustomer = customerDao.findById(request.getCustomerId());
        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Customer Not Found " + request.getCustomerId());
        }

        Customer customer = optionalCustomer.get();

        String accountNumber = GenerateId.generateAccountId();
        Account account;

        if (request.getAccountType() == AccountTypes.SAVINGS) {
            account = new SavingsAccount(
                    accountNumber,
                    request.getMinimumBalance(),
                    request.getInterestRate(), null
            );
        } else if (request.getAccountType() == AccountTypes.CURRENT) {
            account = new CurrentAccount(
                    accountNumber,
                    request.getMinimumBalance(),
                    request.getOverdraftLimit()
            );
        } else {
            throw new IllegalArgumentException("Unsupported Account Type: " + request.getAccountType());
        }

        // initial deposit if any
        if (request.getInitialDiposit() > 0) {
            account.credit(request.getInitialDiposit());
        }

        // attach to customer object in memory (optional, depending on your design)
        customer.addAccount(account);

        // persist account & update customer if needed
        accountDao.save(account, customer.getCustomerId());
        customerDao.update(customer);

        return account;
    }

    // (III) Changing Account Status..!!
    public void changeAccountStatus(AccountStatusChangeRequest request) {

        Account account = accountDao.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() ->
                        new IllegalArgumentException("Account Not Found: " + request.getAccountNumber())
                );

        AccountStatus newStatus = request.getAccountStatus();
        switch (newStatus) {
            case ACTIVE -> account.unFreezeAccount();
            case FROZEN -> account.freezeAccount();
            case CLOSED -> account.freezeAccount();
        }

        accountDao.update(account);
    }

    // (IV) Transfer Funds..!!
    public void transferFunds(TransferRequest request) {

        Account from = accountDao.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "From account not found: " + request.getFromAccountNumber())
                );

        Account to = accountDao.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException(
                        "To account not found: " + request.getToAccountNumber())
                );

        double amount = request.getAmount();

        if (!from.canDebit(amount)) {
            System.out.println("Transfer failed: insufficient funds.");
            return;
        }

        TransactionRequest debitReq = new TransactionRequest(
                from.getAccountNumber(),
                TransactionType.DEBIT,
                amount
        );

        TransactionRequest creditReq = new TransactionRequest(
                to.getAccountNumber(),
                TransactionType.CREDIT,
                amount
        );

        transactionService.processTransaction(debitReq, from);
        transactionService.processTransaction(creditReq, to);

        // persist updated accounts after transfer
        accountDao.update(from);
        accountDao.update(to);
    }

    // (V) Applying Monthly Rules and Policies!!
    public void applyMonthlyProcess() {
        List<Account> accounts = accountDao.findAll(); // new DAO method
        for (Account account : accounts) {
            account.applyMonthlyRules();
            accountDao.update(account);
        }
    }

    public Optional<Account> findAccountByNumber(String accountNumber) {
        return accountDao.findByAccountNumber(accountNumber);
    }

}
