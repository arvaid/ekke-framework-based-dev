package hu.uni.eku.tzs.model;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private int id;

    private String name;

    private double price;
}
