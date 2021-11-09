package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.*;
import hu.uni.eku.tzs.model.Customer;
import hu.uni.eku.tzs.model.Employee;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.SaleManager;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaleControllerTest {

    @Mock
    private SaleManager saleManager;

    @Mock
    private SaleMapper saleMapper;

    @InjectMocks
    private SaleController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(saleManager.readAll()).thenReturn(List.of(TestDataProvider.getSale1()));
        when(saleMapper.sale2saleDto(any())).thenReturn(TestDataProvider.getSale1Dto());
        Collection<SaleDto> expected = List.of(TestDataProvider.getSale1Dto());
        // when
        Collection<SaleDto> actual = controller.readAllSales();
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createSaleHappyPath() throws SaleAlreadyExistsException {
        // given
        Sale sale1 = TestDataProvider.getSale1();
        SaleDto sale1Dto = TestDataProvider.getSale1Dto();
        when(saleMapper.saleDto2sale(sale1Dto)).thenReturn(sale1);
        when(saleManager.record(sale1)).thenReturn(sale1);
        when(saleMapper.sale2saleDto(sale1)).thenReturn(sale1Dto);
        // when
        SaleDto actual = controller.create(sale1Dto);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(sale1Dto);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws SaleNotFoundException {
        // given
        Sale sale1 = TestDataProvider.getSale1();
        when(saleManager.readById(sale1.getId())).thenReturn(sale1);
        doNothing().when(saleManager).delete(sale1);
        // when
        controller.delete(sale1.getId());
        // then is not necessary, mock are checked by default
    }

    @Test
    void deleteFromQueryParamWhenSaleNotFound() throws SaleNotFoundException {
        // given
        final int notFoundSaleId = TestDataProvider.unknownId;
        doThrow(new SaleNotFoundException(String.format("Cannot find employee with IDd %d", notFoundSaleId)))
                .when(saleManager).readById(notFoundSaleId);
        // when then
        assertThatThrownBy(() -> controller.delete(notFoundSaleId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        private static final int unknownId = -1;

        public static Employee getJaneDoeModel() {
            return new Employee(1, "Jane", "x", "Doe");
        }

        public static EmployeeDto getJaneDoeDto() {
            return EmployeeDto.builder()
                    .id(1)
                    .firstName("Jane")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Customer getJohnDoeModel() {
            return new Customer(1, "John", "x", "Doe");
        }

        public static CustomerDto getJohnDoeDto() {
            return CustomerDto.builder()
                    .id(1)
                    .firstName("John")
                    .middleInitial("x")
                    .lastName("Doe")
                    .build();
        }

        public static Product getHoverboardModel() {
            return new Product(1, "Hoverboard", 99);
        }

        public static ProductDto getHoverboardDto() {
            return ProductDto.builder()
                    .id(1)
                    .name("Hoverboard")
                    .price(99)
                    .build();
        }

        public static Sale getSale1() {
            return new Sale(1, getJaneDoeModel(), getJohnDoeModel(), getHoverboardModel(), 3);
        }

        public static SaleDto getSale1Dto() {
            return SaleDto.builder()
                    .id(1)
                    .salesPerson(getJaneDoeDto())
                    .customer(getJohnDoeDto())
                    .product(getHoverboardDto())
                    .quantity(3)
                    .build();
        }

        public static Sale getSale2() {
            return new Sale(2, getJaneDoeModel(), getJohnDoeModel(), getHoverboardModel(), 5);
        }

        public static SaleDto getSale2Dto() {
            return SaleDto.builder()
                    .id(2)
                    .salesPerson(getJaneDoeDto())
                    .customer(getJohnDoeDto())
                    .product(getHoverboardDto())
                    .quantity(5)
                    .build();
        }
    }

}