package hu.uni.eku.tzs.controller;

import hu.uni.eku.tzs.controller.dto.BookDto;
import hu.uni.eku.tzs.controller.dto.BookMapper;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.service.BookManager;
import hu.uni.eku.tzs.service.exceptions.BookAlreadyExistsException;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Collection;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Api(tags = "Books")
@RequestMapping("/books")
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookManager bookManager;

    private final BookMapper bookMapper;

    @ApiOperation("Read All")
    @GetMapping(value = {"/", ""})
    public Collection<BookDto> readAllBooks() {
        return bookManager.readAll()
            .stream()
            .map(bookMapper::book2bookDto)
            .collect(Collectors.toList());

    }

    @ApiOperation("Record")
    @PostMapping(value = {"", "/"})
    public BookDto create(@Valid @RequestBody BookDto recordRequestDto) {
        Book book = bookMapper.bookDto2Book(recordRequestDto);
        try {
            Book recordedBook = bookManager.record(book);
            return bookMapper.book2bookDto(recordedBook);
        } catch (BookAlreadyExistsException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Update")
    @PutMapping(value = {"", "/"})
    public BookDto update(@Valid @RequestBody BookDto updateRequestDto) {
        Book book = bookMapper.bookDto2Book(updateRequestDto);
        Book updatedBook = bookManager.modify(book);
        return bookMapper.book2bookDto(updatedBook);
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"", "/"})
    public void delete(@RequestParam String isbn) {
        try {
            bookManager.delete(bookManager.readByIsbn(isbn));
        } catch (BookNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation("Delete")
    @DeleteMapping(value = {"/{isbn}"})
    public void deleteBasedOnPath(@PathVariable String isbn) {
        this.delete(isbn);
    }

}
