package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;

import java.util.Collection;

public interface CustomerManager {
    Customer record(Customer customer) throws CustomerAlreadyExistsException;

    Customer readById(int id) throws CustomerNotFoundException;

    Collection<Customer> readAll();

    Customer modify(Customer customer);

    void delete(Customer customer);
}