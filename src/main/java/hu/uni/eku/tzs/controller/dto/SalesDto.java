package hu.uni.eku.tzs.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesDto {

    private int id;

    @Valid
    private EmployeeDto salesPerson;

    @Valid
    private CustomerDto customer;

    @Valid
    private ProductDto product;

    private double quantity;

}
