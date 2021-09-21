package hu.uni.eku.tzs.dao;

import hu.uni.eku.tzs.dao.entity.BookInstanceEntity;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookInstanceRepository extends JpaRepository<BookInstanceEntity, String> {

    Collection<BookInstanceEntity> findAllByBook(String isbn);

}
