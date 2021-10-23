package hu.uni.eku.tzs.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SaleManagerImpl implements SaleManager {

    private final SaleRepository saleRepository;

    private static SaleEntity convertSaleModel2Entity(Sale sale) {
        return SaleEntity.builder()
                .id(sale.getId())
                .salesPerson(convertEmployeeModel2Entity(sale.getSalesPerson()))
                .customer(convertCustomerModel2Entity(sale.getCustomer()))
                .product(convertProductModel2Entity(sale.getProduct()))
                .quantity(sale.getQuantity())
                .build();
    }

    private static Sale convertSaleEntity2Model(SaleEntity saleEntity) {
        return new Sale(
                saleEntity.getId(),
                convertEmployeeEntity2Model(saleEntity.getSalesPerson()),
                convertCustomerEntity2Model(saleEntity.getCustomer()),
                convertProductEntity2Model(saleEntity.getProduct()),
                saleEntity.getQuantity()
        );
    }

    private static Employee convertEmployeeEntity2Model(EmployeeEntity employeeEntity) {
        return new Employee(
                employeeEntity.getId(),
                employeeEntity.getFirstName(),
                employeeEntity.getMiddleInitial(),
                employeeEntity.getLastName()
        );
    }

    private static EmployeeEntity convertEmployeeModel2Entity(Employee employee) {
        return EmployeeEntity.builder()
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .middleInitial(employee.getMiddleInitial())
                .lastName(employee.getLastName())
                .build();
    }

    private static Customer convertCustomerEntity2Model(CustomerEntity customerEntity) {
        return new Customer(
                customerEntity.getId(),
                customerEntity.getFirstName(),
                customerEntity.getMiddleInitial(),
                customerEntity.getLastName()
        );
    }

    private static CustomerEntity convertCustomerModel2Entity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .middleInitial(customer.getMiddleInitial())
                .lastName(customer.getLastName())
                .build();
    }

    private static Product convertProductEntity2Model(ProductEntity productEntity) {
        return new Product(
                productEntity.getId(),
                productEntity.getName(),
                productEntity.getPrice()
        );
    }

    private static ProductEntity convertProductModel2Entity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    @Override
    public Sale record(Sale sale) throws SaleAlreadyExistsException {
        if (saleRepository.findById(sale.getId()).isPresent()) {
            throw new SaleAlreadyExistsException();
        }

        EmployeeEntity employeeEntity = convertEmployeeModel2Entity(sale.getSalesPerson());
        CustomerEntity customerEntity = convertCustomerModel2Entity(sale.getCustomer());
        ProductEntity productEntity = convertProductModel2Entity(sale.getProduct());

        SaleEntity saleEntity = saleRepository.save(
                SaleEntity.builder()
                        .id(sale.getId())
                        .salesPerson(employeeEntity)
                        .customer(customerEntity)
                        .product(productEntity)
                        .quantity(sale.getQuantity())
                        .build()
        );
        return convertSaleEntity2Model(saleEntity);
    }

    @Override
    public Sale readById(int id) throws SaleNotFoundException {
        Optional<SaleEntity> entity = saleRepository.findById(id);
        if (entity.isEmpty()) {
            throw new SaleNotFoundException(String.format("Cannot find sale with ID %d", id));
        }
        return convertSaleEntity2Model(entity.get());
    }

    @Override
    public Collection<Sale> readAll() {
        return saleRepository.findAll().stream().map(SaleManagerImpl::convertSaleEntity2Model)
                .collect(Collectors.toList());
    }

    @Override
    public Sale modify(Sale sale) {
        SaleEntity entity = convertSaleModel2Entity(sale);
        return convertSaleEntity2Model(saleRepository.save(entity));
    }

    @Override
    public void delete(Sale sale) {
        saleRepository.delete(convertSaleModel2Entity(sale));
    }
}
