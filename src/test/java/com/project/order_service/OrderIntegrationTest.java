package com.project.order_service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.project.order_service.dto.request.OrderCreateRequest;
import com.project.order_service.dto.request.OrderItemRequest;
import com.project.order_service.entity.Item;
import com.project.order_service.repository.ItemRepository;
import com.project.order_service.repository.OrderRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class OrderIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    static WireMockServer wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());

    @BeforeAll
    static void startWireMock() {
        wireMockServer.start();
    }

    @AfterAll
    static void stopWireMock() {
        wireMockServer.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.services.user-service.url", wireMockServer::baseUrl);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        itemRepository.deleteAll();
        wireMockServer.resetAll();
    }

    @Test
    @DisplayName("Успешное создание заказа с обогащением данных пользователя через OpenFeign и сохранением в БД")
    void createOrder_IntegrationSuccess() throws Exception {
        UUID userId = UUID.randomUUID();
        Item item = itemRepository.save(Item.builder()
            .name("Test product")
            .price(new BigDecimal("12.50"))
            .build());
        UUID productId = item.getId();

        wireMockServer.stubFor(get(urlEqualTo("/api/users/" + userId))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "id": "%s",
                                    "email": "alex.vidilin@system.local",
                                    "name": "Алексей",
                                    "surname": "Видилин"
                                }
                                """.formatted(userId))));

        OrderCreateRequest request = new OrderCreateRequest(
                userId,
                List.of(new OrderItemRequest(productId, 2))
        );

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
            .andDo(result -> wireMockServer.verify(1, getRequestedFor(urlEqualTo("/api/users/" + userId))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.user.id").value(userId.toString()))
                .andExpect(jsonPath("$.user.email").value("alex.vidilin@system.local"))
                .andExpect(jsonPath("$.user.name").value("Алексей"))
                .andExpect(jsonPath("$.user.surname").value("Видилин"))
                .andExpect(jsonPath("$.totalPrice").value(25.0))
                .andExpect(jsonPath("$.items[0].itemId").value(productId.toString()));

        org.assertj.core.api.Assertions.assertThat(orderRepository.findAll()).hasSize(1);
    }
}