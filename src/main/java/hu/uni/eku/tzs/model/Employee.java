package hu.uni.eku.tzs.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Employee {
    private int id;

    private String firstName;

    private String middleInitial;

    private String lastName;
}
