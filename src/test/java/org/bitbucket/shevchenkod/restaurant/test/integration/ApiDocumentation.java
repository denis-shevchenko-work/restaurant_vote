package org.bitbucket.shevchenkod.restaurant.test.integration;

import com.fasterxml.jackson.databind.JsonNode;
import org.bitbucket.shevchenkod.restaurant.RestaurantApplication;
import org.bitbucket.shevchenkod.restaurant.service.transformer.TransformerService;
import org.bitbucket.shevchenkod.restaurant.view.dto.DishDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuDto;
import org.bitbucket.shevchenkod.restaurant.view.dto.MenuItemDto;
import org.bitbucket.shevchenkod.restaurant.model.Dish;
import org.bitbucket.shevchenkod.restaurant.model.Menu;
import org.bitbucket.shevchenkod.restaurant.model.Restaurant;
import org.bitbucket.shevchenkod.restaurant.service.DishService;
import org.bitbucket.shevchenkod.restaurant.service.MenuService;
import org.bitbucket.shevchenkod.restaurant.service.RestaurantService;
import org.bitbucket.shevchenkod.restaurant.service.UserVoteService;
import org.bitbucket.shevchenkod.restaurant.util.JsonUtils;
import org.bitbucket.shevchenkod.restaurant.view.dto.RestaurantDto;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.request.AbstractParametersSnippet;
import org.springframework.restdocs.request.ParameterDescriptor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.snippet.SnippetException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;

import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = RestaurantApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@TestPropertySource(locations="classpath:application-test.properties")
public class ApiDocumentation {

	@Autowired
	WebApplicationContext context;

	@Autowired
	RestaurantService restaurantService;

	@Autowired
	DishService dishService;

	@Autowired
	MenuService menuService;

	@Autowired
	UserVoteService userVoteService;

	@Autowired
	TransformerService<RestaurantDto, Restaurant> restaurantTransformer;

	@Autowired
	TransformerService<DishDto, Dish> dishTransformer;

	@Rule
	public final RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");

	@Autowired
	private UserDetailsService userDetailsService;

	private Authentication adminAuthentication;
	private Authentication userAuthentication;


	private MockMvc mockMvc;

	public Authentication getAdminAuthentication() {
		if (adminAuthentication == null) {
			UserDetails user = userDetailsService.loadUserByUsername("admin");
			adminAuthentication = new TestingAuthenticationToken(user, user.getPassword(), user.getAuthorities().stream().collect(Collectors.toList()));
		}
		return adminAuthentication;	
	}

	public Authentication getUserAuthentication() {
		if (userAuthentication == null) {
			UserDetails user = userDetailsService.loadUserByUsername("user");
			userAuthentication = new TestingAuthenticationToken(user, user.getPassword(), new ArrayList<GrantedAuthority>(user.getAuthorities()));
		}
		return userAuthentication;
	}

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).
				apply(documentationConfiguration(this.restDocumentation).uris().withHost("localhost").withPort(8080)).
				build();
	}

	@Test
	public void indexCrudTest() throws Exception {

		this.mockMvc.perform(get("/"))
				.andExpect(status().isOk())
				.andDo(document("index")).andReturn().getResponse().getContentAsString();

	}


	@Test
	public void restaurantsCrudTest() throws Exception {

		this.mockMvc.perform(get("/api/restaurants?page=0&page_size=10").principal(getUserAuthentication()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("restaurants-list-example",
						requestParameters(
								parameterWithName("page").description("The page to retrieve"),
								parameterWithName("page_size").description("Entries per page"))
						, responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data[]").description("Content")
						)
				//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		String name = "Restaurant Documentation Create";
		this.mockMvc.perform(post("/api/restaurants/").principal(getAdminAuthentication()).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.JSON_MAPPER.writeValueAsString(new RestaurantDto(name)))
		).andExpect(status().isOk())
				.andDo(document("restaurants-create-example",
						requestFields(
								fieldWithPath("name").description("Name of restaurant")
						),
						responseFields(
								fieldWithPath("success").description("Status of request"),
								fieldWithPath("data").description("Content of response"),
								fieldWithPath("data.id").description("Content of response"),
								fieldWithPath("data.name").description("Content of response")
						)
						//		,requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		Optional<Restaurant> restaurant = restaurantService.findByName(name);

		this.mockMvc.perform(get("/api/restaurants/{id}", restaurant.get().getId()).principal(getUserAuthentication()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("restaurants-item-example",
						pathParameters(
								parameterWithName("id").description("Restaurant identifier"))
						, responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data").description("Content")
						)
						//,	requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		name = name + "d";
		restaurant.get().setName(name);
		this.mockMvc.perform(put("/api/restaurants/{id}", restaurant.get().getId()).principal(getAdminAuthentication()).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.JSON_MAPPER.writeValueAsString(new RestaurantDto(restaurant.get().getName())))
		).andExpect(status().isOk())
				.andDo(document("restaurants-update-example",
						pathParameters(
								parameterWithName("id").description("Restaurant identificator")),
						requestFields(
								fieldWithPath("name").description("Name of restaurant")
						),
						responseFields(
								fieldWithPath("success").description("request status")
						)
						//, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		this.mockMvc.perform(delete("/api/restaurants/{id}", restaurant.get().getId()).principal(getAdminAuthentication()))
				.andExpect(status().isOk())
				.andDo(document("restaurants-delete-example",
						pathParameters(parameterWithName("id").description("Restaurant identificator")),
						responseFields(
								fieldWithPath("success").description("request status")
						)
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));


		assertTrue(true);
	}

	@Test
	public void dishesCrudTest() throws Exception {

		String name = "Restaurant Documentation Create";
		this.mockMvc.perform(post("/api/dishes/").principal(getAdminAuthentication()).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(JsonUtils.JSON_MAPPER.writeValueAsString(new DishDto(name)))
		).andExpect(status().isOk())
				.andDo(document("dishes-create-example",
						requestFields(
								fieldWithPath("name").description("Name of restaurant")
						),
						responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data").description("data placeholder"),
								fieldWithPath("data.id").description("saved dish id"),
								fieldWithPath("data.name").description("saved dish name")
						)
						/*, requestFields(
								attributes(
										key("title").value("Request Body")),
								fieldWithPath("name")
										.description("The restaurant's name")
										.attributes(
												key("constraints").value("Must not be null. Must not be empty"))
								)*/
						//		,requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		this.mockMvc.perform(get("/api/dishes?page=0&page_size=10").principal(getUserAuthentication()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("dishes-list-example",
						requestParameters(
								parameterWithName("page").description("The page to retrieve"),
								parameterWithName("page_size").description("Entries per page")),
						responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data").description("request status")
						)
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));



		Optional<Dish> dish = dishService.findByName(name);

		this.mockMvc.perform(get("/api/dishes/{id}", dish.get().getId()).principal(getUserAuthentication()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("dishes-item-example",
						pathParameters(
								parameterWithName("id").description("Restaurant identifier")),
						responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data").description("request status")
						)
						//,	requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		name = name + "d";
		dish.get().setName(name);
		this.mockMvc.perform(put("/api/dishes/{id}", dish.get().getId()).principal(getAdminAuthentication()).contentType(MediaType.APPLICATION_JSON)
						.content(JsonUtils.JSON_MAPPER.writeValueAsString(new DishDto(dish.get().getName())))
		).andExpect(status().isOk())
				.andDo(document("dishes-update-example",
						pathParameters(
								parameterWithName("id").description("Restaurant id")),
						requestFields(
								fieldWithPath("name").description("Name of restaurant")
						),
						responseFields(
								fieldWithPath("success").description("request status")
						)
						//, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		this.mockMvc.perform(delete("/api/dishes/{id}", dish.get().getId()).principal(getAdminAuthentication()))
				.andExpect(status().isOk())
				.andDo(document("dishes-delete-example",
						responseFields(
								fieldWithPath("success").description("request status")
						),
						pathParameters(parameterWithName("id").description("Restaurant identificator"))
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));


		assertTrue(true);
	}

	private Restaurant createRestaurant(String name) {
		return new Restaurant(name);
	}
	private Dish createDish(String name) {
		return new Dish(name);
	}


	private MenuDto createMenu(String dishName, String restaurantName) {
		MenuDto menu = new MenuDto(restaurantName);
		menu.getMenu().add(new MenuItemDto(dishName, new BigDecimal(10.5)));
		return menu;
	}



	@Test
	public void voteTest() throws Exception {

		String dishName = "Votes Documentation vote";
		String restaurantName = "Votes Documentation vote";
		restaurantService.create(createRestaurant(restaurantName));
		dishService.create(createDish(dishName));
		MenuDto menu = createMenu(dishName, restaurantName);
		menuService.overwriteMenuItems(menu);

		SecurityContextHolder.getContext().setAuthentication(getUserAuthentication());

		this.mockMvc.perform(post("/api/votes").principal(getUserAuthentication()).accept(MediaType.APPLICATION_JSON)
						.param("restaurant_name", restaurantName)
		).andExpect(status().isOk())
				.andDo(document("votes-vote-example",
						requestParameters(
								parameterWithName("restaurant_name").description("Restaurant name")
						)
						, responseFields(
								fieldWithPath("success").description("request status")
						)
						//		,requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		this.mockMvc.perform(get("/api/votes").principal(getUserAuthentication()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("votes-item-example"
						, responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data").description("request status"),
								fieldWithPath("data.date").description("request status"),
								fieldWithPath("data.vote").description("request status"),
								fieldWithPath("data.vote.restaurant_name").description("request status"),
								fieldWithPath("data.vote.menu").description("request status")
						)
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		this.mockMvc.perform(get("/api/votes/results").principal(getUserAuthentication()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("votes-list-results-example"
						, responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data[]").description("data placeholder"),
								fieldWithPath("data[].restaurant_name").description("name of the restaurant"),
								fieldWithPath("data[].votes").description("votes")
						)
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		SecurityContextHolder.clearContext();
	}

	@Test
	public void menuCrudTest() throws Exception {

		String dish1Name = "Menus Documentation create";
		dishService.create(createDish(dish1Name));


		String restaurantNameBad = "Menus Documentation error";
		MenuDto menuDto = createMenu(dish1Name, restaurantNameBad);
		this.mockMvc.perform(post("/api/menus").principal(getAdminAuthentication()).contentType(MediaType.APPLICATION_JSON)
						.content(JsonUtils.JSON_MAPPER.writeValueAsString(menuDto))
		).andExpect(status().isBadRequest())
				.andDo(document("error-example",
						requestFields(
								fieldWithPath("restaurant_name").description("Name of restaurant"),
								fieldWithPath("date").description("Menu date"),
								fieldWithPath("menu").description("Menu items"),
								fieldWithPath("menu[].dish_name").description("Name of the dish"),
								fieldWithPath("menu[].price").description("Price of the dish")
						),
						responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("errors").type(JsonFieldType.ARRAY).description("list of pairs {field_name:error_code}")
						)
						//		requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));



		String restaurantName = "Menus Documentation create";
		restaurantService.create(createRestaurant(restaurantName));
		menuDto = createMenu(dish1Name, restaurantName);

		this.mockMvc.perform(post("/api/menus").principal(getAdminAuthentication()).contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtils.JSON_MAPPER.writeValueAsString(menuDto))
		).andExpect(status().isOk())
				.andDo(document("menus-create-example",
						requestFields(
								fieldWithPath("restaurant_name").description("Name of restaurant"),
								fieldWithPath("date").description("Menu date"),
								fieldWithPath("menu").description("Menu items"),
								fieldWithPath("menu[].dish_name").description("Name of the dish"),
								fieldWithPath("menu[].price").description("Price of the dish")
						),
						responseFields(
								fieldWithPath("success").description("request status"))
				));

		Optional<Menu> menuOptional = menuService.findByRestaurantNameAndDate(restaurantName, new Date());

		String dish2Name = "Menus Documentation create 2";
		dishService.create(createDish(dish2Name));
		this.mockMvc.perform(post("/api/menus/{menu_id}/items", menuOptional.get().getId()).principal(getAdminAuthentication()).contentType(MediaType.APPLICATION_JSON)
						.content(JsonUtils.JSON_MAPPER.writeValueAsString(new MenuItemDto(dish2Name, new BigDecimal(3.5))))

		).andExpect(status().isOk())
				.andDo(document("menus-item-create-example",
						pathParameters(
								parameterWithName("menu_id").description("Restaurant identifier")),
						requestFields(
								fieldWithPath("dish_name").description("Name of the dish"),
								fieldWithPath("price").description("Price of the dish")
						),
						responseFields(
								fieldWithPath("success").description("request status"))
						//, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));


		this.mockMvc.perform(get("/api/menus/today").principal(getUserAuthentication()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(document("menus-list-today-example",
						responseFields(
								fieldWithPath("success").description("request status"),
								fieldWithPath("data").description("request status"),
								fieldWithPath("data.date").description("Menu creation date"),
								fieldWithPath("data.list[]").type(JsonFieldType.ARRAY).description("list of menus "),
								fieldWithPath("data.list[].id").type(JsonFieldType.ARRAY).description(" menu id "),
								fieldWithPath("data.list[].restaurant_name").type(JsonFieldType.ARRAY).description("Name of the restaurant"),
								fieldWithPath("data.list[].menu[]").type(JsonFieldType.ARRAY).description("Restaurant's menu items"),
								fieldWithPath("data.list[].menu[].dish_name").type(JsonFieldType.ARRAY).description("name of the dish"),
								fieldWithPath("data.list[].menu[].price").type(JsonFieldType.ARRAY).description("price")
						)
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));


		this.mockMvc.perform(
				delete("/api/menus/{menu_id}/items/{item_id}", menuOptional.get().getId(), menuOptional.get().getItems().iterator().next().getId() ).principal(getAdminAuthentication())
		).andExpect(status().isOk())
				.andDo(document("menus-items-delete-example",
						pathParameters(
								parameterWithName("menu_id").description("menu identifier"),
								parameterWithName("item_id").description("menu item identifier")),
						responseFields(
								fieldWithPath("success").description("request status"))
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));

		this.mockMvc.perform(
				delete("/api/menus/{menu_id}", menuOptional.get().getId()).principal(getAdminAuthentication())
		).andExpect(status().isOk())
				.andDo(document("menus-delete-example",
						pathParameters(
								parameterWithName("menu_id").description("menu identifier")),
						responseFields(
								fieldWithPath("success").description("request status"))
						//		, requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
				));
	}

}