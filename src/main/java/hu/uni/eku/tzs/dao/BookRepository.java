package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<BookEntity, String> {
}
