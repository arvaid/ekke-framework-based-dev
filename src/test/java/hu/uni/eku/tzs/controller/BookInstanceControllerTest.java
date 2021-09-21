package hu.uni.eku.tzs.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;
import hu.uni.eku.tzs.controller.dto.AuthorDto;
import hu.uni.eku.tzs.controller.dto.BookDto;
import hu.uni.eku.tzs.controller.dto.BookInstanceDto;
import hu.uni.eku.tzs.controller.dto.BookInstanceMapper;
import hu.uni.eku.tzs.controller.dto.BookMapper;
import hu.uni.eku.tzs.model.Author;
import hu.uni.eku.tzs.model.Book;
import hu.uni.eku.tzs.model.BookInstance;
import hu.uni.eku.tzs.model.BookState;
import hu.uni.eku.tzs.service.BookInstanceManager;
import hu.uni.eku.tzs.service.exceptions.BookNotFoundException;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class BookInstanceControllerTest {

    @Mock
    private BookInstanceManager bookInstanceManager;

    @Mock
    private BookInstanceMapper bookInstanceMapper;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookInstanceController controller;

    @Test
    void testReadAllBookInstances() {
        // given
        when(bookInstanceManager.readAll()).thenReturn(TestDataProvider.getDuneInstances());
        Collection<BookInstanceDto> expected = TestDataProvider.getDuneInstanceDto();
        // when
        Collection<BookInstanceDto> actual = controller.readAllBookInstances();
        // then
//        assertThat(actual).usingRecursiveComparison()
//            .isEqualTo(expected);
    }

    @Test
    void testCreateHappyPath() throws BookNotFoundException {
        // given
        BookDto duneDto = TestDataProvider.getDuneDto();
        Book dune = TestDataProvider.getDune();
        when(bookMapper.bookDto2Book(duneDto)).thenReturn(dune);
        BookInstance expectedBookInstance = TestDataProvider.getDuneInstances().get(0);
        when(bookInstanceManager.record(dune)).thenReturn(expectedBookInstance);
        BookInstanceDto expected = TestDataProvider.getDuneInstanceDto().get(0);
        when(bookInstanceMapper.bookInstance2BookInstanceDto(expectedBookInstance)).thenReturn(expected);
        // when
        BookInstanceDto actual = controller.create(duneDto);
        // then
        assertThat(actual).usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    void testCreateWhenBookNotFoundExceptionIsThrown() throws BookNotFoundException {
        // given
        BookDto duneDto = TestDataProvider.getDuneDto();
        Book dune = TestDataProvider.getDune();
        when(bookMapper.bookDto2Book(duneDto)).thenReturn(dune);
        when(bookInstanceManager.record(dune)).thenThrow(new BookNotFoundException());
        // when then
        assertThatThrownBy(() -> controller.create(duneDto))
            .isInstanceOf(ResponseStatusException.class);
    }

    private static class TestDataProvider {

        public static final String DUNE_ISBN = "1-0000";

        public static Author getFrankHerber() {
            return new Author(0, "Frank", "Herber", "American");
        }

        public static Book getDune() {
            return new Book(DUNE_ISBN, getFrankHerber(), "Dune", "English");
        }

        public static AuthorDto getFrankHerbertDto() {
            return AuthorDto.builder()
                .id(0)
                .firstName("Frank")
                .lastName("Herbert")
                .nationality("American")
                .build();
        }

        public static BookDto getDuneDto() {
            return BookDto.builder()
                .isbn(DUNE_ISBN)
                .author(getFrankHerbertDto())
                .title("Dune")
                .language("English")
                .build();
        }

        public static List<BookInstance> getDuneInstances() {
            return List.of(
                new BookInstance("0", getDune(), BookState.BORROWABLE),
                new BookInstance("1", getDune(), BookState.BORROWABLE),
                new BookInstance("2", getDune(), BookState.BORROWABLE)
            );
        }

        public static List<BookInstanceDto> getDuneInstanceDto() {
            return List.of(
                new BookInstanceDto("0", getDuneDto(), BookState.BORROWABLE.toString()),
                new BookInstanceDto("1", getDuneDto(), BookState.BORROWABLE.toString()),
                new BookInstanceDto("2", getDuneDto(), BookState.BORROWABLE.toString())
            );
        }
    }

}