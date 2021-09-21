package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    AuthorDto author2AuthorDto(Author author);

    Author authorDto2Author(AuthorDto dto);
}
