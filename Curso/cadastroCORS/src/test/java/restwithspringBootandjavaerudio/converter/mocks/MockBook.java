package restwithspringBootandjavaerudio.converter.mocks;



import restwithspringBootandjavaerudio.data.vo.v1.BookVO;
import restwithspringBootandjavaerudio.data.vo.v1.PersonVO;
import restwithspringBootandjavaerudio.model.Book;
import restwithspringBootandjavaerudio.model.Person;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockBook {


    public Book mockEntity() {
    	return mockEntity(0);
    }
    
    public BookVO mockVO() {
    	return mockVO(0);
    }
    
    public List<Book> mockEntityList() {
        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 14; i++) {
            books.add(mockEntity(i));
        }
        return books;
    }

    public List<BookVO> mockVOList() {
        List<BookVO> books = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            books.add(mockVO(i));
        }
        return books;
    }
    
    public Book mockEntity(Integer number) {
    	Book book = new Book();
    	book.setAuthor("Author's Name" + number);
        book.setLaunchDate(new Date());
        book.setPrice(10.0 + number);
        book.setId(number.longValue());
        book.setTitle("Book's Title" + number);
        return book;
    }

    public BookVO mockVO(Integer number) {
    	BookVO bookVO = new BookVO();
        bookVO.setAuthor("Author's Name" + number);
        bookVO.setLaunchDate(new Date());
        bookVO.setPrice(10.0 + number);
        bookVO.setKey(number.longValue());
        bookVO.setTitle("Book's Title" + number);
        return bookVO;
    }

}
