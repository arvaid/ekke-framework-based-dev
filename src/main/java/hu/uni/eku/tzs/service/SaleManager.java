package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Sale;
import hu.uni.eku.tzs.service.exceptions.SaleAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.SaleNotFoundException;

import java.util.Collection;

public interface SaleManager {
    Sale record(Sale sale) throws SaleAlreadyExistsException;

    Sale readById(int id) throws SaleNotFoundException;

    Collection<Sale> readAll();

    Sale modify(Sale sale);

    void delete(Sale sale);
}
