package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Sale;

public interface SaleMapper {
    SaleDto sale2saleDto(Sale sale);

    Sale saleDto2sale(SaleDto dto);
}
