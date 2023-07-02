package restwithspringBootandjavaerudio.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import restwithspringBootandjavaerudio.controller.BookController;
import restwithspringBootandjavaerudio.data.vo.v1.BookVO;
import restwithspringBootandjavaerudio.exceptions.RequiredObjectIsNullException;
import restwithspringBootandjavaerudio.exceptions.ResourceNotFoundException;
import restwithspringBootandjavaerudio.mapper.DozerMapper;
import restwithspringBootandjavaerudio.model.Book;
import restwithspringBootandjavaerudio.repositories.BookRepository;

import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    BookRepository repository;

    public List<BookVO> findAll() {
        logger.info("Find all books!");

        var persons = DozerMapper.parseListObject(repository.findAll(), BookVO.class);
        persons
                .stream()
                .forEach(p -> p.add(linkTo(methodOn(BookController.class).findById(p.getKey())).withSelfRel()));
        return persons;
    }

    public BookVO findById(Long id) {
        logger.info("Find a book!");
        Book entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this ID!"));
        BookVO vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public BookVO create(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating a book!");
        Book entity = DozerMapper.parseObject(book, Book.class);
        BookVO vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO update(BookVO book) {
        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating a book!");
        Book entity = repository.findById(book.getKey()).orElseThrow(() -> new ResourceNotFoundException("No records founds for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        BookVO vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting a book!");
        Book entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records founds for this ID!"));
        repository.delete(entity);
    }
}
