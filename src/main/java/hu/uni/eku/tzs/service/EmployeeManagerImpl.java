package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.EmployeeRepository;
import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.service.exceptions.EmployeeAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.EmployeeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeManagerImpl implements EmployeeManager {

    private final EmployeeRepository employeeRepository;

    private static Employee convertEmployeeEntity2Model(EmployeeEntity employeeEntity) {
        return new Employee(
                employeeEntity.getId(),
                employeeEntity.getFirstName(),
                employeeEntity.getMiddleInitial(),
                employeeEntity.getLastName()
        );
    }

    private static EmployeeEntity convertEmployeeModel2Entity(Employee employee) {
        return EmployeeEntity.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .middleInitial(employee.getMiddleInitial())
                .lastName(employee.getLastName())
                .build();
    }

    @Override
    public Employee record(Employee employee) throws EmployeeAlreadyExistsException {
        if (employeeRepository.findById(employee.getId()).isPresent()) {
            throw new EmployeeAlreadyExistsException();
        }
        EmployeeEntity employeeEntity = employeeRepository.save(
                EmployeeEntity.builder()
                        .id(employee.getId())
                        .firstName(employee.getFirstName())
                        .middleInitial(employee.getMiddleInitial())
                        .lastName(employee.getLastName())
                        .build()
        );
        return convertEmployeeEntity2Model(employeeEntity);
    }

    @Override
    public Employee readById(int id) throws EmployeeNotFoundException {
        Optional<EmployeeEntity> entity = employeeRepository.findById(id);
        if (entity.isEmpty()) {
            throw new EmployeeNotFoundException(String.format("Cannot find employee with ID %d", id));
        }
        return convertEmployeeEntity2Model(entity.get());
    }

    @Override
    public Collection<Employee> readAll() {
        return employeeRepository.findAll().stream().map(EmployeeManagerImpl::convertEmployeeEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Employee modify(Employee employee) {
        EmployeeEntity entity = convertEmployeeModel2Entity(employee);
        return convertEmployeeEntity2Model(employeeRepository.save(entity));
    }

    @Override
    public void delete(Employee employee) {
        employeeRepository.delete(convertEmployeeModel2Entity(employee));
    }
}
