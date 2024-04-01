package mhmmdnaufall.productservice;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import mhmmdnaufall.productservice.document.Product;
import mhmmdnaufall.productservice.dto.ProductRequest;
import mhmmdnaufall.productservice.dto.ProductResponse;
import mhmmdnaufall.productservice.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	private static final MongoDBContainer MONGO_DB_CONTAINER = new MongoDBContainer("mongo:7.0");

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ProductRepository productRepository;

	@DynamicPropertySource
	private static void setProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.mongodb.uri", MONGO_DB_CONTAINER::getReplicaSetUrl);
	}

	@AfterEach
	void tearDown() {
		productRepository.deleteAll();
	}

	@Test
	void createProduct() throws Exception {
		final var request = productRequest.get();

		mockMvc
				.perform(
						post("/api/products")
								.contentType(MediaType.APPLICATION_JSON)
								.content(objectMapper.writeValueAsString(request))
				)
				.andExpect(status().isCreated());

		assertEquals(1, productRepository.findAll().size());
	}

	@Test
	void getAllProducts() throws Exception {
		productRepository.saveAll(
				Stream.of(productRequest.get(), productRequest.get())
						.map(toProduct)
						.toList()
		);

		mockMvc
				.perform(get("/api/products").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(result -> {
					final var productResponse = objectMapper.readValue(
							result.getResponse().getContentAsString(),
							new TypeReference<List<ProductResponse>>() {}
					);
					assertEquals(2, productResponse.size());
				});

	}

	private final Supplier<ProductRequest> productRequest = () ->
			new ProductRequest("iPhone 13", "iPhone 13", BigDecimal.valueOf(1200));

	private final Function<ProductRequest, Product> toProduct = request ->
			Product.builder().name(request.name()).description(request.description()).price(request.price()).build();

}
