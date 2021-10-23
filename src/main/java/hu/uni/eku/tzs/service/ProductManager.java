package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Product;
import hu.uni.eku.tzs.service.exceptions.ProductAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.ProductNotFoundException;

import java.util.Collection;

public interface ProductManager {

    Product record(Product product) throws ProductAlreadyExistsException;

    Product readById(int id) throws ProductNotFoundException;

    Collection<Product> readAll();

    Product modify(Product product);

    void delete(Product product);
}
