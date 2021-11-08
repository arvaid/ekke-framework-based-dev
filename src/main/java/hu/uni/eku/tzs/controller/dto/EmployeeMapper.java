package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Employee;

public interface EmployeeMapper {
    EmployeeDto employee2employeeDto(Employee employee);

    Employee employeeDto2employee(EmployeeDto dto);
}
