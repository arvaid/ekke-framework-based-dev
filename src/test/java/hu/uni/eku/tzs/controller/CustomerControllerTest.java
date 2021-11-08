package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.CustomerDto;
import hu.uni.eku.tzs.controller.dto.CustomerMapper;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.CustomerManager;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerManager customerManager;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(customerManager.readAll()).thenReturn(List.of(TestDataProvider.getJohnDoe()));
        when(customerMapper.customer2customerDto(any())).thenReturn(TestDataProvider.getJohnDoeDto());
        Collection<CustomerDto> expected = List.of(TestDataProvider.getJohnDoeDto());
        // when
        Collection<CustomerDto> actual = controller.readAllCustomers();
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createCustomerHappyPath() throws CustomerAlreadyExistsException {
        // given
        Customer johnDoe = CustomerControllerTest.TestDataProvider.getJohnDoe();
        CustomerDto johnDoeDto = CustomerControllerTest.TestDataProvider.getJohnDoeDto();
        when(customerMapper.customerDto2customer(johnDoeDto)).thenReturn(johnDoe);
        when(customerManager.record(johnDoe)).thenReturn(johnDoe);
        when(customerMapper.customer2customerDto(johnDoe)).thenReturn(johnDoeDto);
        // when
        CustomerDto actual = controller.create(johnDoeDto);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(johnDoeDto);
    }

    @Test
    void createCustomerThrowsCustomerAlreadyExistsException() throws CustomerAlreadyExistsException {
        // given
        Customer johnDoe = CustomerControllerTest.TestDataProvider.getJohnDoe();
        CustomerDto johnDoeDto = CustomerControllerTest.TestDataProvider.getJohnDoeDto();
        when(customerMapper.customerDto2customer(johnDoeDto)).thenReturn(johnDoe);
        when(customerManager.record(johnDoe)).thenThrow(new CustomerAlreadyExistsException());
        // when then
        assertThatThrownBy(() -> controller.create(johnDoeDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws CustomerNotFoundException {
        // given
        Customer johnDoe = CustomerControllerTest.TestDataProvider.getJohnDoe();
        when(customerManager.readById(TestDataProvider.johnDoeId)).thenReturn(johnDoe);
        doNothing().when(customerManager).delete(johnDoe);
        // when
        controller.delete(TestDataProvider.johnDoeId);
        // then is not necessary, mock are checked by default
    }

    @Test
    void deleteFromQueryParamWhenCustomerNotFound() throws CustomerNotFoundException {
        // given
        final int notFoundSaleId = TestDataProvider.unknownId;
        doThrow(new CustomerNotFoundException(String.format("Cannot find sale with ID %d", notFoundSaleId)))
                .when(customerManager).readById(notFoundSaleId);
        // when then
        assertThatThrownBy(() -> controller.delete(notFoundSaleId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final int johnDoeId = 1;
        public static final int janeDoeId = 2;

        public static final int unknownId = -1;

        public static Customer getJohnDoe() {
            return new Customer(johnDoeId, "John", "x", "Doe");
        }

        public static CustomerDto getJohnDoeDto() {
            return CustomerDto.builder()
                    .id(johnDoeId)
                    .firstName("John")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Customer getJaneDoe() {
            return new Customer(janeDoeId, "Jane", "x", "Doe");
        }

        public static CustomerDto getJaneDoeDto() {
            return CustomerDto.builder()
                    .id(johnDoeId)
                    .firstName("Jane")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }
    }
}