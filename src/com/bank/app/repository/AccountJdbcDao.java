package com.bank.app.repository;

import com.bank.app.enums.AccountTypes;
import com.bank.app.enums.AccountStatus;
import com.bank.app.enums.AccountTypes;
import com.bank.app.model.Account;
import com.bank.app.model.SavingsAccount;
import com.bank.app.util.DBUtil;
import com.bank.app.model.CurrentAccount;
import com.bank.app.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountJdbcDao implements AccountDao {

    @Override
    public void save(Account account, String customerId) {
        String sql = """
        			INSERT INTO accounts
        			(account_number, customer_id, account_type, status, balance, minimum_balance, interest_rate, overdraft_limit)
        			VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        		
        		""";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, account.getAccountNumber());
            ps.setString(2, customerId);
            ps.setString(3, account.getAccountType().name());
            ps.setString(4, account.getStatus().name());
            ps.setDouble(5, account.getBalance());
            ps.setDouble(6, account.getMinBalance());

            if (account instanceof SavingsAccount sa) {
                ps.setDouble(7, sa.getInterestRate());
                ps.setNull(8, Types.DOUBLE);
            } else if (account instanceof CurrentAccount ca) {
                ps.setNull(7, Types.DOUBLE);
                ps.setDouble(8, ca.getOverDraftLimit());
            } else {
                ps.setNull(7, Types.DOUBLE);
                ps.setNull(8, Types.DOUBLE);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving account", e);
        }
    }

    
    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToAccount(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding account", e);
        }
    }

    @Override
    public List<Account> findByCustomerId(String customerId) {
        String sql = "SELECT * FROM accounts WHERE customer_id = ?";
        List<Account> result = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing accounts for customer", e);
        }
        return result;
    }

    @Override
    public List<Account> findTopByBalance(int limit) {
        String sql = "SELECT * FROM accounts ORDER BY balance DESC LIMIT ?";
        List<Account> result = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing top accounts", e);
        }
        return result;
    }

    @Override
    public void update(Account account) {
    	String sql = """
    			UPDATE accounts
    			SET status = ?, balance = ?
    			WHERE account_number = ?
    			""";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

        	ps.setString(1, account.getStatus().name());
        	ps.setDouble(2, account.getBalance());
        	ps.setString(3, account.getAccountNumber());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating account", e);
        }
    }

    private Account mapRowToAccount(ResultSet rs) throws SQLException {

        String accNum = rs.getString("account_number");
        AccountTypes type = AccountTypes.valueOf(rs.getString("account_type"));
        AccountStatus status = AccountStatus.valueOf(rs.getString("status"));

        double balance = rs.getDouble("balance");
        double minBal  = rs.getDouble("minimum_balance");

        Account account;

        if (type == AccountTypes.SAVINGS) {

            double rate = rs.getDouble("interest_rate");

            SavingsAccount sa = new SavingsAccount(
                    accNum,
                    minBal,
                    rate,
                    status
            );

            sa.credit(balance);   
            account = sa;

        } else {

            double limit = rs.getDouble("overdraft_limit");
            CurrentAccount ca = new CurrentAccount(
                    accNum,
                    minBal,
                    limit
            );

            ca.credit(balance);   
            if (status == AccountStatus.FROZEN || status == AccountStatus.CLOSED) {
                ca.freezeAccount();
            }

            account = ca;
        }

        return account;
    }


    @Override
    public List<Account> findAll() {
        String sql = "SELECT * FROM accounts";
        List<Account> result = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRowToAccount(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing accounts", e);
        }

        return result;
    }

}
