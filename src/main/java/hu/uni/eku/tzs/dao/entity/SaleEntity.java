package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Sales")
public class SaleEntity {
    @Id
    @Column(name = "SalesID")
    private int id;

    @ManyToOne
    @JoinColumn(name = "SalesPersonID")
    private EmployeeEntity salesPerson;

    @ManyToOne
    @JoinColumn(name = "CustomerID")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private ProductEntity product;

    @Column(name = "Quantity")
    private int quantity;
}