package com.bank.app.repository;

import com.bank.app.enums.TransactionType;
import com.bank.app.model.Transaction;
import com.bank.app.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionJdbcDao implements TransactionDao {

    @Override
    public void save(Transaction tx, String accountNumber) {
        String sql = "INSERT INTO transactions " +
                     "(transaction_id, account_number, transaction_type, amount, timestamp, " +
                     " success, failure_reason, related_transaction_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tx.getTransactionId());
            ps.setString(2, accountNumber);
            ps.setString(3, tx.getTransactionType().name());
            ps.setDouble(4, tx.getAmount());
            ps.setTimestamp(5, Timestamp.valueOf(tx.getTimestamp()));
            ps.setBoolean(6, tx.isSuccess());
            ps.setString(7, tx.getFailureReason());
            ps.setString(8, tx.getRelatedTransactionId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving transaction", e);
        }
    }

    @Override
    public List<Transaction> findByAccount(String accountNumber) {
        String sql = "SELECT * FROM transactions WHERE account_number = ? ORDER BY timestamp DESC";
        List<Transaction> result = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing transactions", e);
        }
        return result;
    }

    @Override
    public List<Transaction> findFailedTransactions() {
        String sql = "SELECT * FROM transactions WHERE success = false";
        List<Transaction> result = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing failed transactions", e);
        }
        return result;
    }

    @Override
    public Optional<Transaction> findById(String transactionId) {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(mapRowToTransaction(rs));
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding transaction", e);
        }
    }

    private Transaction mapRowToTransaction(ResultSet rs) throws SQLException {
        Transaction tx = new Transaction(null, null, 0, null, false, null, null);
        tx.setTransactionId(rs.getString("transaction_id"));
        tx.setTransactionType(TransactionType.valueOf(rs.getString("transaction_type")));
        tx.setAmount(rs.getDouble("amount"));
        Timestamp ts = rs.getTimestamp("timestamp");
        tx.setTimestamp(ts.toLocalDateTime());
        tx.setSuccess(rs.getBoolean("success"));
        tx.setFailureReason(rs.getString("failure_reason"));
        tx.setRelatedTransactionId(rs.getString("related_transaction_id"));
        return tx;
    }

    @Override
    public List<Transaction> findAll() {
        String sql = "SELECT * FROM transactions";
        List<Transaction> result = new ArrayList<>();

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapRowToTransaction(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing transactions", e);
        }

        return result;
    }

}

