package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Sale;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CustomerMapper.class, EmployeeMapper.class, ProductMapper.class})
public interface SaleMapper {
    SaleDto sale2saleDto(Sale sale);

    Sale saleDto2sale(SaleDto dto);
}
