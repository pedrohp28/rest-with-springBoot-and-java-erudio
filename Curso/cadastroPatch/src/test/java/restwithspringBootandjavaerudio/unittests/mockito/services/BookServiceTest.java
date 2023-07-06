package restwithspringBootandjavaerudio.unittests.mockito.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import restwithspringBootandjavaerudio.converter.mocks.MockBook;
import restwithspringBootandjavaerudio.data.vo.v1.BookVO;
import restwithspringBootandjavaerudio.exceptions.RequiredObjectIsNullException;
import restwithspringBootandjavaerudio.model.Book;
import restwithspringBootandjavaerudio.repositories.BookRepository;
import restwithspringBootandjavaerudio.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockBook();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        List<BookVO> books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        BookVO bookOne = books.get(1);
        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getLinks());

        assertTrue(bookOne.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author's Name1", bookOne.getAuthor());
        assertEquals(11.0, bookOne.getPrice());
        assertEquals("Book's Title1", bookOne.getTitle());
        assertNotNull(bookOne.getLaunchDate());

        BookVO bookFour = books.get(4);
        assertNotNull(bookFour);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getLinks());

        assertTrue(bookFour.toString().contains("links: [</api/book/v1/4>;rel=\"self\"]"));
        assertEquals("Author's Name4", bookFour.getAuthor());
        assertEquals(14.0, bookFour.getPrice());
        assertEquals("Book's Title4", bookFour.getTitle());
        assertNotNull(bookFour.getLaunchDate());

        BookVO bookTwelve = books.get(12);
        assertNotNull(bookTwelve);
        assertNotNull(bookTwelve.getKey());
        assertNotNull(bookTwelve.getLinks());

        assertTrue(bookTwelve.toString().contains("links: [</api/book/v1/12>;rel=\"self\"]"));
        assertEquals("Author's Name12", bookTwelve.getAuthor());
        assertEquals(22.0, bookTwelve.getPrice());
        assertEquals("Book's Title12", bookTwelve.getTitle());
        assertNotNull(bookTwelve.getLaunchDate());
    }

    @Test
    void findById() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        BookVO result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author's Name1", result.getAuthor());
        assertEquals(11.0, result.getPrice());
        assertEquals("Book's Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void create() {
        Book entity = input.mockEntity(1);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);
        vo.setLaunchDate(persisted.getLaunchDate());

        when(repository.save(entity)).thenReturn(persisted);

        BookVO result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author's Name1", result.getAuthor());
        assertEquals(11.0, result.getPrice());
        assertEquals("Book's Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void createWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void update() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        Book persisted = entity;
        persisted.setId(1L);

        BookVO vo = input.mockVO(1);
        vo.setKey(1L);
        vo.setLaunchDate(persisted.getLaunchDate());

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        BookVO result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/book/v1/1>;rel=\"self\"]"));
        assertEquals("Author's Name1", result.getAuthor());
        assertEquals(11.0, result.getPrice());
        assertEquals("Book's Title1", result.getTitle());
        assertNotNull(result.getLaunchDate());
    }

    @Test
    void updateWithNullBook() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void delete() {
        Book entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}