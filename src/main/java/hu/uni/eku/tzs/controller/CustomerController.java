package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.CustomerDto;
import hu.uni.eku.tzs.controller.dto.CustomerMapper;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.CustomerManager;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;

@Api(tags = "Customers")
@RequestMapping("/customers")
@RestController
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerManager customerManager;

    private final CustomerMapper customerMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {"/", ""})
    public Collection<CustomerDto> readAllCustomers() {
        return customerManager.readAll()
                .stream()
                .map(customerMapper::customer2customerDto)
                .collect(Collectors.toList());
    }

    @ApiOperation("Record")
    @PostMapping(value = {"", "/"})
    public CustomerDto create(@Valid @RequestBody CustomerDto recordRequestDto) {
        Customer customer = customerMapper.customerDto2customer(recordRequestDto);
        try {
            Customer recordedCustomer = customerManager.record(customer);
            return customerMapper.customer2customerDto(recordedCustomer);
        } catch (CustomerAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {"", "/"})
    public CustomerDto update(@Valid @RequestBody CustomerDto updateRequestDto) {
        Customer customer = customerMapper.customerDto2customer(updateRequestDto);
        Customer updatedCustomer = customerManager.modify(customer);
        return customerMapper.customer2customerDto(updatedCustomer);
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"", "/"})
    public void delete(@RequestParam int id) {
        try {
            customerManager.delete(customerManager.readById(id));
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"/{id}"})
    public void deleteBasedOnPath(@PathVariable int id) {
        this.delete(id);
    }
}
