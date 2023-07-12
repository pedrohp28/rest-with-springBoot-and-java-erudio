package restwithspringBootandjavaerudio.integrationTests.controller.withYaml;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
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
import restwithspringBootandjavaerudio.integrationTests.vo.PersonVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.TokenVOTest;
import restwithspringBootandjavaerudio.integrationTests.vo.pagedmodel.PagedModelPerson;
import restwithspringBootandjavaerudio.mapper.YMLMapper;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class PersonControllerYamlTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static YMLMapper ymlMapper;

	private static PersonVOTest person;

	@BeforeAll
	public static void setup() {
		ymlMapper = new YMLMapper();
		person = new PersonVOTest();
	}

	@Test
	@Order(0)
	public void authorization() throws JsonMappingException, JsonProcessingException {
		AccountCredentialsVOTest user = new AccountCredentialsVOTest("leandro", "admin123");

		var accessToken = given()
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.basePath("/auth/signin")
					.port(TestConfigs.SERVER_PORT)
					.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(user, ymlMapper)
					.when()
				.post()
					.then()
						.statusCode(200)
							.extract()
							.body()
								.as(TokenVOTest.class, ymlMapper)
							.getAccessToken();

		specification = new RequestSpecBuilder()
				.addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
					.addFilter(new RequestLoggingFilter(LogDetail.ALL))
					.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
	}

	@Test
	@Order(1)
	public void testCreate() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.body(person, ymlMapper)
					.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
						.body()
							.as(PersonVOTest.class, ymlMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Stallman", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(2)
	public void testUpdate() throws JsonMappingException, JsonProcessingException {
		person.setLastName("Joestar");

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.body(person, ymlMapper)
				.when()
				.put()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVOTest.class, ymlMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertTrue(persistedPerson.getEnabled());

		assertEquals(person.getId(), persistedPerson.getId());

		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Joestar", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(3)
	public void testDisablePersonById() throws JsonMappingException, JsonProcessingException {

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId())
				.when()
				.patch("{id}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PersonVOTest.class, ymlMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Joestar", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(4)
	public void testFindById() throws JsonMappingException, JsonProcessingException {
		mockPerson();

		var persistedPerson = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.accept(TestConfigs.CONTENT_TYPE_YML)
					.pathParam("id", person.getId())
					.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
				.body()
				.as(PersonVOTest.class, ymlMapper);

		person = persistedPerson;

		assertNotNull(persistedPerson);

		assertNotNull(persistedPerson.getId());
		assertNotNull(persistedPerson.getFirstName());
		assertNotNull(persistedPerson.getLastName());
		assertNotNull(persistedPerson.getAddress());
		assertNotNull(persistedPerson.getGender());
		assertFalse(persistedPerson.getEnabled());

		assertTrue(persistedPerson.getId() > 0);

		assertEquals("Richard", persistedPerson.getFirstName());
		assertEquals("Joestar", persistedPerson.getLastName());
		assertEquals("New York City, New York, US", persistedPerson.getAddress());
		assertEquals("Male", persistedPerson.getGender());
	}

	@Test
	@Order(5)
	public void testDelete() throws JsonMappingException, JsonProcessingException {

		given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("id", person.getId())
				.when()
				.delete("{id}")
				.then()
				.statusCode(204);

	}

	@Test
	@Order(6)
	public void testFindAll() throws JsonMappingException, JsonProcessingException {

		var wrapper = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, ymlMapper);

		//PersonVOTest persistedPerson = objectMapper.readValue(content, PersonVOTest.class);
		var people = wrapper.getContent();
		PersonVOTest findPersonOne = people.get(0);

		assertNotNull(findPersonOne.getId());
		assertNotNull(findPersonOne.getFirstName());
		assertNotNull(findPersonOne.getLastName());
		assertNotNull(findPersonOne.getAddress());
		assertNotNull(findPersonOne.getGender());
		assertTrue(findPersonOne.getEnabled());

		assertEquals(671, findPersonOne.getId());

		assertEquals("Alic", findPersonOne.getFirstName());
		assertEquals("Terbrug", findPersonOne.getLastName());
		assertEquals("3 Eagle Crest Court", findPersonOne.getAddress());
		assertEquals("Male", findPersonOne.getGender());

		PersonVOTest findPersonFour = people.get(5);

		assertNotNull(findPersonFour.getId());
		assertNotNull(findPersonFour.getFirstName());
		assertNotNull(findPersonFour.getLastName());
		assertNotNull(findPersonFour.getAddress());
		assertNotNull(findPersonFour.getGender());
		assertTrue(findPersonFour.getEnabled());

		assertEquals(905, findPersonFour.getId());

		assertEquals("Allegra", findPersonFour.getFirstName());
		assertEquals("Dome", findPersonFour.getLastName());
		assertEquals("57 Roxbury Pass", findPersonFour.getAddress());
		assertEquals("Female", findPersonFour.getGender());
	}

	@Test
	@Order(7)
	public void testHATEOAS() throws JsonMappingException, JsonProcessingException {

		var unthreatedContent = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.queryParams("page", 3, "size", 10, "direction", "asc")
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(200)
				.extract()
				.body()
				.asString();

		var content = unthreatedContent.replace("\n", "").replace("\r", "");

		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/person/v1/671\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/person/v1/408\""));
		assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/person/v1/840\""));

		assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"prev\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\""));
		assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\""));
		assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\""));

		assertTrue(content.contains("page:  size: 10  totalElements: 1004  totalPages: 101  number: 3"));

	}

	@Test
	@Order(8)
	public void testFindByName() throws JsonMappingException, JsonProcessingException {

		var wrapper = given().spec(specification)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.pathParam("firstName", "abbe")
				.queryParams("page", 0, "size", 6, "direction", "asc")
				.accept(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get("/findPeopleByName/{firstName}")
				.then()
				.statusCode(200)
				.extract()
				.body()
				.as(PagedModelPerson.class, ymlMapper);

		//PersonVOTest persistedPerson = objectMapper.readValue(content, PersonVOTest.class);
		var people = wrapper.getContent();
		PersonVOTest findPersonOne = people.get(0);

		assertNotNull(findPersonOne.getId());
		assertNotNull(findPersonOne.getFirstName());
		assertNotNull(findPersonOne.getLastName());
		assertNotNull(findPersonOne.getAddress());
		assertNotNull(findPersonOne.getGender());
		assertTrue(findPersonOne.getEnabled());

		assertEquals(724, findPersonOne.getId());

		assertEquals("Abbe", findPersonOne.getFirstName());
		assertEquals("Quilleash", findPersonOne.getLastName());
		assertEquals("1 South Avenue", findPersonOne.getAddress());
		assertEquals("Female", findPersonOne.getGender());
	}

	@Test
	@Order(9)
	public void testFindAllWithOutToken() throws JsonMappingException, JsonProcessingException {

		RequestSpecification specificationWithOutToken = specification = new RequestSpecBuilder()
				.setBasePath("/api/person/v1")
				.setPort(TestConfigs.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();

		given().spec(specificationWithOutToken)
				.config(RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)))
				.contentType(TestConfigs.CONTENT_TYPE_YML)
				.when()
				.get()
				.then()
				.statusCode(403);
	}
	
	private void mockPerson() {
		person.setFirstName("Richard");
		person.setLastName("Stallman");
		person.setAddress("New York City, New York, US");
		person.setGender("Male");
		person.setEnabled(true);
	}

}
