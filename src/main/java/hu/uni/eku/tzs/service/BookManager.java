package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import java.util.Collection;

public interface BookManager {

    Book record(Book book) throws BookAlreadyExistsException;

    Book readByIsbn(String isbn) throws BookNotFoundException;

    Collection<Book> readAll();

    Book modify(Book book);

    void delete(Book book) throws BookNotFoundException;

}
