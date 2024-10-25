package com.jpdr.apps.demo.webflux.product.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.product.exception.retailer.RetailerNotFoundException;
import com.jpdr.apps.demo.webflux.product.exception.retailer.RetailerRepositoryException;
import com.jpdr.apps.demo.webflux.product.exception.dto.ErrorDto;
import com.jpdr.apps.demo.webflux.product.repository.retailer.impl.RetailerRepositoryImpl;
import com.jpdr.apps.demo.webflux.product.service.dto.retailer.RetailerDto;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.io.IOException;

import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getRetailer;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class RetailerRepositoryTest {

  private RetailerRepositoryImpl retailerRepository;
  
  private static MockWebServer mockWebServer;
  
  private ObjectMapper objectMapper;
  
  
  @BeforeAll
  static void setupOnce() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start(9999);
  }
  
  
  @BeforeEach
  void setupEach() {
    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
    WebClient webClient = WebClient.builder()
      .baseUrl(baseUrl).build();
    retailerRepository = new RetailerRepositoryImpl(webClient);
    objectMapper = new ObjectMapper();
  }
  
  @Test
  @DisplayName("OK - Find By Retailer By Id")
  void givenUserIdWhenFindUserByIdThenReturnUser() throws JsonProcessingException {
    
    RetailerDto expectedRetailer = getRetailer();
    String responseBody = objectMapper.writeValueAsString(expectedRetailer);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.OK.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(retailerRepository.getRetailerById(1))
      .assertNext(receivedRetailer -> {
      assertEquals(expectedRetailer.getId(), receivedRetailer.getId());
      assertEquals(expectedRetailer.getName(), receivedRetailer.getName());
      assertEquals(expectedRetailer.getEmail(), receivedRetailer.getEmail());
      assertEquals(expectedRetailer.getAddress(), receivedRetailer.getAddress());
      assertEquals(expectedRetailer.getSectorId(), receivedRetailer.getSectorId());
      assertEquals(expectedRetailer.getSectorName(), receivedRetailer.getSectorName());
      assertEquals(expectedRetailer.getIsActive(), receivedRetailer.getIsActive());
      assertEquals(expectedRetailer.getCreationDate(), receivedRetailer.getCreationDate());
      assertEquals(expectedRetailer.getDeletionDate(), receivedRetailer.getDeletionDate());
      })
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Retailer By Id - Retailer Not Found")
  void givenRetailerNotFoundWhenFindRetailerByIdThenReturnError() throws JsonProcessingException {
    
    ErrorDto expectedError = new ErrorDto("The retailer 1 wasn't found");
    String responseBody = objectMapper.writeValueAsString(expectedError);
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.NOT_FOUND.value());
    response.setBody(responseBody);
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(retailerRepository.getRetailerById(1))
      .expectError(RetailerNotFoundException.class)
      .verify();
  }
  
  
  @Test
  @DisplayName("Error - Find By Retailer By Id - Internal Server Error")
  void givenInternalServerErrorWhenFindRetailerByIdThenReturnError() {
    
    MockResponse response = new MockResponse();
    response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    mockWebServer.enqueue(response);
    
    StepVerifier.create(retailerRepository.getRetailerById(1))
      .expectError(RetailerRepositoryException.class)
      .verify();
  }
  
  
  
  
  
  @AfterAll
  static void closeOnce() throws IOException {
    mockWebServer.shutdown();
  }
  
  


}
