package hu.uni.eku.tzs.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {

    private int id;

    @Valid
    private EmployeeDto salesPerson;

    @Valid
    private CustomerDto customer;

    @Valid
    private ProductDto product;

    @Min(value = 0, message = "the quantity must be positive")
    private double quantity;

}
