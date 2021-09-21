package hu.uni.eku.tzs.service;

import hu.uni.eku.tzs.dao.AuthorRepository;
import hu.uni.eku.tzs.dao.BookRepository;
import hu.uni.eku.tzs.dao.entity.AuthorEntity;
import hu.uni.eku.tzs.dao.entity.BookEntity;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookManagerImpl implements BookManager {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private static Book convertBookEntity2Model(BookEntity bookEntity) {
        return new Book(
            bookEntity.getIsbn(),
            new Author(
                bookEntity.getAuthor().getId(),
                bookEntity.getAuthor().getFirstName(),
                bookEntity.getAuthor().getLastName(),
                bookEntity.getAuthor().getNationality()),
            bookEntity.getTitle(),
            bookEntity.getLanguage()
        );
    }

    private static BookEntity convertBookModel2Entity(Book book) {
        return BookEntity.builder()
            .isbn(book.getIsbn())
            .title(book.getTitle())
            .author(convertAuthorModel2Entity(book.getAuthor()))
            .language(book.getLanguage())
            .build();
    }

    private static AuthorEntity convertAuthorModel2Entity(Author author) {
        return AuthorEntity.builder()
            .id(author.getId())
            .firstName(author.getFirstName())
            .lastName(author.getLastName())
            .nationality(author.getNationality())
            .build();
    }

    @Override
    public Book record(Book book) throws BookAlreadyExistsException {
        if (bookRepository.findById(book.getIsbn()).isPresent()) {
            throw new BookAlreadyExistsException();
        }
        AuthorEntity authorEntity = this.readOrRecordAuthor(book.getAuthor());
        BookEntity bookEntity = bookRepository.save(
            BookEntity.builder()
                .isbn(book.getIsbn())
                .author(authorEntity)
                .title(book.getTitle())
                .language(book.getLanguage())
                .build()
        );
        return convertBookEntity2Model(bookEntity);
    }

    @Override
    public Book readByIsbn(String isbn) throws BookNotFoundException {
        Optional<BookEntity> entity = bookRepository.findById(isbn);
        if (entity.isEmpty()) {
            throw new BookNotFoundException(String.format("Cannot find book with ISBN %s", isbn));
        }

        return convertBookEntity2Model(entity.get());
    }

    @Override
    public Collection<Book> readAll() {
        return bookRepository.findAll().stream().map(BookManagerImpl::convertBookEntity2Model)
            .collect(Collectors.toList());
    }

    @Override
    public Book modify(Book book) {
        BookEntity entity = convertBookModel2Entity(book);
        return convertBookEntity2Model(bookRepository.save(entity));
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(convertBookModel2Entity(book));

    }

    private AuthorEntity readOrRecordAuthor(Author author) {
        if (authorRepository.findById(author.getId()).isPresent()) {
            return authorRepository.findById(author.getId()).get();
        }
        return authorRepository.save(
            AuthorEntity.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .nationality(author.getNationality())
                .build()
        );
    }

}
