package hu.uni.eku.tzs.controller.dto;

import hu.uni.eku.tzs.model.BookInstance;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface BookInstanceMapper {

    BookInstanceDto bookInstance2BookInstanceDto(BookInstance bookInstance);

}
