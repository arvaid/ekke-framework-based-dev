package hu.uni.eku.tzs.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "book_instances")
public class BookInstanceEntity {

    @Id
    private String inventoryNo;

    @ManyToOne
    @JoinColumn(name = "book")
    private BookEntity book;

    @Column
    private String state;
}
