package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.model.BookInstance;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import java.util.Collection;

public interface BookInstanceManager {

    Collection<BookInstance> readAll();

    Collection<BookInstance> readInstancesOfBook(Book book);

    BookInstance record(Book book) throws BookNotFoundException;

    BookInstance modify(BookInstance bookInstance);

    void delete(String inventoryNo);

    void delete(BookInstance bookInstance);
}
