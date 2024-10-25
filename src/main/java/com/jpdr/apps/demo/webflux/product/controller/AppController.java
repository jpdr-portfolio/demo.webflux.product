package com.jpdr.apps.demo.webflux.product.controller;

import com.jpdr.apps.demo.webflux.product.service.AppService;
import com.jpdr.apps.demo.webflux.product.service.dto.product.CategoryDto;
import com.jpdr.apps.demo.webflux.product.service.dto.product.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  
  @GetMapping("/products")
  public ResponseEntity<Flux<ProductDto>> findAllProducts(
    @RequestParam(name="categoryId",required = false) Integer categoryId,
    @RequestParam(name="retailerId",required = false) Integer retailerId){
    return new ResponseEntity<>(appService.findAllProducts(categoryId, retailerId), HttpStatus.OK);
  }
  
  @GetMapping("/products/{productId}")
  public ResponseEntity<Mono<ProductDto>> findProductById(@PathVariable(name="productId") Integer productId){
    return new ResponseEntity<>(appService.findProductById(productId), HttpStatus.OK);
  }
  
  @PostMapping("/products")
  public ResponseEntity<Mono<ProductDto>> createProduct(@RequestBody ProductDto productDto){
    return new ResponseEntity<>(appService.createProduct(productDto), HttpStatus.CREATED);
  }
  
  
  @GetMapping("/categories")
  public ResponseEntity<Flux<CategoryDto>> findAllCategories(){
    return new ResponseEntity<>(appService.findAllCategories(), HttpStatus.OK);
  }
  
  @GetMapping("/categories/{categoryId}")
  public ResponseEntity<Mono<CategoryDto>> findCategoryById(@PathVariable(name="categoryId") Integer categoryId){
    return new ResponseEntity<>(appService.findCategoryById(categoryId), HttpStatus.OK);
  }
  
  @PostMapping("/categories")
  public ResponseEntity<Mono<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDto){
    return new ResponseEntity<>(appService.createCategory(categoryDto), HttpStatus.CREATED);
  }
  
}
