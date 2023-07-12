package restwithspringBootandjavaerudio.integrationTests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import restwithspringBootandjavaerudio.configs.TestConfigs;
import restwithspringBootandjavaerudio.integrationTests.testContainer.AbstractIntegrationTest;
import restwithspringBootandjavaerudio.integrationTests.vo.AccountCredentialsVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.BookVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.PersonVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.TokenVOTest;
import restwithspringBootandjavaerudio.integrationTests.wrappers.book.WrapperBookVO;
import restwithspringBootandjavaerudio.integrationTests.wrappers.person.WrapperPersonVO;
import restwithspringBootandjavaerudio.model.Book;

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerJsonTest extends AbstractIntegrationTest {
	
	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;

	private static BookVOTest book;
	
	@BeforeAll
	public static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		book = new BookVOTest();
	}
	
	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVOTest user = new AccountCredentialsVOTest("leandro", "admin123");
		
		var accessToken = given()
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(user)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVOTest.class)
							.getAccessToken();
		
		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}
	
	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockBook();
		
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.body(book)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();

		BookVOTest persistedBook = objectMapper.readValue(content, BookVOTest.class);
		book = persistedBook;
		
		assertNotNull(persistedBook);
		
		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());
		
		assertTrue(persistedBook.getId() > 0);
		
		assertEquals("J.K. Rowling", persistedBook.getAuthor());
		assertEquals(50.0, persistedBook.getPrice());
		assertEquals("Harry Potter", persistedBook.getTitle());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		book.setAuthor("JK");

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.body(book)
				.when()
				.put()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		BookVOTest persistedBook = objectMapper.readValue(content, BookVOTest.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());

		assertTrue(persistedBook.getId() > 0);

		assertEquals("JK", persistedBook.getAuthor());
		assertEquals(50.0, persistedBook.getPrice());
		assertEquals("Harry Potter", persistedBook.getTitle());
	}

	@Test
	@Order(3)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockBook();
			
		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
					.header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
					.pathParam("id", book.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
						.body()
							.asString();

		BookVOTest persistedBook = objectMapper.readValue(content, BookVOTest.class);
		book = persistedBook;

		assertNotNull(persistedBook);

		assertNotNull(persistedBook.getId());
		assertNotNull(persistedBook.getAuthor());
		assertNotNull(persistedBook.getLaunchDate());
		assertNotNull(persistedBook.getPrice());
		assertNotNull(persistedBook.getTitle());

		assertTrue(persistedBook.getId() > 0);

		assertEquals("JK", persistedBook.getAuthor());
		assertEquals(50.0, persistedBook.getPrice());
		assertEquals("Harry Potter", persistedBook.getTitle());
	}

	@Test
	@Order(4)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.pathParam("id", book.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);

	}

	@Test
	@Order(5)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 0, "size", 10, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		WrapperBookVO wrapper = objectMapper.readValue(content, WrapperBookVO.class);
		var books = wrapper.getEmbedded().getPeople();
		BookVOTest findPersonOne = books.get(0);

		assertNotNull(findPersonOne.getId());
		assertNotNull(findPersonOne.getAuthor());
		assertNotNull(findPersonOne.getLaunchDate());
		assertNotNull(findPersonOne.getPrice());
		assertNotNull(findPersonOne.getTitle());

		assertEquals(15, findPersonOne.getId());

		assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", findPersonOne.getAuthor());
		assertEquals(54.0, findPersonOne.getPrice());
		assertEquals("Implantando a governança de TI", findPersonOne.getTitle());

		BookVOTest findBookFour = books.get(3);

		assertNotNull(findBookFour.getId());
		assertNotNull(findBookFour.getAuthor());
		assertNotNull(findBookFour.getLaunchDate());
		assertNotNull(findBookFour.getPrice());
		assertNotNull(findBookFour.getTitle());

		assertEquals(8, findBookFour.getId());

		assertEquals("Eric Evans", findBookFour.getAuthor());
		assertEquals(92.0, findBookFour.getPrice());
		assertEquals("Domain Driven Design", findBookFour.getTitle());
	}

	@Test
	@Order(6)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var content = given().spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.queryParams("page", 0, "size", 6, "direction", "asc")
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/15\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/9\"}}}"));
		assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/book/v1/4\"}}}"));

		assertTrue(content.contains("\"_links\":{\"first\":{\"href\":\"http://localhost:8888/api/book/v1?direction=asc&page=0&size=6&sort=author,asc\"}"));
		assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/book/v1?page=0&size=6&direction=asc\"}"));
		assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/book/v1?direction=asc&page=1&size=6&sort=author,asc\"}"));
		assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/book/v1?direction=asc&page=2&size=6&sort=author,asc\"}}"));

		assertTrue(content.contains("\"page\":{\"size\":6,\"totalElements\":15,\"totalPages\":3,\"number\":0}}"));

	}

	@Test
	@Order(7)
	public void testFindAllWithOutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithOutToken = specification = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithOutToken)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
				.when()
				.get()
				.then()
				.statusCode(403);
	}
	
	private void mockBook() {
		book.setAuthor("J.K. Rowling");
		book.setLaunchDate(new Date());
		book.setPrice(50.0);
		book.setTitle("Harry Potter");
	}

}
