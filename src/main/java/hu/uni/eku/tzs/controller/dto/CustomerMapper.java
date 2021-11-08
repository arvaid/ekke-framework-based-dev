package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Customer;

public interface CustomerMapper {

    CustomerDto customer2customerDto(Customer customer);

    Customer customerDto2customer(CustomerDto dto);
}
