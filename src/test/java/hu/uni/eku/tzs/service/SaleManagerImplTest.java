package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.CustomerRepository;
import hu.uni.eku.tzs.dao.EmployeeRepository;
import hu.uni.eku.tzs.dao.ProductRepository;
import hu.uni.eku.tzs.dao.SaleRepository;
import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import hu.uni.eku.tzs.dao.entity.EmployeeEntity;
import hu.uni.eku.tzs.dao.entity.ProductEntity;
import hu.uni.eku.tzs.dao.entity.SaleEntity;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaleManagerImplTest {

    @Mock
    SaleRepository saleRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    CustomerRepository customerRepository;

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    SaleManagerImpl service;

    @Test
    void recordSaleHappyPath() throws SaleAlreadyExistsException {
        // given
        Sale sale1 = TestDataProvider.getSale1();
        SaleEntity sale1Entity = TestDataProvider.getSale1Entity();
        when(saleRepository.findById(any())).thenReturn(Optional.empty());
        when(saleRepository.save(any())).thenReturn(sale1Entity);
        // when
        Sale actual = service.record(sale1);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(sale1);
    }

    @Test
    void recordSaleAlreadyExistsException() throws SaleAlreadyExistsException {
        // given
        Sale sale1 = TestDataProvider.getSale1();
        SaleEntity sale1Entity = TestDataProvider.getSale1Entity();
        when(saleRepository.findById(sale1.getId())).thenReturn(Optional.ofNullable(sale1Entity));
        // when
        assertThatThrownBy(() -> service.record(sale1))
                .isInstanceOf(SaleAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws SaleNotFoundException {
        // given
        Sale expected = TestDataProvider.getSale1();
        SaleEntity saleEntity = TestDataProvider.getSale1Entity();
        when(saleRepository.findById(expected.getId())).thenReturn(Optional.ofNullable(saleEntity));
        // when
        Sale actual = service.readById(expected.getId());
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdSaleNotFoundException() throws SaleNotFoundException {
        // given
        when(saleRepository.findById(TestDataProvider.unknownId)).thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.readById(TestDataProvider.unknownId))
                .isInstanceOf(SaleNotFoundException.class)
                .hasMessageContaining(String.valueOf(TestDataProvider.unknownId));
    }

    @Test
    void readAllHappyPath() {
        // given
        List<SaleEntity> saleEntities = List.of(
                TestDataProvider.getSale1Entity(),
                TestDataProvider.getSale2Entity()
        );

        Collection<Sale> expectedSales = List.of(
                TestDataProvider.getSale1(),
                TestDataProvider.getSale2()
        );
        when(saleRepository.findAll()).thenReturn(saleEntities);
        // when
        Collection<Sale> actualSales = service.readAll();
        // then
        assertThat(actualSales)
                .usingRecursiveComparison()
                .isEqualTo(expectedSales);
    }

    @Test
    void modifySaleHappyPath() {
        // given
        Sale sale = TestDataProvider.getSale1();
        SaleEntity saleEntity = TestDataProvider.getSale1Entity();
        when(saleRepository.save(saleEntity)).thenReturn(saleEntity);
        // when
        Sale actual = service.modify(sale);
        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(sale);
    }

    private static class TestDataProvider {
        private static final int unknownId = -1;

        public static Employee getJaneDoeModel() {
            return new Employee(1, "Jane", "x", "Doe");
        }

        public static EmployeeEntity getJaneDoeEntity() {
            return EmployeeEntity.builder()
                    .id(1)
                    .firstName("Jane")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Customer getJohnDoeModel() {
            return new Customer(1, "John", "x", "Doe");
        }

        public static CustomerEntity getJohnDoeEntity() {
            return CustomerEntity.builder()
                    .id(1)
                    .firstName("John")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Product getHoverboardModel() {
            return new Product(1, "Hoverboard", 99);
        }

        public static ProductEntity getHoverboardEntity() {
            return ProductEntity.builder()
                    .id(1)
                    .name("Hoverboard")
                    .price(99)
                    .build();
        }

        public static Sale getSale1() {
            return new Sale(1, getJaneDoeModel(), getJohnDoeModel(), getHoverboardModel(), 3);
        }

        public static SaleEntity getSale1Entity() {
            return SaleEntity.builder()
                    .id(1)
                    .salesPerson(getJaneDoeEntity())
                    .customer(getJohnDoeEntity())
                    .product(getHoverboardEntity())
                    .quantity(3)
                    .build();
        }

        public static Sale getSale2() {
            return new Sale(2, getJaneDoeModel(), getJohnDoeModel(), getHoverboardModel(), 5);
        }

        public static SaleEntity getSale2Entity() {
            return SaleEntity.builder()
                    .id(2)
                    .salesPerson(getJaneDoeEntity())
                    .customer(getJohnDoeEntity())
                    .product(getHoverboardEntity())
                    .quantity(5)
                    .build();
        }
    }
}