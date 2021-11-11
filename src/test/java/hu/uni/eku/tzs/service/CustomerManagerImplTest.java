package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CustomerRepository;
import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.service.exceptions.CustomerAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.CustomerNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerManagerImplTest {

    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerManagerImpl service;

    @Test
    void recordCustomerHappyPath() throws CustomerAlreadyExistsException {
        // given
        Customer customer = TestDataProvider.getJohnDoe();
        CustomerEntity customerEntity = TestDataProvider.getJohnDoeEntity();
        when(customerRepository.findById(any())).thenReturn(Optional.empty());
        when(customerRepository.save(any())).thenReturn(customerEntity);
        // when
        Customer actual = service.record(customer);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(customer);
    }

    @Test
    void recordCustomerAlreadyExistsException() throws CustomerAlreadyExistsException {
        //given
        Customer customer = TestDataProvider.getJohnDoe();
        CustomerEntity customerEntity = TestDataProvider.getJohnDoeEntity();
        when(customerRepository.findById(TestDataProvider.johnDoeId))
                .thenReturn(Optional.ofNullable(customerEntity));
        // when then
        assertThatThrownBy(() -> service.record(customer))
                .isInstanceOf(CustomerAlreadyExistsException.class);

    }

    @Test
    void readByIdHappyPath() throws CustomerNotFoundException {
        // given
        Customer expected = TestDataProvider.getJohnDoe();
        when(customerRepository.findById(TestDataProvider.johnDoeId))
                .thenReturn(Optional.of(TestDataProvider.getJohnDoeEntity()));
        // when
        Customer actual = service.readById(TestDataProvider.johnDoeId);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readyByIdCustomerNotFoundException() throws CustomerNotFoundException {
        // given
        when(customerRepository.findById(TestDataProvider.unknownId))
                .thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.readById(TestDataProvider.unknownId))
                .isInstanceOf(CustomerNotFoundException.class)
                .hasMessageContaining(String.valueOf(TestDataProvider.unknownId));
    }

    @Test
    void readAllHappyPath() {
        // given
        List<CustomerEntity> customerEntities = List.of(
                TestDataProvider.getJohnDoeEntity(),
                TestDataProvider.getJaneDoeEntity()
        );
        Page<CustomerEntity> page = new PageImpl<>(customerEntities);
        Collection<Customer> expectedCustomers = List.of(
                TestDataProvider.getJohnDoe(),
                TestDataProvider.getJaneDoe()
        );
        when(customerRepository.findAll(isA(Pageable.class))).thenReturn(page);
        // when
        Collection<Customer> actualCustomers = service.readAll();
        // then
        assertThat(actualCustomers)
                .usingRecursiveComparison()
                .isEqualTo(expectedCustomers);
    }

    @Test
    void modifyCustomerHappyPath() {
        // given
        Customer customer = TestDataProvider.getJohnDoe();
        CustomerEntity customerEntity = TestDataProvider.getJohnDoeEntity();
        when(customerRepository.save(customerEntity)).thenReturn(customerEntity);
        // when
        Customer actual = service.modify(customer);
        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(customer);
    }


    private static class TestDataProvider {
        public static final int johnDoeId = 1;
        public static final int janeDoeId = 2;

        public static final int unknownId = -1;

        public static Customer getJohnDoe() {
            return new Customer(johnDoeId, "John", "x", "Doe");
        }

        public static CustomerEntity getJohnDoeEntity() {
            return CustomerEntity.builder()
                    .id(johnDoeId)
                    .firstName("John")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Customer getJaneDoe() {
            return new Customer(janeDoeId, "Jane", "x", "Doe");
        }

        public static CustomerEntity getJaneDoeEntity() {
            return CustomerEntity.builder()
                    .id(janeDoeId)
                    .firstName("Jane")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

    }
}