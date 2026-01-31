package com.bank.app.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.TransactionType;

public abstract class Account {
	protected String accountNumber;
	protected AccountTypes accountType;
	protected AccountStatus status;
	protected double balance;
	protected double minimumBalance;
	protected List<Transaction> transactions = new ArrayList<>();
	
	protected Account(String accountNumber, AccountTypes accountType, double minimumBalance) {
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.minimumBalance = minimumBalance;
		this.status = AccountStatus.ACTIVE;
		this.balance = 0.00;
	}
	
	
    public void credit(double amount) {
        if (status != AccountStatus.ACTIVE) {
            return;
        }
        balance += amount;
        Transaction t = Transaction.success(TransactionType.CREDIT, amount);
        transactions.add(t);
    }

    public boolean canDebit(double amount) {
        return balance - amount >= minimumBalance;
    }

    public void debit(double amount) {
        if (status != AccountStatus.ACTIVE) {
            return;
        }
        if (!canDebit(amount)) {
            Transaction t = Transaction.failure(
                    TransactionType.DEBIT,
                    amount,
                    "Insufficient balance"
            );
            transactions.add(t);
            return;
        }
        balance -= amount;
        Transaction t = Transaction.success(TransactionType.DEBIT, amount);
        transactions.add(t);
    }	
	public void freezeAccount() {
		this.status = AccountStatus.FROZEN;
	}
	
	public void unFreezeAccount() {
		this.status = AccountStatus.ACTIVE;
	}
	
	public double getActiveBalance() {
		return balance;
	}
	
	public List <Transaction> getLastNTransactions(int count){
		int size = transactions.size();
		if(count >= size) {
			return Collections.unmodifiableList(transactions);
		}
		return Collections.unmodifiableList(
					transactions.subList(size-count, size)
				);
	}
	
	public abstract void applyMonthlyRules();


	public String getAccountNumber() {
		return accountNumber;
	}


	public AccountTypes getAccountType() {
		return accountType;
	}


	public double getBalance() {
		return balance;
	}

	public AccountStatus getStatus() {
		return status;
	}
	
	public double getMinBalance() {
		return minimumBalance;
	}
}




/*

package com.bank.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountType;
import com.bank.app.enums.TransactionType;

public abstract class Account {

    protected String accountNumber;
    protected AccountType accountType;
    protected AccountStatus status;
    protected double balance;
    protected double minimumBalance;
    protected List<Transaction> transactions = new ArrayList<>();

    protected Account(String accountNumber,
                      AccountType accountType,
                      double minimumBalance) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.minimumBalance = minimumBalance;
        this.status = AccountStatus.ACTIVE;
        this.balance = 0.0;
    }

    public void credit(double amount) {
        if (status != AccountStatus.ACTIVE) {
            return;
        }
        balance += amount;
        Transaction tx = Transaction.success(TransactionType.CREDIT, amount);
        transactions.add(tx);
    }

    public boolean canDebit(double amount) {
        return balance - amount >= minimumBalance;
    }

    public void debit(double amount) {
        if (status != AccountStatus.ACTIVE) {
            return;
        }
        if (!canDebit(amount)) {
            Transaction tx = Transaction.failure(
                    TransactionType.DEBIT,
                    amount,
                    "Insufficient balance"
            );
            transactions.add(tx);
            return;
        }
        balance -= amount;
        Transaction tx = Transaction.success(TransactionType.DEBIT, amount);
        transactions.add(tx);
    }

    public void freezeAccount() {
        this.status = AccountStatus.FROZEN;
    }

    public void unfreezeAccount() {
        this.status = AccountStatus.ACTIVE;
    }

    public double getAvailableBalance() {
        return balance;
    }

    public List<Transaction> getLastNTransactions(int count) {
        int size = transactions.size();
        if (count >= size) {
            return Collections.unmodifiableList(transactions);
        }
        return Collections.unmodifiableList(
                transactions.subList(size - count, size)
        );
    }

    public abstract void applyMonthlyRules();

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public double getBalance() {
        return balance;
    }
}


*/
