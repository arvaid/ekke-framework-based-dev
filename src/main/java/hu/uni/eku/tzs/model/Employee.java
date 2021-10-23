package hu.uni.eku.tzs.model;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int id;

    private String firstName;

    private String middleInitial;

    private String lastName;
}
