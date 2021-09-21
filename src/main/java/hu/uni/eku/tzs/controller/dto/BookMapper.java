package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Book;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class})
public interface BookMapper {

    BookDto book2bookDto(Book book);

    Book bookDto2Book(BookDto dto);
}
