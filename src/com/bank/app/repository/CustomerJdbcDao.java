package com.bank.app.repository;

import com.bank.app.model.Customer;
import com.bank.app.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerJdbcDao implements CustomerDao {

    @Override
    public void save(Customer customer) {
        String sql = "INSERT INTO customers (customer_id, name, pan) VALUES (?, ?, ?)";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getCustomerId());
            ps.setString(2, customer.getName());
            ps.setString(3, customer.getPan());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving customer", e);
        }
    }

    
    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, pan = ? WHERE customer_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customer.getName());
            ps.setString(2, customer.getPan());
            ps.setString(3, customer.getCustomerId());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating customer: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    
    
    
    @Override
    public Optional<Customer> findById(String customerId) {
        String sql = "SELECT customer_id, name, pan FROM customers WHERE customer_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Customer c = new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("pan")
                );
                return Optional.of(c);
            }
            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error finding customer", e);
        }
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT customer_id, name, pan FROM customers";
        List<Customer> result = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer c = new Customer(
                    rs.getString("customer_id"),
                    rs.getString("name"),
                    rs.getString("pan")
                );
                result.add(c);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing customers", e);
        }
        return result;
    }

    @Override
    public void deleteById(String customerId) {
        String sql = "DELETE FROM customers WHERE customer_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, customerId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting customer", e);
        }
    }
}
