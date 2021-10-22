package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<CustomerEntity, String> {
}
