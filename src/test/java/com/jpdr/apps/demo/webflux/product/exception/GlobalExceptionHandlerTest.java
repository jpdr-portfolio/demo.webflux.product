package com.jpdr.apps.demo.webflux.product.exception;

import com.jpdr.apps.demo.webflux.product.exception.dto.ErrorDto;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.MethodNotAllowedException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  
  @InjectMocks
  GlobalExceptionHandler globalExceptionHandler;
  
  @Test
  @DisplayName("Error - MethodNotAllowedException")
  void givenMethodNotAllowedExceptionWhenHandleExceptionThenReturnError(){
    MethodNotAllowedException exception = new MethodNotAllowedException(HttpMethod.GET, List.of(HttpMethod.POST));
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ServerWebInputException")
  void givenServerWebInputExceptionWhenHandleExceptionThenReturnError(){
    ServerWebInputException exception = new ServerWebInputException("");
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ValidationException")
  void givenValidationExceptionWhenHandleValidationExceptionThenReturnError(){
    ValidationException exception = new ValidationException("");
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - ProductNotFoundException")
  void givenProductNotFoundExceptionWhenHandleExceptionThenReturnError(){
    ProductNotFoundException exception = new ProductNotFoundException(1L);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - CategoryNotFoundException")
  void givenCategoryNotFoundExceptionWhenHandleExceptionThenReturnError(){
    CategoryNotFoundException exception = new CategoryNotFoundException(1L);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - SubCategoryNotFoundException")
  void givenSubCategoryNotFoundExceptionWhenHandleExceptionThenReturnError(){
    SubCategoryNotFoundException exception = new SubCategoryNotFoundException(1L);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - RetailerNotFoundException")
  void givenRetailerNotFoundExceptionWhenHandleExceptionThenReturnError(){
    RetailerNotFoundException exception = new RetailerNotFoundException(1L);
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - RuntimeException")
  void givenRuntimeExceptionWhenHandleRuntimeExceptionThenReturnError(){
    RuntimeException exception = new RuntimeException();
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
  @Test
  @DisplayName("Error - Exception")
  void givenExceptionWhenHandleExceptionThenReturnError(){
    Exception exception = new Exception();
    ResponseEntity<Mono<ErrorDto>> response = globalExceptionHandler.handleException(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
  
}
