package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.ProductRepository;
import hu.uni.eku.tzs.dao.entity.ProductEntity;
import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;
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
class ProductManagerImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductManagerImpl service;

    @Test
    void recordProductHappyPath() throws ProductAlreadyExistsException {
        // given
        Product product = TestDataProvider.getHoverboard();
        ProductEntity productEntity = TestDataProvider.getHoverboardEntity();
        when(productRepository.findById(any())).thenReturn(Optional.empty());
        when(productRepository.save(any())).thenReturn(productEntity);
        // when
        Product actual = service.record(product);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(product);
    }

    @Test
    void recordProductAlreadyExistst() throws ProductAlreadyExistsException {
        // given
        Product product = TestDataProvider.getHoverboard();
        ProductEntity productEntity = TestDataProvider.getHoverboardEntity();
        when(productRepository.findById(TestDataProvider.hoverboardId))
                .thenReturn(Optional.ofNullable(productEntity));
        // when then
        assertThatThrownBy(() -> service.record(product))
                .isInstanceOf(ProductAlreadyExistsException.class);
    }

    @Test
    void readByIdHappyPath() throws ProductNotFoundException {
        // given
        Product expected = TestDataProvider.getHoverboard();
        when(productRepository.findById(TestDataProvider.hoverboardId))
                .thenReturn(Optional.of(TestDataProvider.getHoverboardEntity()));
        // when
        Product actual = service.readById(TestDataProvider.hoverboardId);
        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void readByIdProductNotFoundException() throws ProductNotFoundException {
        // given
        when(productRepository.findById(TestDataProvider.unknownId))
                .thenReturn(Optional.empty());
        // when then
        assertThatThrownBy(() -> service.readById(TestDataProvider.unknownId))
                .isInstanceOf(ProductNotFoundException.class)
                .hasMessageContaining(String.valueOf(TestDataProvider.unknownId));
    }

    @Test
    void readAllHappyPath() {
        // given
        List<ProductEntity> productEntities = List.of(
                TestDataProvider.getHoverboardEntity(),
                TestDataProvider.getPortalGunEntity()
        );

        Collection<Product> expectedProducts = List.of(
                TestDataProvider.getHoverboard(),
                TestDataProvider.getPortalGun()
        );
        when(productRepository.findAll()).thenReturn(productEntities);
        // when
        Collection<Product> actualProducts = service.readAll();
        // then
        assertThat(actualProducts)
                .usingRecursiveComparison()
                .isEqualTo(expectedProducts);
    }

    @Test
    void modifyProductHappyPath() {
        // given
        Product product = TestDataProvider.getHoverboard();
        ProductEntity productEntity = TestDataProvider.getHoverboardEntity();
        when(productRepository.save(productEntity)).thenReturn(productEntity);
        // when
        Product actual = service.modify(product);
        // then
        assertThat(actual).usingRecursiveComparison()
                .isEqualTo(product);
    }

    private static class TestDataProvider {
        public static final int hoverboardId = 1;

        public static final int portalGunId = 2;

        public static final int unknownId = -1;

        public static Product getHoverboard() {
            return new Product(hoverboardId, "Hoverboard", 99);
        }

        public static ProductEntity getHoverboardEntity() {
            return ProductEntity.builder()
                    .id(hoverboardId)
                    .name("Hoverboard")
                    .price(99)
                    .build();
        }

        public static Product getPortalGun() {
            return new Product(portalGunId, "Portal Gun", 9999);
        }

        public static ProductEntity getPortalGunEntity() {
            return ProductEntity.builder()
                    .id(portalGunId)
                    .name("Portal Gun")
                    .price(9999)
                    .build();
        }
    }

}