package com.bank.app.repository;

import com.bank.app.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    void save(Customer customer);
    Optional<Customer> findById(String customerId);
    List<Customer> findAll();
    void deleteById(String customerId);
	void update(Customer customer);
}
/*
 package com.bank.app.repository;

import com.bank.app.model.Customer;
import java.util.List;
import java.util.Optional;

public interface CustomerDao {

    void save(Customer customer);           // create
    void update(Customer customer);         // update
    Optional<Customer> findById(String customerId);
    List<Customer> findAll();
}

 
 */