package restwithspringBootandjavaerudio.integrationTests.controller.withXml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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

import java.util.Date;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class BookControllerXmlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static XmlMapper objectMapper;

	private static BookVOTest book;

	@BeforeAll
	public static void setup() {
		objectMapper = new XmlMapper();
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
					.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
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
				.contentType(TestConfigs.CONTENT_TYPE_XML)
				.accept(TestConfigs.CONTENT_TYPE_XML)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		//PersonVOTest persistedPerson = objectMapper.readValue(content, PersonVOTest.class);
		List<BookVOTest> books = objectMapper.readValue(content, new TypeReference<List<BookVOTest>>() {});
		BookVOTest findPersonOne = books.get(0);

		assertNotNull(findPersonOne.getId());
		assertNotNull(findPersonOne.getAuthor());
		assertNotNull(findPersonOne.getLaunchDate());
		assertNotNull(findPersonOne.getPrice());
		assertNotNull(findPersonOne.getTitle());

		assertEquals(1, findPersonOne.getId());

		assertEquals("Michael C. Feathers", findPersonOne.getAuthor());
		assertEquals(49.00, findPersonOne.getPrice());
		assertEquals("Working effectively with legacy code", findPersonOne.getTitle());

		BookVOTest findBookFour = books.get(3);

		assertNotNull(findBookFour.getId());
		assertNotNull(findBookFour.getAuthor());
		assertNotNull(findBookFour.getLaunchDate());
		assertNotNull(findBookFour.getPrice());
		assertNotNull(findBookFour.getTitle());

		assertEquals(4, findBookFour.getId());

		assertEquals("Crockford", findBookFour.getAuthor());
		assertEquals(67.00, findBookFour.getPrice());
		assertEquals("JavaScript", findBookFour.getTitle());
	}

	@Test
	@Order(6)
	public void testFindAllWithOutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithOutToken = specification = new RequestSpecBuilder()
				.setBasePath("/api/book/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithOutToken)
				.contentType(TestConfigs.CONTENT_TYPE_XML)
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
