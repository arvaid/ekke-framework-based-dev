package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.ProductDto;
import hu.uni.eku.tzs.controller.dto.ProductMapper;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.ProductManager;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
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
class ProductControllerTest {

    @Mock
    private ProductManager productManager;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductController controller;

    @Test
    void readAllHappyPath() {
        // given
        when(productManager.readAll()).thenReturn(List.of(TestDataProvider.getHoverboard()));
        when(productMapper.product2productDto(any())).thenReturn(TestDataProvider.getHoverboardDto());
        Collection<ProductDto> expected = List.of(TestDataProvider.getHoverboardDto());
        // when
        Collection<ProductDto> actual = controller.readAllProducts();
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void createProductHappyPath() throws ProductAlreadyExistsException {
        // given
        Product hoverboard = TestDataProvider.getHoverboard();
        ProductDto hoverboardDto = TestDataProvider.getHoverboardDto();
        when(productMapper.productDto2product(hoverboardDto)).thenReturn(hoverboard);
        when(productManager.record(hoverboard)).thenReturn(hoverboard);
        when(productMapper.product2productDto(hoverboard)).thenReturn(hoverboardDto);
        // when
        ProductDto actual = controller.create(hoverboardDto);
        // then
        Assertions.assertThat(actual).usingRecursiveComparison().isEqualTo(hoverboardDto);
    }

    @Test
    void createProductThrowsProductAlreadyExistsException() throws ProductAlreadyExistsException {
        // given
        Product hoverboard = TestDataProvider.getHoverboard();
        ProductDto hoverboardDto = TestDataProvider.getHoverboardDto();
        when(productMapper.productDto2product(hoverboardDto)).thenReturn(hoverboard);
        when(productManager.record(hoverboard)).thenThrow(new ProductAlreadyExistsException());
        // when then
        assertThatThrownBy(() -> controller.create(hoverboardDto)).isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void deleteFromQueryParamHappyPath() throws ProductNotFoundException {
        // given
        Product hoverboard = TestDataProvider.getHoverboard();
        when(productManager.readById(TestDataProvider.hoverboardId)).thenReturn(hoverboard);
        doNothing().when(productManager).delete(hoverboard);
        // when
        controller.delete(TestDataProvider.hoverboardId);
        // then is not necessary, mock are checked by default
    }

    @Test
    void deleteFromQueryParamWhenProductNotFound() throws ProductNotFoundException {
        // given
        final int notFoundSaleId = TestDataProvider.unknownId;
        doThrow(new ProductNotFoundException(String.format("Cannot find sale with ID %d", notFoundSaleId)))
                .when(productManager).readById(notFoundSaleId);
        // when then
        assertThatThrownBy(() -> controller.delete(notFoundSaleId))
                .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {
        public static final int hoverboardId = 1;

        public static final int portalGunId = 2;

        public static final int unknownId = -1;

        public static Product getHoverboard() {
            return new Product(hoverboardId, "Hoverboard", 99);
        }

        public static ProductDto getHoverboardDto() {
            return ProductDto.builder()
                    .id(hoverboardId)
                    .name("Hoverboard")
                    .price(99)
                    .build();
        }
    }
}