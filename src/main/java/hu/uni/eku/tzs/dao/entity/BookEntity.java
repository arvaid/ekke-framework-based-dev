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
@Entity(name = "books")
public class BookEntity {

    @Id
    private String isbn;

    @ManyToOne
    @JoinColumn(name = "author")
    private AuthorEntity author;

    @Column(name = "title")
    private String title;

    @Column(name = "language")
    private String language;
}
