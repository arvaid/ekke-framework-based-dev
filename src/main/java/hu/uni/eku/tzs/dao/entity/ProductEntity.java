package hu.uni.eku.tzs.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Products")
public class ProductEntity {
    @Id
    @Column(name = "ProductID")
    private int id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Price")
    private double price;
}