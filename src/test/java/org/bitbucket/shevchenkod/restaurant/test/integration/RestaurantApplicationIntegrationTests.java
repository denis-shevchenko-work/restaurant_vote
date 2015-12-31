package org.bitbucket.shevchenkod.restaurant.test.integration;

import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.*;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource(locations="classpath:application-test.properties")
public class RestaurantApplicationIntegrationTests {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BASE_AUTH = "Basic ";

	@Value("${local.server.port}")
	int port;

	@Autowired
	private JdbcTemplate template;


	@Before
	public void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		//RestAssured.keystore(keystoreFile, keystorePass);
		//Mockito.reset(mockedServiceGateway);

/*
		HttpHost host = new HttpHost("localhost", 8080, "http");
		//restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactoryBasicAuth(host));


		HttpEntity<String> request = getHttpEntity("", "");
		restTemplate = new RestTemplate();
*/
	}

	private RequestSpecification withBasicAuthentication(String username, String password) {
		return given().auth().preemptive().basic(username, password);
	}

	@Test
	public void contextLoads() {
	}

	@Test
	public void testDefaultSettings() throws Exception {
		assertEquals(new Integer(4), this.template
				.queryForObject("SELECT COUNT(*) FROM account", Integer.class));
	}

	@Test
	public void rootEndpointIsAvailableToEveryone() {
		when().get("/").
				then().statusCode(HttpStatus.OK.value());//.body("success", equalTo("true"));
	}

	@Test
	public void apiEndpointWithoutCredentialsReturnsUnauthorized() {
		when().get("/api").
				then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void apiEndpointWithInvalidUserCredentialsReturnsUnauthorized() {
		String username = "user";
		String password = "124";
		withBasicAuthentication(username, password).
				when().get("/api").
				then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void apiEndpointWithCorrectUserCredentialsReturnsOk() {
		String username = "user";
		String password = "123";
		//ResponseEntity<String> response = restTemplate.exchange("/api", HttpMethod.GET, getHttpEntity(username, password), String.class);

		withBasicAuthentication(username, password).
				when().get("/api").
				then().statusCode(HttpStatus.OK.value());
	}

	@Test
	public void apiEndpointWithCorrectAdminCredentialsReturnsOk() {
		String username = "admin";
		String password = "123";
		withBasicAuthentication(username, password).
				when().get("/api").
				then().statusCode(HttpStatus.OK.value());
	}


	@Test
	public void adminEndpointWithCorrectUserCredentialsReturnsForbidden() {
		String username = "user";
		String password = "123";
		withBasicAuthentication(username, password).
				when().get("/api/admin").
				then().statusCode(HttpStatus.FORBIDDEN.value());
	}

	@Test
	public void adminEndpointWithCorrectAdminCredentialsReturnsOk() {
		String username = "admin";
		String password = "123";
		withBasicAuthentication(username, password).
				when().get("/api/admin").
				then().statusCode(HttpStatus.OK.value());
	}


	@Test
	public void authenticateWithoutPasswordReturnsUnauthorized() {
		withBasicAuthentication("user", "").
				when().get("/api").
				then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void authenticateWithoutUsernameReturnsUnauthorized() {
		withBasicAuthentication("", "123").
				when().get("/api").
				then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

	@Test
	public void authenticateWithoutUsernameAndPasswordReturnsUnauthorized() {
		withBasicAuthentication("", "").
				when().get("/api").
				then().statusCode(HttpStatus.UNAUTHORIZED.value());
	}

}
