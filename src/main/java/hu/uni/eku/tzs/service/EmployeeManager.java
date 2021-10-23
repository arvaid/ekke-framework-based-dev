package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;

import java.util.Collection;

public interface EmployeeManager {
    Employee record(Employee employee) throws EmployeeAlreadyExistsException;

    Employee readById(int id) throws EmployeeNotFoundException;

    Collection<Employee> readAll();

    Employee modify(Employee employee);

    void delete(Employee employee);
}
