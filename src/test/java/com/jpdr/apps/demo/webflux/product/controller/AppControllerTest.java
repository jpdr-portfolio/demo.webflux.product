package com.jpdr.apps.demo.webflux.product.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.product.service.AppService;
import com.jpdr.apps.demo.webflux.product.service.dto.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.ProductDto;
import com.jpdr.apps.demo.webflux.product.service.dto.RetailerDto;
import com.jpdr.apps.demo.webflux.product.service.dto.SubCategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getCategoryDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getCategoryDtos;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewCategoryDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewProductDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewRetailerDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getNewSubCategoryDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getProductDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getProductDtos;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getRetailerDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getRetailerDtos;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getSubCategoryDto;
import static com.jpdr.apps.demo.webflux.product.util.TestDataGenerator.getSubCategoryDtos;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class AppControllerTest {
  
  @Autowired
  private WebTestClient webTestClient;
  @MockBean
  private AppService appService;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private EventLogger eventLogger;
  
  @Test
  @DisplayName("OK - Find All Products")
  void givenProductsWhenFindAllProductsThenReturnProducts() throws JsonProcessingException {
    
    List<ProductDto> expectedProducts = getProductDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedProducts);
    
    when(appService.findAllProducts(isNull(), isNull(), isNull()))
      .thenReturn(Mono.just(expectedProducts));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/products")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Find Product By Id")
  void givenProductWhenFindProductByIdThenReturnProduct() {
    
    ProductDto expectedProduct = getProductDto();
    
    when(appService.findProductById(anyLong())).thenReturn(Mono.just(expectedProduct));
    
    FluxExchangeResult<ProductDto> exchangeResult = this.webTestClient.get()
      .uri("/products" + "/" + 1)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(ProductDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedProduct -> assertEquals(expectedProduct, receivedProduct))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Products By Category Id")
  void givenCategoryIdWhenFindProductsByCategoryIdThenReturnProducts() throws JsonProcessingException {
    
    List<ProductDto> expectedProducts = getProductDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedProducts);
    
    when(appService.findAllProducts(anyLong(), isNull(), isNull()))
      .thenReturn(Mono.just(expectedProducts));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/products" +"?categoryId=1")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Products By Sub-Category Id")
  void givenSubCategoryIdWhenFindProductsBySubCategoryIdThenReturnProducts() throws JsonProcessingException {
    
    List<ProductDto> expectedProducts = getProductDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedProducts);
    
    when(appService.findAllProducts(isNull(), anyLong(), isNull()))
      .thenReturn(Mono.just(expectedProducts));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/products" +"?subCategoryId=1")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Products By Retailer Id")
  void givenRetailerIdWhenFindProductsByRetailerIdThenReturnProducts() throws JsonProcessingException{
    
    List<ProductDto> expectedProducts = getProductDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedProducts);
    
    when(appService.findAllProducts(isNull(), isNull(), anyLong()))
      .thenReturn(Mono.just(expectedProducts));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/products" +"?retailerId=1")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Product")
  void givenProductWhenCreateProductThenReturnProduct() {
    ProductDto requestProduct = getNewProductDto();
    ProductDto expectedProduct = getProductDto();
    
    when(appService.createProduct(any(ProductDto.class)))
      .thenReturn(Mono.just(expectedProduct));
    
    FluxExchangeResult<ProductDto> exchangeResult = this.webTestClient.post()
      .uri("/products")
      .bodyValue(requestProduct)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isCreated()
      .returnResult(ProductDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedProduct -> assertEquals(expectedProduct, receivedProduct))
      .expectComplete()
      .verify();
  }
  
  
  
  
  
  
  
  @Test
  @DisplayName("OK - Find All Categories")
  void givenCategoriesWhenFindAllCategoriesThenReturnCategories() throws JsonProcessingException{
    
    List<CategoryDto> expectedCategories = getCategoryDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedCategories);
    
    when(appService.findAllCategories()).thenReturn(Mono.just(expectedCategories));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/categories")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Category By Id")
  void givenCategoryWhenFindCategoryByIdThenReturnCategory() {
    
    CategoryDto expectedCategory = getCategoryDto();
    
    when(appService.findCategoryById(anyLong())).thenReturn(Mono.just(expectedCategory));
    
    FluxExchangeResult<CategoryDto> exchangeResult = this.webTestClient.get()
      .uri("/categories" + "/" + 1)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(CategoryDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedCategory -> assertEquals(expectedCategory, receivedCategory))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Category")
  void givenCategoryWhenCreateCategoryThenReturnCategory() {
    
    CategoryDto requestCategory = getNewCategoryDto();
    CategoryDto expectedCategory = getCategoryDto();
    
    when(appService.createCategory(any(CategoryDto.class)))
      .thenReturn(Mono.just(expectedCategory));
    
    FluxExchangeResult<CategoryDto> exchangeResult = this.webTestClient.post()
      .uri("/categories")
      .bodyValue(requestCategory)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isCreated()
      .returnResult(CategoryDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedCategory -> assertEquals(expectedCategory, receivedCategory))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find All Sub-Categories")
  void givenSubCategoriesWhenFindAllSubCategoriesThenReturnSubCategories() throws JsonProcessingException{
    
    List<SubCategoryDto> expectedSubCategories = getSubCategoryDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedSubCategories);
    
    when(appService.findAllSubCategories()).thenReturn(Mono.just(expectedSubCategories));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/subcategories")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find Sub-Category By Id")
  void givenSubCategoryWhenFindSubCategoryByIdThenReturnSubCategory() {
    
    SubCategoryDto expectedSubCategory = getSubCategoryDto();
    
    when(appService.findSubCategoryById(anyLong())).thenReturn(Mono.just(expectedSubCategory));
    
    FluxExchangeResult<SubCategoryDto> exchangeResult = this.webTestClient.get()
      .uri("/subcategories" + "/" + 1)
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(SubCategoryDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedSubCategory -> assertEquals(expectedSubCategory, receivedSubCategory))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create Sub-Category")
  void givenSubCategoryWhenCreateSubCategoryThenReturnSubCategory() {
    
    SubCategoryDto requestSubCategory = getNewSubCategoryDto();
    SubCategoryDto expectedSubCategory = getSubCategoryDto();
    
    when(appService.createSubCategory(any(SubCategoryDto.class)))
      .thenReturn(Mono.just(expectedSubCategory));
    
    FluxExchangeResult<SubCategoryDto> exchangeResult = this.webTestClient.post()
      .uri("/subcategories")
      .bodyValue(requestSubCategory)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isCreated()
      .returnResult(SubCategoryDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedSubCategory -> assertEquals(expectedSubCategory, receivedSubCategory))
      .expectComplete()
      .verify();
  }
  
  
  @Test
  @DisplayName("OK - Find Retailer By Id")
  void givenRetailerWhenFindRetailerByIdThenReturnRetailer(){
    
    RetailerDto expectedRetailer = getRetailerDto();
    
    when(appService.findRetailerById(anyLong()))
      .thenReturn(Mono.just(expectedRetailer));
    
    FluxExchangeResult<RetailerDto> exchangeResult = this.webTestClient.get()
      .uri("/retailers" + "/" + 1)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(RetailerDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedRetailer -> assertEquals(expectedRetailer, receivedRetailer))
      .expectComplete()
      .verify();
  
  }
  
  @Test
  @DisplayName("OK - Find Retailers")
  void givenRetailersWhenFindRetailersThenReturnRetailers() throws IOException {
    
    List<RetailerDto> expectedRetailers = getRetailerDtos();
    String expectedBody = objectMapper.writeValueAsString(expectedRetailers);
    
    when(appService.findAllRetailers())
      .thenReturn(Mono.just(expectedRetailers));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/retailers")
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
    
  }
  
  @Test
  @DisplayName("OK - Create Retailer")
  void givenRetailerWhenCreateRetailerThenReturnRetailer(){
    
    RetailerDto requestRetailer = getNewRetailerDto();
    RetailerDto expectedRetailer = getRetailerDto();
    
    when(appService.createRetailer(any(RetailerDto.class)))
      .thenReturn(Mono.just(expectedRetailer));
    
    FluxExchangeResult<RetailerDto> exchangeResult = this.webTestClient.post()
      .uri("/retailers")
      .accept(MediaType.APPLICATION_JSON)
      .bodyValue(requestRetailer)
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isCreated()
      .returnResult(RetailerDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedRetailer -> assertEquals(expectedRetailer, receivedRetailer))
      .expectComplete()
      .verify();
    
  }
  
  
}
